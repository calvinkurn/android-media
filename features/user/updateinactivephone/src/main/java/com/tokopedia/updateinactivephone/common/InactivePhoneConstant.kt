package com.tokopedia.updateinactivephone.common

import android.content.Context
import android.content.ContextWrapper
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import java.io.File

object InactivePhoneConstant {
    const val KEY_SOURCE = "source"
    const val SOURCE_INACTIVE_PHONE = "inactivePhone"

    const val TAG = "InactivePhone"
    const val ID_CARD = "IdCard"
    const val SELFIE = "Selfie"
    const val REGULAR = "Regular"
    const val EXPEDITED = "Expedited"

    const val REQUEST_CAPTURE_ID_CARD = 100
    const val REQUEST_CAPTURE_SELFIE = 200

    const val SQCP_OTP_TYPE = 169
    const val REQUEST_SQCP_OTP_VERIFICATION = 101

    const val PARAM_USER_DETAIL_DATA = "userData"
    const val PARAM_USER_DATA = "userDataModel"
    const val PARAM_FILE_TO_UPLOAD = "fileToUpload"
    const val PARAM_EMAIL = "email"
    const val PARAM_OLD_PHONE = "oldMsisdn"
    const val PARAM_USER_INDEX = "index"
    const val PARAM_PHONE = "phone"

    const val ERROR_FAILED_UPLOAD_IMAGE = "Gagal mengunggah gambar. Silahkan coba lagi"
    const val ERROR_PHONE_NUMBER_EMPTY = "Nomor ponsel tidak ditemukan atau kosong"
    const val ERROR_INVALID_PHONE_NUMBER = "Nomor ponsel harus berupa angka"
    const val ERROR_PHONE_NUMBER_MIN = "Nomor ponsel terlalu pendek, minimum 9 angka"
    const val ERROR_PHONE_NUMBER_MAX = "Nomor ponsel terlalu panjang, maksimum 15 angka"
    const val ERROR_UNKNOWN = "Terjadi kesalahan, silahkan coba kembali"

    const val STATUS_FAIL = 0
    const val STATUS_SUCCESS = 1
    const val STATUS_MULTIPLE_ACCOUNT = 2

    const val OTP_TYPE_INACTIVE_PHONE_EMAIL = 160
    const val OTP_TYPE_INACTIVE_PHONE_PIN = 161
    const val OTP_TYPE_INACTIVE_PHONE_SMS = 162

    const val MINIMUM_PHONE_NUMBER = 9

    const val IS_USE_REGULAR_FLOW = "isUseRegularFlow"

    fun filePath(context: Context, fileType: Int): String {
        return when (fileType) {
            CameraViewMode.ID_CARD.id -> {
                File(getInternalDirectory(context), "$TAG-$ID_CARD.jpg").absolutePath
            }
            CameraViewMode.SELFIE.id -> {
                File(getInternalDirectory(context), "$TAG-$SELFIE.jpg").absolutePath
            }
            else -> ""
        }
    }

    fun getInternalDirectory(context: Context): String {
        val contextWrapper = ContextWrapper(context.applicationContext)
        val dir = File(contextWrapper.cacheDir, TAG)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        return dir.absolutePath
    }
}