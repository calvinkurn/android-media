package ai.advance.liveness.lib

import ai.advance.common.entity.BaseResultEntity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64

object LivenessResult {

    var livenessBase64Str: String? = null
    private var mEntity: BaseResultEntity? = null

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

    var errorDetectionType: Detector.DetectionType? = null

    private val isSuccess: Boolean
        get() = if (mEntity == null) {
            false
        } else {
            mEntity?.success == true
        }

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
            if (mEntity?.success != true) {
                mEntity?.message = errorMsg
            }
        }

    //convert base64 string to bitmap
    val livenessBitmap: Bitmap?
        get() {
            return if (livenessBase64Str != null) {
                val decode = Base64.decode(livenessBase64Str, Base64.NO_WRAP)
                BitmapFactory.decodeByteArray(decode, 0, decode.size)
            } else {
                null
            }
        }

    fun setErrorCode(errorCode: ErrorCode) {
        this.errorCode = errorCode.name
    }

    fun setErrorCode(failedType: Detector.DetectionFailedType?) {
        if (failedType != null) {
            when (failedType) {
                Detector.DetectionFailedType.TIMEOUT -> setErrorCode(ErrorCode.ACTION_TIMEOUT)
                Detector.DetectionFailedType.MUCHMOTION -> setErrorCode(ErrorCode.MUCH_MOTION)
                Detector.DetectionFailedType.FACEMISSING -> setErrorCode(ErrorCode.FACE_MISSING)
                Detector.DetectionFailedType.MULTIPLEFACE -> setErrorCode(ErrorCode.MULTIPLE_FACE)
                else -> {}
            }
        }
    }

    fun setLivenessResult(base64Bitmap: String, entity: BaseResultEntity) {
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
