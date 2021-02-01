package ai.advance.liveness.lib

import ai.advance.common.entity.BaseResultEntity
import ai.advance.common.utils.LogUtil
import android.app.Activity
import android.hardware.Camera
import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

class Detector(context: Activity) {

    private var mDetectionHandle: Long = 0

    var detectionType: DetectionType? = null

    private var mDetectionListener: DetectionListener? = null
    private var mCurActionDetectionStartTime: Long = 0

    private var mDetectionFrameBlockingQueue: BlockingQueue<DetectionFrame>? = null
    private var mDetectorWorker: DetectorWorker? = null
    private var mHandler: Handler? = null
    private var mFaceImage: MutableMap<String, DetectionFrame>? = null
    private var mDetectorInitCallback: DetectorInitCallback? = null
    var cameraAngle: Int = 0

    private val mBlockingQueueSize = 2

    private var mReleased: Boolean = false

    private val bestDetectionFrame: DetectionFrame?
        get() = mFaceImage?.get("bestImage")

    private var mDetectionResultEntity: BaseResultEntity? = null

    val faceMetaData: BaseResultEntity
        @Synchronized get() {
            if (mDetectionResultEntity != null) {
                return mDetectionResultEntity as BaseResultEntity
            }
            val bestDetectionFrame = bestDetectionFrame
            val resultEntity = BaseResultEntity()
            if (bestDetectionFrame == null) {
                resultEntity.code = "NO_BEST_IMAGE"
                resultEntity.message = "not get best image(sdk message)"
            } else {
                resultEntity.success = true
                mDetectionResultEntity = resultEntity
                LivenessResult.setLivenessResult(bestDetectionFrame.formatBitmap, resultEntity)
            }
            if (!resultEntity.success) {
                LivenessResult.errorCode = "CHECKING_" + resultEntity.code
            }
            return resultEntity
        }


    init {
        cameraAngle = CameraUtils.getCameraAngle(context)
    }

    fun setDetectionListener(detectionListener: DetectionListener?) {
        this.mDetectionListener = detectionListener
    }

    @Synchronized
    fun init(firstDetectionType: DetectionType, callback: DetectorInitCallback) {
        mHandler = Handler(Looper.getMainLooper())
        object : Thread() {
            override fun run() {
                mDetectorInitCallback = callback
                val modelCopySuccess = ModelUtils.copyModelsToData()
                if (modelCopySuccess) {
                    if (mDetectorWorker != null) {
                        doOnDetectorInitComplete(false, ErrorCode.ALREADY_INIT.toString(), "already init")
                    } else {
                        mFaceImage = HashMap()
                        mDetectionFrameBlockingQueue = LinkedBlockingDeque(mBlockingQueueSize)
                        changeDetectionType(firstDetectionType)
                        authCheck()
                    }
                } else {
                    doOnDetectorInitComplete(false, ErrorCode.MODEL_ERROR.toString(), "model error")
                }

            }
        }.start()
    }

    private fun onSdkAuthSuccess() {
        LogUtil.sdkLog("sdk auth success")
        mDetectionHandle = LivenessJNI.nativeInit()//native init

        if (mDetectionHandle == 0L) {// init error
            doOnDetectorInitComplete(false, ErrorCode.MODEL_ERROR.toString(), "model error")
        } else {//start detection worker
            doOnDetectorInitComplete(true, "", "")
            startDetectorWorker()
        }
    }

    private fun doOnDetectorInitComplete(isValid: Boolean, errorCode: String, message: String) {
        if (!isValid) {
            LivenessResult.errorCode = "AUTH_$errorCode"
            LivenessResult.errorMsg = message
        }
        mDetectorInitCallback?.onDetectorInitComplete(isValid, errorCode, message)
    }

    private fun authCheck() {
        LogUtil.sdkLog("auth checking")
        mDetectorInitCallback?.onDetectorInitStart()
        if (GuardianLivenessDetectionSDK.isSdkAuthSuccess) {//sdk already auth
            onSdkAuthSuccess()
        } else {
            LivenessJNI.nativeAuth(Locale.getDefault().toString())
            onSdkAuthSuccess()
            GuardianLivenessDetectionSDK.isSdkAuthSuccess = true
        }
    }

