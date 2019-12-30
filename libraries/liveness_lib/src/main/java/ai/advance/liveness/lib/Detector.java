package ai.advance.liveness.lib;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import ai.advance.common.entity.BaseResultEntity;
import ai.advance.common.utils.LogUtil;

/**
 * liveness detection main logic class
 */
public final class Detector {
    /**
     * every action limit time (ms)
     */
    private static final Long ACTION_TIME_LIMIT = 10_000L;
    /**
     * native init result
     */
    private long mDetectionHandle;
    /**
     * current action type
     */
    private DetectionType mDetectionType;
    /**
     * UI callback
     */
    private DetectionListener mDetectionListener;
    private long mCurActionDetectionStartTime;

    private BlockingQueue<DetectionFrame> mDetectionFrameBlockingQueue;
    private DetectorWorker mDetectorWorker;
    private Handler mHandler;
    private Map<String, DetectionFrame> mFaceImage;
    private DetectorInitCallback mDetectorInitCallback;
    static boolean isTestAccount;
    int cameraAngle;

    private final int mBlockingQueueSize = 2;


    public Detector(Activity context) {
        cameraAngle = CameraUtils.getCameraAngle(context);
    }

    public void setDetectionListener(DetectionListener detectionListener) {
        this.mDetectionListener = detectionListener;
    }

    /**
     * init detector
     *
     * @param firstDetectionType the first action type
     * @param callback           init callback
     */
    public synchronized void init(final DetectionType firstDetectionType, final DetectorInitCallback callback) {
        mHandler = new Handler(Looper.getMainLooper());
        new Thread() {
            @Override
            public void run() {
                mDetectorInitCallback = callback;
                boolean modelCopySuccess = ModelUtils.copyModelsToData();
                if (modelCopySuccess) {
                    if (mDetectorWorker != null) {
                        doOnDetectorInitComplete(false, ErrorCode.ALREADY_INIT.toString(), "already init");
                    } else {
                        mFaceImage = new HashMap<>();
                        mDetectionFrameBlockingQueue = new LinkedBlockingDeque<>(mBlockingQueueSize);
                        changeDetectionType(firstDetectionType);
                        authCheck();
                    }
                } else {
                    doOnDetectorInitComplete(false, ErrorCode.MODEL_ERROR.toString(), "model error");
                }

            }
        }.start();
    }

    /**
     * auth success
     */
    private void onSdkAuthSuccess() {
        LogUtil.sdkLog("sdk auth success");
        mDetectionHandle = LivenessJNI.nativeInit();//native init

        if (mDetectionHandle == 0L) {// init error
            doOnDetectorInitComplete(false, ErrorCode.MODEL_ERROR.toString(), "model error");
        } else {//start detection worker
            doOnDetectorInitComplete(true, "", "");
            startDetectorWorker();
        }
    }

    /**
     * init complete
     */
    private void doOnDetectorInitComplete(boolean isValid, String errorCode, String message) {
        if (!isValid) {
            LivenessResult.setErrorCode("AUTH_" + errorCode);
            LivenessResult.setErrorMsg(message);
        }
        if (mDetectorInitCallback != null) {
            mDetectorInitCallback.onDetectorInitComplete(isValid, errorCode, message);
        }
    }

    /**
     * auth check
     */
    private void authCheck() {
        LogUtil.sdkLog("auth checking");
        if (mDetectorInitCallback != null) {
            mDetectorInitCallback.onDetectorInitStart();
        }
        if (GuardianLivenessDetectionSDK.isSdkAuthSuccess()) {//sdk already auth
            onSdkAuthSuccess();
        } else {
            LivenessJNI.nativeAuth(Locale.getDefault().toString());
            onSdkAuthSuccess();
            GuardianLivenessDetectionSDK.isAuthSuccess = true;
        }
    }

    private synchronized void startDetectorWorker() {
        if (mDetectorWorker == null) {
            mDetectorWorker = new DetectorWorker();
            mDetectorWorker.start();
        }
    }

    class DetectorWorker extends Thread {
        float mBestFaceQuality = 0;
        volatile boolean working = true;
        private ActionStatus actionStatus = ActionStatus.FACENODEFINE;
        long mPrepareStartTime;
        volatile private boolean mOnWaitingNextAction = false;

