package com.tokopedia.updateinactivephone.common

import android.content.Context
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import java.io.File

object InactivePhoneConstant {

    const val TAG = "InactivePhone"
    const val ID_CARD = "IdCard"
    const val SELFIE = "Selfie"

    const val REQUEST_CAPTURE_ID_CARD = 100
    const val REQUEST_CAPTURE_SELFIE = 200

    const val PARAM_USER_DETAIL_DATA = "userData"
    const val PARAM_FILE_TO_UPLOAD = "fileToUpload"
    const val PARAM_EMAIL = "email"
    const val PARAM_OLD_PHONE = "oldMsisdn"
    const val PARAM_USER_INDEX = "index"
    const val PARAM_PHONE = "phone"

    const val ERROR_FAILED_UPLOAD_IMAGE = "Gagal mengunggah gambar"
    const val ERROR_INVALID_PHONE_NUMBER = "Nomor ponsel harus berupa angka"
    const val ERROR_PHONE_NUMBER_MIN = "Nomor ponsel terlalu pendek, minimum 9 angka"
    const val ERROR_PHONE_NUMBER_MAX = "Nomor ponsel terlalu panjang, maksimum 15 angka"

    const val STATUS_FAIL = 0
    const val STATUS_SUCCESS = 1
    const val STATUS_MULTIPLE_ACCOUNT = 2

    fun filePath(context: Context, fileType: Int): String {
        return when (fileType) {
            CameraViewMode.ID_CARD.id -> {
                File(context.externalCacheDir, "$TAG-$ID_CARD.jpg").absolutePath
            }
            CameraViewMode.SELFIE.id -> {
                File(context.externalCacheDir, "$TAG-$SELFIE.jpg").absolutePath
            }
            else -> ""
        }
    }
}