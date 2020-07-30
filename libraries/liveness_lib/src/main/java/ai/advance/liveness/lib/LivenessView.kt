package ai.advance.liveness.lib

import ai.advance.common.IMediaPlayer
import ai.advance.common.camera.GuardianCameraView
import ai.advance.common.utils.JsonUtils
import ai.advance.common.utils.LogUtil
import ai.advance.common.utils.SensorUtil
import ai.advance.liveness.lib.impl.LivenessCallback
import ai.advance.liveness.lib.impl.LivenessGetFaceDataCallback
import android.app.Activity
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.widget.Toast
import java.util.*

class LivenessView constructor(context: Context, attrs: AttributeSet? = null) : GuardianCameraView(context, attrs), GuardianCameraView.CallBack, Detector.DetectionListener, Detector.DetectorInitCallback {

    private var mMediaPlayer: IMediaPlayer? = null

    private var mSensor: SensorUtil? = null

    private var mSDKDetected: Boolean = false

    private var mDetector: Detector? = null

    private var mContext: Context? = null

    private var mCurStep: Int = 0

    var currentDetectionType: Detector.DetectionType? = null

    private var mLivenessCallback: LivenessCallback? = null

    private var mHandler: Handler? = null

    private var mDetectionSteps: ArrayList<Detector.DetectionType>? = null

    val isVertical: Boolean
        get() = true

    private var mRatioWidth = 0
    private var mRatioHeight = 0

    private var mLatestWarnCode: Detector.WarnCode? = null

    private fun checkDetectionTypes(vararg detectionTypes: Detector.DetectionType): Boolean {
        for (i in detectionTypes.indices) {
            when (detectionTypes[i]) {
                Detector.DetectionType.DONE, Detector.DetectionType.AIMLESS, Detector.DetectionType.NONE -> return false
                else -> {}
            }
        }
        return true
    }

    private fun onDetectionTypeNotSupported(errorCode: String, errorMsg: String) {
        if (mLivenessCallback != null) {
            mLivenessCallback?.onDetectorInitComplete(false, errorCode, errorMsg)
        } else {
            LogUtil.sdkLogE(errorMsg)
        }

    }

    @Synchronized
    fun startDetection(callback: LivenessCallback, shuffle: Boolean, vararg detectionTypes: Detector.DetectionType) {
        LivenessResult.clearCache()
        mLivenessCallback = callback
        if (detectionTypes.isEmpty()) {
            val errorCode = "EMPTY_DETECTION_TYPE_LIST"
            val errorMsg = "Detection Types need at least one term"
            onDetectionTypeNotSupported(errorCode, errorMsg)
        } else {
            val detectionTypeEnable = checkDetectionTypes(*detectionTypes)
            if (detectionTypeEnable) {
                mHandler = Handler(Looper.getMainLooper())
                mDetectionSteps = ArrayList(listOf(*detectionTypes))
                if (shuffle) {
                    mDetectionSteps?.shuffle()
                }
                initData()
                if (mDetector?.cameraAngle == -1) {
                    LivenessResult.setErrorCode(ErrorCode.DEVICE_NOT_SUPPORT)
                    callback.onDetectorInitComplete(false, ErrorCode.DEVICE_NOT_SUPPORT.toString(), "camera error")
                } else {
                    if (GuardianLivenessDetectionSDK.isEmulator) {
                        openBackCamera(this)
                    } else {
                        openFrontCamera(this)
                    }
                }
            } else {
                val errorCode = "NOT_SUPPORTED_DETECTION_TYPE"
                val errorMsg = "Type of detection not supported\n detectionType must in (POS_PAW,BLINK,MOUTH)"
                onDetectionTypeNotSupported(errorCode, errorMsg)
            }
        }


    }

    private fun getRatio(size: Camera.Size): Float {
        return this.getPreviewWidth(size).toFloat() / this.getPreviewHeight(size).toFloat()
    }

    private fun getPreviewWidth(size: Camera.Size): Int {
        return if (this.isPortrait) size.height else size.width
    }

    private fun getPreviewHeight(size: Camera.Size): Int {
        return if (this.isPortrait) size.width else size.height
    }

    override fun transformTexture() {
        if (GuardianLivenessDetectionSDK.isEmulator) {
            if (this.mPreviewSize != null) {
                val viewWidth = this.viewWidth.toFloat()
                val viewHeight = this.viewHeight.toFloat()
                val ratio = getRatio(this.mPreviewSize)
                val toRect = RectF(0.0f, 0.0f, viewHeight, viewWidth)
                val fromRect = RectF(0f, 0.0f, viewWidth, viewWidth * ratio)
                this.mCameraTransformWidthRatio = fromRect.width() / toRect.width()
                this.mCameraTransformHeightRatio = fromRect.height() / toRect.height()
                val matrix = Matrix()
                matrix.setRectToRect(fromRect, toRect, Matrix.ScaleToFit.START)
                this.setTransform(matrix)
            }
        }
    }

