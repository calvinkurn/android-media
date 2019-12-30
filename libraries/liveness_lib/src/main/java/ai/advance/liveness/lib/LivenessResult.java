package ai.advance.liveness.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import ai.advance.common.entity.BaseResultEntity;

/**
 * createTime:2019/2/1 10:08 AM
 *
 * @author fan.zhang@advance.ai
 */
public class LivenessResult {
    private static String mBase64Bitmap;
    private static BaseResultEntity mEntity;
    /**
     * error code
     */
    private static String mErrorCode;
    /**
     * failed detection type
     */
    private static Detector.DetectionType mErrorDetectionType;

    public static Detector.DetectionType getErrorDetectionType() {
        return mErrorDetectionType;
    }

    static void setErrorDetectionType(Detector.DetectionType detectionType) {
        mErrorDetectionType = detectionType;
    }

    /**
     * whether success
     */
    public static boolean isSuccess() {
        if (mEntity == null) {
            return false;
        } else {
            return mEntity.success;
        }
    }

    public static String getErrorCode() {
        if (isSuccess()) {
            return null;
        }
        if (mErrorCode != null) {
            return mErrorCode;
        }
        if (mEntity != null && !TextUtils.isEmpty(mEntity.code)) {
            return mEntity.code;
        } else {
            return ErrorCode.UNDEFINED.toString();
        }
    }

    static void setErrorCode(String errorCode) {
        mErrorCode = errorCode;
    }

    static void setErrorCode(ErrorCode errorCode) {
        mErrorCode = errorCode.name();
    }

    static void setErrorCode(Detector.DetectionFailedType failedType) {
        if (failedType != null) {
            switch (failedType) {
                case TIMEOUT:
                    setErrorCode(ErrorCode.ACTION_TIMEOUT);
                    break;
                case MUCHMOTION:
                    setErrorCode(ErrorCode.MUCH_MOTION);
                    break;
                case FACEMISSING:
                    setErrorCode(ErrorCode.FACE_MISSING);
                    break;
                case MULTIPLEFACE:
                    setErrorCode(ErrorCode.MULTIPLE_FACE);
                    break;
            }
        }
    }

    /**
     * set error message
     */
    public static void setErrorMsg(String errorMsg) {
        if (mEntity == null) {
            mEntity = new BaseResultEntity();
        }
        if (!mEntity.success) {
            mEntity.message = errorMsg;
        }
    }

    /**
     * get error message
     */
    public static String getErrorMsg() {
        if (mEntity == null) {
            return null;
        } else {
            return mEntity.message;
        }
    }

    static void setLivenessResult(String base64Bitmap, BaseResultEntity entity) {
        mBase64Bitmap = base64Bitmap;
        mEntity = entity;
    }

    /**
     * get liveness bitmap(only success)
     *
     * @return bitmap
     */
    public static Bitmap getLivenessBitmap() {
        if (mBase64Bitmap != null) {
            byte[] decode = Base64.decode(mBase64Bitmap, Base64.NO_WRAP);
            //convert base64 string to bitmap
            return BitmapFactory.decodeByteArray(decode, 0, decode.length);
        } else {
            return null;
        }
    }

    /**
     * get Base64 format picture
     *
     * @return base64 string
     */
    public static String getLivenessBase64Str() {
        return mBase64Bitmap;
    }

    public static void clearCache() {
        mErrorCode = null;
        mErrorDetectionType = null;
        mBase64Bitmap = null;
        mEntity = null;
    }
}
