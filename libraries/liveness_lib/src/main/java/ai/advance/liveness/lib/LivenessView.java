package ai.advance.liveness.lib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.Toast;

import ai.advance.liveness.lib.impl.LivenessCallback;
import ai.advance.liveness.lib.impl.LivenessGetFaceDataCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import ai.advance.common.IMediaPlayer;
import ai.advance.common.camera.GuardianCameraView;
import ai.advance.common.entity.BaseResultEntity;
import ai.advance.common.utils.JsonUtils;
import ai.advance.common.utils.LogUtil;
import ai.advance.common.utils.SensorUtil;

/**
 * camera logic view
 * createTime:2019/4/24
 *
 * @author fan.zhang@advance.ai
 */
public class LivenessView extends GuardianCameraView implements GuardianCameraView.CallBack, Detector.DetectionListener, Detector.DetectorInitCallback {
    public static final String NO_RESPONSE = JsonUtils.NO_RESPONSE;
    private IMediaPlayer mMediaPlayer;
    /**
     * sensor
     */
    private SensorUtil mSensor;
    /**
     * whether SDK  detected
     */
    private boolean mSDKDetected;
    /**
     * detection logic
     */
    private Detector mDetector;
    /**
     * context
     */
    private Context mContext;
    /**
     * index of current step
     */
    private int mCurStep;
    /**
     * current action
     */
    private Detector.DetectionType mCurrentDetectionType;
    /**
     * callback
     */
    private LivenessCallback mLivenessCallback;
    /**
     * main thread handler
     */
    private Handler mHandler;
    /**
     * detection action list
     */
    ArrayList<Detector.DetectionType> mDetectionSteps;

    public LivenessView(Context context) {
        this(context, null);
    }

    public LivenessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean checkDetectionTypes(Detector.DetectionType... detectionTypes) {
        for (int i = 0; i < detectionTypes.length; i++) {
            Detector.DetectionType detectionType = detectionTypes[i];
            switch (detectionType) {
                case DONE:
                case AIMLESS:
                case NONE:
                    return false;
            }
        }
        return true;
    }

    private void onDetectionTypeNotSupported(String errorCode, String errorMsg) {
        if (mLivenessCallback != null) {
            mLivenessCallback.onDetectorInitComplete(false, errorCode, errorMsg);
        } else {
            LogUtil.sdkLogE(errorMsg);
        }

    }


    /**
     * start detection by custom action steps
     *
     * @param shuffle whether shuffle actions
     */
    public synchronized void startDetection(LivenessCallback callback, boolean shuffle, Detector.DetectionType... detectionTypes) {
        LivenessResult.clearCache();
        mLivenessCallback = callback;
        if (detectionTypes.length == 0) {
            String errorCode = "EMPTY_DETECTION_TYPE_LIST";
            String errorMsg = "Detection Types need at least one term";
            onDetectionTypeNotSupported(errorCode, errorMsg);
        } else {
            boolean detectionTypeEnable = checkDetectionTypes(detectionTypes);
            if (detectionTypeEnable) {
                mHandler = new Handler(Looper.getMainLooper());
                mDetectionSteps = new ArrayList<>(Arrays.asList(detectionTypes));
                if (shuffle) {
                    Collections.shuffle(mDetectionSteps);
                }
                initData();
                if (mDetector.cameraAngle == -1) {
                    LivenessResult.setErrorCode(ErrorCode.DEVICE_NOT_SUPPORT);
                    callback.onDetectorInitComplete(false, ErrorCode.DEVICE_NOT_SUPPORT.toString(), "camera error");
                } else {
                    if (GuardianLivenessDetectionSDK.isEmulator) {
                        openBackCamera(this);
                    } else {
                        openFrontCamera(this);
                    }
                }
            } else {
                String errorCode = "NOT_SUPPORTED_DETECTION_TYPE";
                String errorMsg = "Type of detection not supported\n detectionType must in (POS_PAW,BLINK,MOUTH)";
                onDetectionTypeNotSupported(errorCode, errorMsg);
            }
        }


    }

    float getRatio(Camera.Size size) {
        return (float) this.getPreviewWidth(size) / (float) this.getPreviewHeight(size);
    }

    int getPreviewWidth(Camera.Size size) {
        return this.isPortrait() ? size.height : size.width;
    }

    int getPreviewHeight(Camera.Size size) {
        return this.isPortrait() ? size.width : size.height;
    }