    fun setAspectRatio(width: Int, height: Int) {
        require(!(width < 0 || height < 0)) { "Size cannot be negative." }
        mRatioWidth = width
        mRatioHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height)
            } else {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth)
            }
        }
    }

    @Synchronized
    override fun startPreview(textureView: GuardianCameraView) {
        try {
            super.startPreview(textureView)
        } catch (e: Exception) {

            LogUtil.sdkLogE("[" + mCameraId + "] startPreview exception：" + e.message)
        }

    }

    override fun restartCamera(cameraId: Int) {
        try {
            super.restartCamera(cameraId)
        } catch (e: Exception) {
            LogUtil.sdkLogE("[" + cameraId + "] restartCamera exception:" + e.message)
        }

    }

    @Synchronized
    override fun open(cameraId: Int) {
        try {
            if (GuardianLivenessDetectionSDK.isEmulator) {
                if (!this.mOnCameraOpening) {
                    try {
                        this.mOnCameraOpening = true
                        this.mCamera = Camera.open(cameraId)
                        val params = this.mCamera.parameters
                        this.mPreviewSize = this.calBestPreviewSize(this.mCamera.parameters)
                        params.setPreviewSize(this.mPreviewSize.width, this.mPreviewSize.height)
                        this.mCameraAngle = this.getCameraAngle(cameraId)
                        this.mCamera.setDisplayOrientation(0)
                        this.mCamera.parameters = params
                        this.transformTexture()
                        this.startAutoFocus()
                    } catch (var4: Exception) {
                    }

                    if (this.mCamera == null && this.mCallBack != null) {
                        this.mCallBack.onCameraOpenFailed()
                    }

                    this.mOnCameraOpening = false
                }
            } else {
                if (!this.mOnCameraOpening) {
                    try {
                        this.mOnCameraOpening = true
                        this.mCamera = Camera.open(cameraId)
                        val cameraInfo = Camera.CameraInfo()
                        Camera.getCameraInfo(cameraId, cameraInfo)
                        this.mCamera.setPreviewTexture(this.surfaceTexture)
                        val params = this.mCamera.parameters

                        this.mCameraAngle = this.getCameraAngle(cameraId)
                        this.mCamera.setDisplayOrientation(this.mCameraAngle)

                        this.mPreviewSize = this.calBestPreviewSize(this.mCamera.parameters)
                        params.setPreviewSize(this.mPreviewSize.width, this.mPreviewSize.height)
                        val size = params.previewSize
                        val previewWidth = size.width
                        val previewHeight = size.height

                        this.setAspectRatio(previewHeight, previewWidth)

                        this.mCamera.parameters = params
                        this.startAutoFocus()
                    } catch (var4: Exception) {
                    }

                    if (this.mCamera == null && this.mCallBack != null) {
                        this.mCallBack.onCameraOpenFailed()
                    }

                    this.mOnCameraOpening = false
                }
            }
        } catch (e: Exception) {
            LivenessResult.setErrorCode(ErrorCode.DEVICE_NOT_SUPPORT)
            LogUtil.sdkLogE("[" + cameraId + "] open camera exception:" + e.message)
        }

    }

    private fun onOpenCameraFailed() {
        LivenessResult.setErrorCode(ErrorCode.DEVICE_NOT_SUPPORT)
        if (callBackEnable()) {
            mLivenessCallback?.onDetectorInitComplete(false, ErrorCode.DEVICE_NOT_SUPPORT.toString(), "The device does not support liveness detection")
        }
    }

    override fun openFrontCamera(callBack: CallBack) {
        if (GuardianLivenessDetectionSDK.isDeviceSupportLiveness) {
            try {
                super.openFrontCamera(callBack)
            } catch (e: Exception) {
                LogUtil.sdkLogE(e.message)
                onOpenCameraFailed()
            }

        } else {
            onOpenCameraFailed()
        }
    }

    @Synchronized
    fun startDetection(callback: LivenessCallback) {
        if (Random().nextBoolean()) {
            startDetection(callback, false, Detector.DetectionType.MOUTH, Detector.DetectionType.BLINK, Detector.DetectionType.POS_YAW)
        } else {
            startDetection(callback, false, Detector.DetectionType.BLINK, Detector.DetectionType.MOUTH, Detector.DetectionType.POS_YAW)
        }
    }

    private fun initData() {
        mContext = context

        mMediaPlayer = IMediaPlayer(mContext)
        mSensor = SensorUtil(mContext)

        mDetector = Detector(mContext as Activity)
        mDetector?.setDetectionListener(this)
    }

    @Synchronized
    fun stopDetection() {
        closeMediaPlayer()
        closeCamera()
    }

    @Synchronized
    fun destroy() {
        mLivenessCallback = null
        stopDetection()
        if (mDetector != null) {
            mDetector?.setDetectionListener(null)
            mDetector?.release()
        }
        if (mHandler != null) {
            mHandler?.removeCallbacksAndMessages(null)
            mHandler = null
        }
        if (mSensor != null) {
            mSensor?.release()
        }
        if (mDetectionSteps != null) {
            mDetectionSteps?.clear()
        }

    }

    fun setSoundPlayEnable(playEnable: Boolean) {
        if (mMediaPlayer != null) {
            mMediaPlayer?.setPlayEnable(playEnable)
        }
    }

    fun playSound(rawId: Int, repeat: Boolean, repeatInterval: Long) {
        if (mMediaPlayer != null) {
            mMediaPlayer?.doPlay(rawId, repeat, repeatInterval)
        }
    }

    @Deprecated("")
    override fun openBackCamera() {
        if (GuardianLivenessDetectionSDK.isEmulator) {
            super.openBackCamera()
        }
    }

    @Deprecated("")
    override fun openBackCamera(callback: CallBack) {
        if (GuardianLivenessDetectionSDK.isEmulator) {
            super.openBackCamera(callback)
        }
    }

    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
        if (isVertical) {
            if (!mSDKDetected) {
                mSDKDetected = true
                mCurStep = 0
                currentDetectionType = mDetectionSteps?.get(mCurStep)
                currentDetectionType?.let { mDetector?.init(it, this@LivenessView) }
            }
        }

    }

    override fun onCameraOpenFailed() {

    }

    override fun onGetYuvData(bytes: ByteArray, size: Camera.Size) {
        mDetector?.doDetection(bytes, size)
    }

    override fun onDetectionSuccess(validFrame: DetectionFrame?): Detector.DetectionType {
        var nextStep: Detector.DetectionType = Detector.DetectionType.DONE
        try {
            if (mContext != null) {
                mCurStep++
                if (mCurStep >= mDetectionSteps?.size?: 0) {
                    if (callBackEnable()) {
                        mLivenessCallback?.onDetectionSuccess()
                    }
                } else {
                    if (callBackEnable()) {
                        nextStep = mDetectionSteps?.get(mCurStep) ?: Detector.DetectionType.DONE
                        currentDetectionType = nextStep
                        mLivenessCallback?.onDetectionActionChanged()
                    }
                }
            }
        } catch (e: Exception) {
            LogUtil.sdkLogE("an error occur :" + e.message)
        }

        return nextStep
    }

    private fun callBackEnable(): Boolean {
        return mHandler != null && mLivenessCallback != null
    }

    private fun closeMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer?.close()
        }
    }

    fun getLivenessData(callback: LivenessGetFaceDataCallback?) {
        stopDetection()
        if (callBackEnable()) {
            callback?.onGetFaceDataStart()
            Thread(Runnable {
                val resultEntity = mDetector?.faceMetaData
                if (callBackEnable() && callback != null) {
                    mHandler?.post {
                        if (resultEntity != null) {
                            if (resultEntity.success) {
                                callback.onGetFaceDataSuccess(resultEntity)
                            } else {
                                callback.onGetFaceDataFailed(resultEntity)
                            }
                        }
                    }

                }
            }).start()
        }
    }


    override fun onDetectionFailed(failedType: Detector.DetectionFailedType) {
        if (failedType != Detector.DetectionFailedType.MULTIPLEFACE) {
            if (callBackEnable()) {
                currentDetectionType?.let { mLivenessCallback?.onDetectionFailed(failedType, it) }
            }
            mLivenessCallback = null
            mDetector?.setDetectionListener(null)
        }
    }

    override fun onDetectionTimeout(timeout: Long) {
        if (callBackEnable()) {
            mLivenessCallback?.onActionRemainingTimeChanged(timeout)
        }
    }

    override fun onFrameDetected(detectionFrame: DetectionFrame?) {
        if (callBackEnable()) {
            val currentFrameWarnCode = detectionFrame?.faceWarnCode
            if (currentFrameWarnCode != mLatestWarnCode) {
                mLatestWarnCode = currentFrameWarnCode
                mLatestWarnCode?.let { mLivenessCallback?.onDetectionFrameStateChanged(it) }
            }
        }
    }

    override fun onFaceReady() {
        if (callBackEnable()) {
            mHandler?.post { mLivenessCallback?.onDetectionActionChanged() }
        }
    }

    override fun onDetectorInitStart() {
        if (callBackEnable()) {
            mHandler?.post { mLivenessCallback?.onDetectorInitStart() }

        }
    }

    override fun onDetectorInitComplete(isValid: Boolean, errorCode: String, message: String) {
        if (callBackEnable()) {
            mHandler?.post {
                if (Detector.isTestAccount) {
                    Toast.makeText(context, "This is a test account for development and debugging only!", Toast.LENGTH_LONG).show()
                }
                mLivenessCallback?.onDetectorInitComplete(isValid, errorCode, message)
            }
        }
    }

    companion object {
        const val NO_RESPONSE = JsonUtils.NO_RESPONSE
    }
}
