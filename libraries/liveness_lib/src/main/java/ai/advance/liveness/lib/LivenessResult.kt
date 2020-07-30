package ai.advance.liveness.lib

import ai.advance.common.entity.BaseResultEntity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64

object LivenessResult {
    /**
     * get Base64 format picture
     *
     * @return base64 string
     */
    var livenessBase64Str: String? = null

    private var mEntity: BaseResultEntity? = null
    /**
     * error code
     */
    var errorCode: String? = null
        get() {
            if (isSuccess) {
                return null
            }
            if (field != null) {
                return field
            }
            return if (mEntity != null && !TextUtils.isEmpty(mEntity?.code)) {
                mEntity?.code
            } else {
                ErrorCode.UNDEFINED.toString()
            }
        }
    /**
     * failed detection type
     */
    var errorDetectionType: Detector.DetectionType? = null

    /**
     * whether success
     */
    val isSuccess: Boolean
        get() = if (mEntity == null) {
            false
        } else {
            mEntity?.success?: false
        }

    /**
     * get error message
     */
    /**
     * set error message
     */
    var errorMsg: String?
        get() = if (mEntity == null) {
            null
        } else {
            mEntity?.message
        }
        set(errorMsg) {
            if (mEntity == null) {
                mEntity = BaseResultEntity()
            }
            if (mEntity?.success == false) {
                mEntity?.message = errorMsg
            }
        }

    /**
     * get liveness bitmap(only success)
     *
     * @return bitmap
     */
    //convert base64 string to bitmap
    val livenessBitmap: Bitmap?
        get() {
            if (livenessBase64Str != null) {
                val decode = Base64.decode(livenessBase64Str, Base64.NO_WRAP)
                return BitmapFactory.decodeByteArray(decode, 0, decode.size)
            } else {
                return null
            }
        }

    internal fun setErrorCode(errorCode: ErrorCode) {
        LivenessResult.errorCode = errorCode.name
    }

    internal fun setErrorCode(failedType: Detector.DetectionFailedType?) {
        if (failedType != null) {
            when (failedType) {
                Detector.DetectionFailedType.TIMEOUT -> setErrorCode(ErrorCode.ACTION_TIMEOUT)
                Detector.DetectionFailedType.MUCHMOTION -> setErrorCode(ErrorCode.MUCH_MOTION)
                Detector.DetectionFailedType.FACEMISSING -> setErrorCode(ErrorCode.FACE_MISSING)
                Detector.DetectionFailedType.MULTIPLEFACE -> setErrorCode(ErrorCode.MULTIPLE_FACE)
            }
        }
    }

    internal fun setLivenessResult(base64Bitmap: String, entity: BaseResultEntity) {
        livenessBase64Str = base64Bitmap
        mEntity = entity
    }

    fun clearCache() {
        errorCode = null
        errorDetectionType = null
        livenessBase64Str = null
        mEntity = null
    }
}