    @Override
    protected void transformTexture() {
        if (GuardianLivenessDetectionSDK.isEmulator) {
            if (this.mPreviewSize != null) {
                float viewWidth = (float) this.getViewWidth();
                float viewHeight = (float) this.getViewHeight();
                float ratio = getRatio(this.mPreviewSize);
                RectF toRect = new RectF(0.0F, 0.0F, viewHeight, viewWidth);
                RectF fromRect = new RectF(0f, 0.0F, viewWidth, viewWidth * ratio);
                this.mCameraTransformWidthRatio = fromRect.width() / toRect.width();
                this.mCameraTransformHeightRatio = fromRect.height() / toRect.height();
                Matrix matrix = new Matrix();
                matrix.setRectToRect(fromRect, toRect, Matrix.ScaleToFit.START);
                this.setTransform(matrix);
            }
        } else {
            super.transformTexture();
        }
    }

    @Override
    public synchronized void startPreview(GuardianCameraView textureView) {
        try {
            super.startPreview(textureView);
        } catch (Exception e) {

            LogUtil.sdkLogE("[" + mCameraId + "] startPreview exception：" + e.getMessage());
        }
    }

    @Override
    public void restartCamera(int cameraId) {
        try {
            super.restartCamera(cameraId);
        } catch (Exception e) {
            LogUtil.sdkLogE("[" + cameraId + "] restartCamera exception:" + e.getMessage());
        }
    }

    @Override
    protected synchronized void open(int cameraId) {
        try {
            if (GuardianLivenessDetectionSDK.isEmulator) {
                if (!this.mOnCameraOpening) {
                    try {
                        this.mOnCameraOpening = true;
                        this.mCamera = Camera.open(cameraId);
                        Camera.Parameters params = this.mCamera.getParameters();
                        this.mPreviewSize = this.calBestPreviewSize(this.mCamera.getParameters());
                        params.setPreviewSize(this.mPreviewSize.width, this.mPreviewSize.height);
                        this.mCameraAngle = this.getCameraAngle(cameraId);
                        this.mCamera.setDisplayOrientation(0);
                        this.mCamera.setParameters(params);
                        this.transformTexture();
                        this.startAutoFocus();
                    } catch (Exception var4) {
                    }

                    if (this.mCamera == null && this.mCallBack != null) {
                        this.mCallBack.onCameraOpenFailed();
                    }

                    this.mOnCameraOpening = false;
                }
            } else {
                super.open(cameraId);
            }
        } catch (Exception e) {
            LivenessResult.setErrorCode(ErrorCode.DEVICE_NOT_SUPPORT);
            LogUtil.sdkLogE("[" + cameraId + "] open camera exception:" + e.getMessage());
        }

    }

    private void onOpenCameraFailed() {
        LivenessResult.setErrorCode(ErrorCode.DEVICE_NOT_SUPPORT);
        if (callBackEnable()) {
            mLivenessCallback.onDetectorInitComplete(false, ErrorCode.DEVICE_NOT_SUPPORT.toString(), "The device does not support liveness detection");
        }
    }

    @Override
    public void openFrontCamera(CallBack callBack) {
        if (GuardianLivenessDetectionSDK.isDeviceSupportLiveness()) {
            try {
                super.openFrontCamera(callBack);
            } catch (Exception e) {
                LogUtil.sdkLogE(e.getMessage());
                onOpenCameraFailed();
            }
        } else {
            onOpenCameraFailed();
        }
    }

    /**
     * start detection by default action steps
     *
     * @param callback callback
     */
    public synchronized void startDetection(LivenessCallback callback) {
        if (new Random().nextBoolean()) {
            startDetection(callback, false, Detector.DetectionType.MOUTH, Detector.DetectionType.BLINK, Detector.DetectionType.POS_YAW);
        } else {
            startDetection(callback, false, Detector.DetectionType.BLINK, Detector.DetectionType.MOUTH, Detector.DetectionType.POS_YAW);
        }
    }

    private void initData() {
        mContext = getContext();

        mMediaPlayer = new IMediaPlayer(mContext);
        mSensor = new SensorUtil(mContext);

        mDetector = new Detector((Activity) mContext);
        mDetector.setDetectionListener(this);
    }

    /**
     * get current action
     */
    public Detector.DetectionType getCurrentDetectionType() {
        return mCurrentDetectionType;
    }

    /**
     * stop detection and close camera
     */
    public synchronized void stopDetection() {
        closeMediaPlayer();
        closeCamera();
    }

    /**
     * destroy view and cycle memory
     */
    public synchronized void destroy() {
        mLivenessCallback = null;
        stopDetection();
        if (mDetector != null) {
            mDetector.setDetectionListener(null);
            mDetector.release();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mSensor != null) {
            mSensor.release();
        }
        if (mDetectionSteps != null) {
            mDetectionSteps.clear();
        }

    }