    @Synchronized
    private fun startDetectorWorker() {
        if (mDetectorWorker == null) {
            mDetectorWorker = DetectorWorker()
            mDetectorWorker?.start()
        }
    }

    inner class DetectorWorker : Thread("liveness_worker") {
        var mBestFaceQuality = 0f
        @Volatile
        var working = true
        private var actionStatus: ActionStatus? = ActionStatus.FACENODEFINE
        var mPrepareStartTime: Long = 0
        @Volatile
        private var mOnWaitingNextAction = false

        override fun run() {
            setActionStartTimeMills()
            mPrepareStartTime = System.currentTimeMillis()
            loop@ while (working) {
                try {
                    if (mOnWaitingNextAction) {
                        sleep(10)
                        continue
                    }
                    val curDetectionFrame: DetectionFrame?
                    // check if we should continue running
                    if (detectionType == DetectionType.DONE)
                        break

                    // retrieve the frame
                    try {
                        curDetectionFrame = mDetectionFrameBlockingQueue?.poll(300, TimeUnit.MILLISECONDS)
                        if (curDetectionFrame == null || curDetectionFrame.detectionType != detectionType) {
                            continue
                        }
                    } catch (e: Exception) {
                        // TODO: frame time out issue
                        continue
                    }

                    // check timeout
                    if (Math.abs(System.currentTimeMillis() - mCurActionDetectionStartTime) >= ACTION_TIME_LIMIT) {
                        if (detectionType != DetectionType.AIMLESS) {
                            onDetectionFailed(DetectionFailedType.TIMEOUT)
                            working = false
                            break
                        }
                    }

                    // run detection
                    val detectorStartTimer = System.currentTimeMillis()
                    curDetectionFrame.bitmap
                    val rawResult = detectionType?.mInterValue?.let {
                        LivenessJNI.nativeDetection(mDetectionHandle,
                                curDetectionFrame.bitmapPixels,
                                curDetectionFrame.width,
                                curDetectionFrame.height,
                                it)
                    }

                    val detTime = Integer.parseInt((System.currentTimeMillis() - detectorStartTimer).toString())
                    LogUtil.debug("liveness_time", detTime.toString() + "ms")
                    LogUtil.debug("json_result", rawResult)

                    // analyse the result

                    val livenessJson = JSONObject(rawResult)
                    curDetectionFrame.update(livenessJson)
                    // TODO: remove the dependence of enum order
                    if (actionStatus == ActionStatus.FACEMOTIONREADY) {
                        mDetectionListener?.onFaceReady()
                    }
                    actionStatus = curDetectionFrame.mActionStatus
                    when (WarnCode.valueOf(livenessJson.optInt("code"))) {

                        WarnCode.OK_ACTIONDONE -> {
                            mOnWaitingNextAction = true
                            mHandler?.post {
                                if (mDetectionListener != null) {
                                    mDetectionListener?.onFrameDetected(curDetectionFrame)
                                    detectionType = mDetectionListener?.onDetectionSuccess(curDetectionFrame)
                                    mOnWaitingNextAction = false
                                    if (detectionType == DetectionType.DONE) { // done
                                        working = false
                                    } else {
                                        changeDetectionType(detectionType)
                                    }
                                }
                            }
                        }
                        WarnCode.FACEMISSING -> {
                            if (actionStatus?.isFaceNotReady == true) {
                                setActionStartTimeMills()
                                mHandler?.post {
                                    mDetectionListener?.onFrameDetected(curDetectionFrame)
                                }
                                break@loop
                            }
                            run {
                                mHandler?.post {
                                    mDetectionListener?.onDetectionTimeout(mCurActionDetectionStartTime + ACTION_TIME_LIMIT - System.currentTimeMillis())
                                }
                                mHandler?.post {
                                    mDetectionListener?.onFrameDetected(curDetectionFrame)
                                }
                            }
                        }
                        WarnCode.WARN_EYE_OCCLUSION, WarnCode.WARN_MOTION, WarnCode.WARN_LARGE_YAW, WarnCode.WARN_MOUTH_OCCLUSION, WarnCode.FACEINACTION -> {
                            mHandler?.post {
                                mDetectionListener?.onDetectionTimeout(mCurActionDetectionStartTime + ACTION_TIME_LIMIT - System.currentTimeMillis())
                            }
                            mHandler?.post {
                                mDetectionListener?.onFrameDetected(curDetectionFrame)
                            }
                        }
                        WarnCode.ERROR_FACEMISSING -> {}
                        WarnCode.ERROR_MULTIPLEFACES -> {}
                        WarnCode.ERROR_MUCHMOTION -> {
                            onDetectionFailed(DetectionFailedType.MUCHMOTION)
                            working = false
                        }
                        WarnCode.FACECAPTURE -> {
                            val imageQuality = curDetectionFrame.faceInfo?.faceQuality
                            LogUtil.debug("bestImage", "" + imageQuality)
                            if (imageQuality != null) {
                                if (imageQuality > mBestFaceQuality) {
                                    mBestFaceQuality = imageQuality
                                    mFaceImage?.set("bestImage", curDetectionFrame)
                                }
                            }
                            setActionStartTimeMills()
                            mHandler?.post {
                                mDetectionListener?.onFrameDetected(curDetectionFrame)
                            }
                        }
                        else -> {
                            setActionStartTimeMills()
                            mHandler?.post {
                                mDetectionListener?.onFrameDetected(curDetectionFrame)
                            }
                        }
                    }//onDetectionFailed(DetectionFailedType.FACEMISSING);
                    //working = false;
                    //onDetectionFailed(DetectionFailedType.MULTIPLEFACE);
                    //working = false;
                } catch (ignored: Exception) {
                }

            }

        }

    }