        DetectorWorker() {
            super("liveness_worker");
        }

        public void run() {
            setActionStartTimeMills();
            mPrepareStartTime = System.currentTimeMillis();
            while (working) {
                try {
                    if (mOnWaitingNextAction) {
                        Thread.sleep(10);
                        continue;
                    }
                    final DetectionFrame curDetectionFrame;
                    // check if we should continue running
                    if (mDetectionType == DetectionType.DONE)
                        break;

                    // retrieve the frame
                    try {
                        curDetectionFrame = mDetectionFrameBlockingQueue.poll(300, TimeUnit.MILLISECONDS);
                        if (curDetectionFrame == null || curDetectionFrame.getDetectionType() != mDetectionType) {
                            continue;
                        }
                    } catch (Exception e) {
                        // TODO: frame time out issue
                        continue;
                    }

                    // check timeout
                    if (Math.abs(System.currentTimeMillis() - mCurActionDetectionStartTime) >= ACTION_TIME_LIMIT) {
                        if (mDetectionType != DetectionType.AIMLESS) {
                            onDetectionFailed(DetectionFailedType.TIMEOUT);
                            working = false;
                            break;
                        }
                    }

                    // run detection
                    long detectorStartTimer = System.currentTimeMillis();
                    curDetectionFrame.getBitmap();
                    String rawResult = LivenessJNI.nativeDetection(mDetectionHandle,
                            curDetectionFrame.getBitmapPixels(),
                            curDetectionFrame.getWidth(),
                            curDetectionFrame.getHeight(),
                            mDetectionType.mInterValue);

                    int detTime = Integer.parseInt(String.valueOf(System.currentTimeMillis() - detectorStartTimer));
                    LogUtil.debug("liveness_time", detTime + "ms");
                    LogUtil.debug("json_result", rawResult);

                    // analyse the result

                    JSONObject livenessJson = new JSONObject(rawResult);
                    curDetectionFrame.update(livenessJson);
                    // TODO: remove the dependence of enum order
                    if (actionStatus == ActionStatus.FACEMOTIONREADY) {
                        mDetectionListener.onFaceReady();
                    }
                    actionStatus = curDetectionFrame.mActionStatus;
                    switch (WarnCode.valueOf(livenessJson.optInt("code"))) {

                        case OK_ACTIONDONE: {
                            mOnWaitingNextAction = true;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mDetectionListener != null) {
                                        mDetectionListener.onFrameDetected(curDetectionFrame);
                                        mDetectionType = mDetectionListener.onDetectionSuccess(curDetectionFrame);
                                        mOnWaitingNextAction = false;
                                        if (mDetectionType == DetectionType.DONE) { // done
                                            working = false;
                                        } else {
                                            changeDetectionType(mDetectionType);
                                        }
                                    }
                                }
                            });
                        }
                        break;
                        case FACEMISSING:
                            if (actionStatus.isFaceNotReady()) {
                                setActionStartTimeMills();
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mDetectionListener != null)
                                            mDetectionListener.onFrameDetected(curDetectionFrame);
                                    }
                                });
                                break;
                            }
                        case WARN_EYE_OCCLUSION:
                        case WARN_MOTION:
                        case WARN_LARGE_YAW:
                        case WARN_MOUTH_OCCLUSION:
                        case FACEINACTION: {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mDetectionListener != null) {
                                        mDetectionListener.onDetectionTimeout(mCurActionDetectionStartTime + ACTION_TIME_LIMIT - System.currentTimeMillis());
                                    }
                                }
                            });
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mDetectionListener != null)
                                        mDetectionListener.onFrameDetected(curDetectionFrame);
                                }
                            });
                        }
                        break;
                        case ERROR_FACEMISSING:
                            onDetectionFailed(DetectionFailedType.FACEMISSING);
                            working = false;
                            break;
                        case ERROR_MULTIPLEFACES:
                            onDetectionFailed(DetectionFailedType.MULTIPLEFACE);
                            working = false;
                            break;
                        case ERROR_MUCHMOTION:
                            onDetectionFailed(DetectionFailedType.MUCHMOTION);
                            working = false;
                            break;
                        case FACECAPTURE: {

                            float imageQuality = curDetectionFrame.mFaceInfo.faceQuality;
                            LogUtil.debug("bestImage", "" + imageQuality);
                            if (imageQuality > mBestFaceQuality) {
                                mBestFaceQuality = imageQuality;
                                mFaceImage.put("bestImage", curDetectionFrame);
                            }
                            setActionStartTimeMills();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mDetectionListener != null) {
                                        mDetectionListener.onFrameDetected(curDetectionFrame);
                                    }
                                }
                            });
                        }
                        break;
                        default:
                            setActionStartTimeMills();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mDetectionListener != null) {
                                        mDetectionListener.onFrameDetected(curDetectionFrame);
                                    }
                                }
                            });
                    }
                } catch (Exception ignored) {
                }
            }

        }

    }

    /**
     * Set the current action start timestamp
     */
    private void setActionStartTimeMills() {
        mCurActionDetectionStartTime = System.currentTimeMillis();
    }


    /**
     * detection failed
     *
     * @param detectionFailedType failed type
     */
    private void onDetectionFailed(final DetectionFailedType detectionFailedType) {
        LogUtil.sdkLog("liveness detection failed,reason:" + detectionFailedType.name());
        LivenessResult.setErrorCode(detectionFailedType);
        LivenessResult.setErrorDetectionType(mDetectionType);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mDetectionListener != null) {
                    mDetectionListener.onDetectionFailed(detectionFailedType);
                }
            }
        });

    }


    /**
     * Whether resources have been released to prevent repeated calls
     */
    private boolean mReleased;

    /**
     * release memory
     */
    public synchronized void release() {
        if (mReleased) {
            return;
        }
        mReleased = true;
        try {
            if (mDetectorWorker != null) {
                if (mDetectorWorker.working) {//If this Boolean value is true, it means that we have not determined that the liveness detection is over. The user manually triggered the release method, which can be considered as the user giving up the detection
                    LivenessResult.setErrorCode(ErrorCode.USER_GIVE_UP);
                }
                mDetectorWorker.working = false;
                try {
                    mDetectorWorker.join();
                } catch (InterruptedException ignored) {
                }
                mDetectorWorker = null;
            }
            if (mDetectorInitCallback != null) {
                mDetectorInitCallback = null;
            }
            if (mDetectionHandle != 0L) {
                LivenessJNI.nativeRelease(mDetectionHandle);
                mDetectionHandle = 0;
            }
            mDetectionFrameBlockingQueue = null;
        } catch (Exception ignored) {

        }
    }

    private void changeDetectionType(DetectionType detectionType) {
        LogUtil.sdkLog("next action:" + detectionType);
        mDetectionType = detectionType;
        setActionStartTimeMills();
    }

    public DetectionType getDetectionType() {
        return mDetectionType;
    }

    /**
     * Get the best frame
     */
    private DetectionFrame getBestDetectionFrame() {
        return mFaceImage.get("bestImage");
    }

    private BaseResultEntity mDetectionResultEntity;

    /**
     * get face data
     *
     * @return face data entity
     */
    public synchronized BaseResultEntity getFaceMetaData() {
        if (mDetectionResultEntity != null) {
            return mDetectionResultEntity;
        }
        DetectionFrame bestDetectionFrame = getBestDetectionFrame();
        BaseResultEntity resultEntity = new BaseResultEntity();
        if (bestDetectionFrame == null) {
            resultEntity.code = "NO_BEST_IMAGE";
            resultEntity.message = "not get best image(sdk message)";
        } else {
            resultEntity.success = true;
            mDetectionResultEntity = resultEntity;
            LivenessResult.setLivenessResult(bestDetectionFrame.getFormatBitmap(), resultEntity);
        }
        if (!resultEntity.success) {
            LivenessResult.setErrorCode("CHECKING_" + resultEntity.code);
        }
        return resultEntity;
    }

    /**
     * Cache camera's image into queue
     *
     * @param yuvData     yuv data from camera
     * @param previewSize preview size of camera
     */
    public synchronized boolean doDetection(byte[] yuvData, Camera.Size previewSize) {
        if (mDetectionFrameBlockingQueue == null) {
            return false;
        } else {
            try {
                DetectionFrame detectionFrame = new DetectionFrame(yuvData, cameraAngle, previewSize.width, previewSize.height, mDetectionType);
                return this.mDetectionFrameBlockingQueue.offer(detectionFrame);
            } catch (Exception error) {

                LogUtil.debug("do_detection", error.toString());
                return false;
            }
        }
    }

    public enum DetectionFailedType {
        //        ACTIONBLEND,
//        NOTVIDEO,
        TIMEOUT,
        //        MASK,
//        FACENOTCONTINUOUS,
//        TOOMANYFACELOST,
//        NOFACE,
        WEAKLIGHT,
        STRONGLIGHT,
        FACEMISSING,
        MULTIPLEFACE,
        MUCHMOTION,
        UNAUTHORIZED,
        UNSUPPORT_DEVICE;

        DetectionFailedType() {
        }
    }

    public enum ActionStatus {
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

        public boolean isFaceNotReady() {
            return this.ordinal() < Detector.ActionStatus.FACEMOTIONREADY.ordinal();
        }

        public static ActionStatus valueOf(int value) {
            switch (value) {
                case 1:
                    return NOFACE;
                case 2:
                    return FACECHECKSIZE;
                case 3:
                    return FACESIZEREADY;
                case 4:
                    return FACECENTERREADY;
                case 5:
                    return FACEFRONTALREADY;
                case 6:
                    return FACECAPTUREREADY;
                case 7:
                    return FACEMOTIONREADY;
                case 8:
                    return FACEBLINK;
                case 9:
                    return FACEMOUTH;
                case 10:
                    return FACEYAW;
                default:
                    return FACENODEFINE;
            }
        }
    }


    public enum WarnCode {
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

        public static WarnCode valueOf(int value) {
            switch (value) {
                case 1:
                    return FACEMISSING;
                case 2:
                    return FACELARGE;
                case 3:
                    return FACESMALL;
                case 4:
                    return FACENOTCENTER;
                case 5:
                    return FACENOTFRONTAL;
                case 6:
                    return FACENOTSTILL;
                case 7:
                    return WARN_MULTIPLEFACES;
                case 8:
                    return WARN_EYE_OCCLUSION;
                case 9:
                    return WARN_MOUTH_OCCLUSION;
                case 10:
                    return FACECAPTURE;
                case 11:
                    return FACEINACTION;
                case 12:
                    return OK_ACTIONDONE;
                case 13:
                    return ERROR_MULTIPLEFACES;
                case 14:
                    return ERROR_FACEMISSING;
                case 15:
                    return ERROR_MUCHMOTION;
                case 16:
                    return OK_COUNTING;
                case 17:
                    return WARN_MOTION;
                case 18:
                    return WARN_LARGE_YAW;
                default:
                    return OK_DEFAULT;
            }
        }
    }


    public enum DetectionType {
        NONE(0),
        BLINK(1),
        MOUTH(2),
        POS_YAW(3),
        DONE(6),
//        POS_PITCH(4),
//        POS_YAW_LEFT(7),
//        POS_YAW_RIGHT(8),

        //        POS_PITCH_UP(9),
//        POS_PITCH_DOWN(10),
        AIMLESS(5);


        private int mInterValue;

        DetectionType(int value) {
            this.mInterValue = value;
        }
    }

    /**
     * detection logic callback
     */
    public interface DetectionListener {
        /**
         * current action detection success
         *
         * @param validFrame last frame of current action
         * @return Need to return the next detectionType to the detector, if it is DONE it means the end
         */
        DetectionType onDetectionSuccess(DetectionFrame validFrame);

        /**
         * action detection failed
         *
         * @param failedType failed type
         */
        void onDetectionFailed(DetectionFailedType failedType);

        /**
         * Callback of time remaining in the current action
         *
         * @param timeout remaining time (ms)
         */
        void onDetectionTimeout(long timeout);

        /**
         * Callback of the result after each frame recognition is completed
         *
         * @param detectionFrame Information of the identified frame
         */
        void onFrameDetected(DetectionFrame detectionFrame);

        /**
         * The preparation phase is completed and the action phase is about to be entered
         */
        void onFaceReady();

    }

    public interface DetectorInitCallback {
        void onDetectorInitStart();

        void onDetectorInitComplete(boolean isValid, String errorCode, String message);

    }

}