    /**
     * toggle sound play
     *
     * @param playEnable true is playable
     */
    public void setSoundPlayEnable(boolean playEnable) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setPlayEnable(playEnable);
        }
    }

    /**
     * play sound
     *
     * @param rawId          raw resource id
     * @param repeat         whether repeat
     * @param repeatInterval repeat interval
     */
    public void playSound(int rawId, boolean repeat, long repeatInterval) {
        if (mMediaPlayer != null) {
            mMediaPlayer.doPlay(rawId, repeat, repeatInterval);
        }
    }

    /**
     * whether mobile is vertical(unused)
     */
    public boolean isVertical() {
//        if (mSensor == null) {
//            return true;
//        } else {
//            return mSensor.isVertical();
//        }
        return true;
    }

    /**
     * disable back camera when device type is real phone
     */
    @Deprecated
    @Override
    public void openBackCamera() {
        if (GuardianLivenessDetectionSDK.isEmulator) {
            super.openBackCamera();
        }
    }

    /**
     * disable back camera when device type is real phone
     */
    @Deprecated
    @Override
    public void openBackCamera(CallBack callback) {
        if (GuardianLivenessDetectionSDK.isEmulator) {
            super.openBackCamera(callback);
        }
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        if (isVertical()) {
            if (!mSDKDetected) {
                mSDKDetected = true;
                mCurStep = 0;
                mCurrentDetectionType = mDetectionSteps.get(mCurStep);
                mDetector.init(mCurrentDetectionType, LivenessView.this);
            }
        }

    }

    @Override
    public void onCameraOpenFailed() {

    }

    @Override
    public void onGetYuvData(byte[] bytes, Camera.Size size) {
        mDetector.doDetection(bytes, size);
    }

    @Override
    public Detector.DetectionType onDetectionSuccess(DetectionFrame validFrame) {
        Detector.DetectionType nextStep = Detector.DetectionType.DONE;
        try {
            if (mContext != null) {
                mCurStep++;
                if (mCurStep >= mDetectionSteps.size()) {
                    if (callBackEnable()) {
                        mLivenessCallback.onDetectionSuccess();
                    }
                } else {
                    if (callBackEnable()) {
                        nextStep = mDetectionSteps.get(mCurStep);
                        mCurrentDetectionType = nextStep;
                        mLivenessCallback.onDetectionActionChanged();
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.sdkLogE("an error occur :" + e.getMessage());
        }
        return nextStep;
    }

    /**
     * check callback status
     */
    private boolean callBackEnable() {
        return mHandler != null && mLivenessCallback != null;
    }

    /**
     * stop voice play
     */
    private void closeMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.close();
        }
    }

    /**
     * get liveness detection data
     */
    public void getLivenessData(final LivenessGetFaceDataCallback callback) {
        stopDetection();
        if (callBackEnable()) {
            if (callback != null) {
                callback.onGetFaceDataStart();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final BaseResultEntity resultEntity = mDetector.getFaceMetaData();
                    if (callBackEnable() && callback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (resultEntity.success) {
                                    callback.onGetFaceDataSuccess(resultEntity);
                                } else {
                                    callback.onGetFaceDataFailed(resultEntity);
                                }
                            }
                        });

                    }
                }
            }).start();
        }
    }


    @Override
    public void onDetectionFailed(Detector.DetectionFailedType failedType) {

        if (callBackEnable()) {
            mLivenessCallback.onDetectionFailed(failedType, mCurrentDetectionType);
        }
        mLivenessCallback = null;
        if (mDetector != null) {
            mDetector.setDetectionListener(null);
        }
    }

    @Override
    public void onDetectionTimeout(final long timeout) {
        if (callBackEnable()) {
            mLivenessCallback.onActionRemainingTimeChanged(timeout);
        }
    }

    /**
     * latest warnCode for UI notify
     */
    private Detector.WarnCode mLatestWarnCode;

    @Override
    public void onFrameDetected(DetectionFrame detectionFrame) {
        if (callBackEnable()) {
            Detector.WarnCode currentFrameWarnCode = detectionFrame.mFaceWarnCode;
            if (currentFrameWarnCode != mLatestWarnCode) {
                mLatestWarnCode = currentFrameWarnCode;
                mLivenessCallback.onDetectionFrameStateChanged(mLatestWarnCode);
            }
        }
    }

    @Override
    public void onFaceReady() {
        if (callBackEnable()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mLivenessCallback.onDetectionActionChanged();
                }
            });
        }
    }

    @Override
    public void onDetectorInitStart() {
        if (callBackEnable()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mLivenessCallback.onDetectorInitStart();
                }
            });

        }
    }

    @Override
    public void onDetectorInitComplete(final boolean isValid, final String errorCode, final String message) {
        if (callBackEnable()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (Detector.isTestAccount) {
                        Toast.makeText(getContext(), "This is a test account for development and debugging only!", Toast.LENGTH_LONG).show();
                    }
                    mLivenessCallback.onDetectorInitComplete(isValid, errorCode, message);
                }
            });
        }
    }
}