    private fun setActionStartTimeMills() {
        mCurActionDetectionStartTime = System.currentTimeMillis()
    }

    private fun onDetectionFailed(detectionFailedType: DetectionFailedType) {
        LogUtil.sdkLog("liveness detection failed,reason:" + detectionFailedType.name)
        LivenessResult.setErrorCode(detectionFailedType)
        LivenessResult.errorDetectionType = detectionType
        mHandler?.post {
            mDetectionListener?.onDetectionFailed(detectionFailedType)
        }

    }

    @Synchronized
    fun release() {
        if (mReleased) {
            return
        }
        mReleased = true
        try {
            if (mDetectorWorker != null) {
                if (mDetectorWorker?.working == false) {//If this Boolean value is true, it means that we have not determined that the liveness detection is over. The user manually triggered the release method, which can be considered as the user giving up the detection
                    LivenessResult.setErrorCode(ErrorCode.USER_GIVE_UP)
                }
                mDetectorWorker?.working = false
                try {
                    mDetectorWorker?.join()
                } catch (ignored: InterruptedException) {
                }

                mDetectorWorker = null
            }
            if (mDetectorInitCallback != null) {
                mDetectorInitCallback = null
            }
            if (mDetectionHandle != 0L) {
                LivenessJNI.nativeRelease(mDetectionHandle)
                mDetectionHandle = 0
            }
            mDetectionFrameBlockingQueue = null
        } catch (ignored: Exception) {

        }

    }

    private fun changeDetectionType(detectionType: DetectionType?) {
        LogUtil.sdkLog("next action:$detectionType")
        this.detectionType = detectionType
        setActionStartTimeMills()
    }

    @Synchronized
    fun doDetection(yuvData: ByteArray, previewSize: Camera.Size): Boolean {
        return try {
            val detectionFrame = detectionType?.let { DetectionFrame(yuvData, cameraAngle, previewSize.width, previewSize.height, it) }
            this.mDetectionFrameBlockingQueue?.offer(detectionFrame) ?: false
        } catch (error: Exception) {
            LogUtil.debug("do_detection", error.toString())
            false
        }
    }

    enum class DetectionFailedType {
        //ACTIONBLEND,
        //NOTVIDEO,
        TIMEOUT,
        //MASK,
        //FACENOTCONTINUOUS,
        //TOOMANYFACELOST,
        //NOFACE,
        WEAKLIGHT,
        STRONGLIGHT,
        FACEMISSING,
        BADNETWORK,
        MULTIPLEFACE,
        MUCHMOTION,
        UNAUTHORIZED,
        UNSUPPORT_DEVICE
    }

    enum class ActionStatus {
        NOFACE,
        FACECHECKSIZE,
        FACESIZEREADY,
        FACECENTERREADY,
        FACEFRONTALREADY,
        FACECAPTUREREADY,
        FACEMOTIONREADY,
        FACEBLINK,
        FACEMOUTH,
        FACEYAW,
        FACEINIT,
        FACENODEFINE;

        val isFaceNotReady: Boolean
            get() = this.ordinal < ordinal

        companion object {

            fun valueOf(value: Int): ActionStatus {
                return when (value) {
                    1 -> NOFACE
                    2 -> FACECHECKSIZE
                    3 -> FACESIZEREADY
                    4 -> FACECENTERREADY
                    5 -> FACEFRONTALREADY
                    6 -> FACECAPTUREREADY
                    7 -> FACEMOTIONREADY
                    8 -> FACEBLINK
                    9 -> FACEMOUTH
                    10 -> FACEYAW
                    else -> FACENODEFINE
                }
            }
        }
    }


    enum class WarnCode {
        FACEMISSING,
        FACELARGE,
        FACESMALL,
        FACENOTCENTER,
        FACENOTFRONTAL,
        FACENOTSTILL,


        WARN_MULTIPLEFACES,
        WARN_EYE_OCCLUSION,
        WARN_MOUTH_OCCLUSION,
        FACECAPTURE,
        FACEINACTION,
        OK_ACTIONDONE,
        ERROR_MULTIPLEFACES,
        ERROR_FACEMISSING,
        ERROR_MUCHMOTION,
        OK_COUNTING,
        OK_DEFAULT,
        WARN_MOTION,
        WARN_LARGE_YAW;


        companion object {

            fun valueOf(value: Int): WarnCode {
                when (value) {
                    1 -> return FACEMISSING
                    2 -> return FACELARGE
                    3 -> return FACESMALL
                    4 -> return FACENOTCENTER
                    5 -> return FACENOTFRONTAL
                    6 -> return FACENOTSTILL
                    7 -> return WARN_MULTIPLEFACES
                    8 -> return WARN_EYE_OCCLUSION
                    9 -> return WARN_MOUTH_OCCLUSION
                    10 -> return FACECAPTURE
                    11 -> return FACEINACTION
                    12 -> return OK_ACTIONDONE
                    13 -> return ERROR_MULTIPLEFACES
                    14 -> return ERROR_FACEMISSING
                    15 -> return ERROR_MUCHMOTION
                    16 -> return OK_COUNTING
                    17 -> return WARN_MOTION
                    18 -> return WARN_LARGE_YAW
                    else -> return OK_DEFAULT
                }
            }
        }
    }


    enum class DetectionType constructor(val mInterValue: Int) {
        NONE(0),
        BLINK(1),
        MOUTH(2),
        POS_YAW(3),
        DONE(6),
        //POS_PITCH(4),
        //POS_YAW_LEFT(7),
        //POS_YAW_RIGHT(8),

        //POS_PITCH_UP(9),
        //POS_PITCH_DOWN(10),
        AIMLESS(5)
    }

    interface DetectionListener {
        fun onDetectionSuccess(validFrame: DetectionFrame?): DetectionType
        fun onDetectionFailed(failedType: DetectionFailedType)
        fun onDetectionTimeout(timeout: Long)
        fun onFrameDetected(detectionFrame: DetectionFrame?)
        fun onFaceReady()
    }

    interface DetectorInitCallback {
        fun onDetectorInitStart()
        fun onDetectorInitComplete(isValid: Boolean, errorCode: String, message: String)
    }

    companion object {
        private const val ACTION_TIME_LIMIT = 30_000L
        var isTestAccount: Boolean = false
    }

}