package com.tokopedia.updateinactivephone.revamp.common

import android.content.Context
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import java.io.File

object InactivePhoneConstant {

    const val TAG = "InactivePhone"
    const val ID_CARD = "IdCard"
    const val SELFIE = "Selfie"

    const val REQUEST_CAPTURE_ID_CARD = 100
    const val REQUEST_CAPTURE_SELFIE = 200

    @Deprecated("Deleted soon")
    const val SAVING_BOOk = "SavingBook"

    @Deprecated("Deleted soon")
    const val REQUEST_CAPTURE_SAVING_BOOK = 300

    const val PARAM_USER_DETAIL_DATA = "userData"
    const val PARAM_URL_UPLOAD_IMAGE = "image_upload_url"
    const val PARAM_FILE_TO_UPLOAD = "fileToUpload"
    const val PARAM_USER_ID = "user_id"
    const val PARAM_ID = "id"
    const val PARAM_PHONE = "phone"
    const val PARAM_EMAIL = "email"
    const val PARAM_TOKEN = "token"
    const val PARAM_NEW_ADD = "new_add"
    const val PARAM_SERVER_ID = "server_id"
    const val PARAM_RESOLUTION = "resolution"
    const val PARAM_PATH_ID_CARD = "ktp_image_path"
    const val PARAM_PATH_SELFIE = "account_image_path"

    const val ERROR_FAILED_UPLOAD_IMAGE = "Gagal mengunggah gambar"
    const val ERROR_VALIDATE_PHONE_NUMBER = "Gagal verifikasi nomor ponsel"
    const val ERROR_EMPTY_PHONE = "Nomor ponsel harus diisi"
    const val ERROR_INVALID_PHONE_NUMBER = "Nomor ponsel harus berupa angka"
    const val ERROR_PHONE_NUMBER_MIN = "Nomor ponsel terlalu pendek, minimum 9 angka"
    const val ERROR_PHONE_NUMBER_MAX = "Nomor ponsel terlalu panjang, maksimum 15 angka"

    fun filePath(context: Context, fileType: Int): String {
        return when (fileType) {
            CameraViewMode.ID_CARD.id -> {
                File(context.externalCacheDir, "$TAG-$ID_CARD.jpg").absolutePath
            }
            CameraViewMode.SELFIE.id -> {
                File(context.externalCacheDir, "$TAG-$SELFIE.jpg").absolutePath
            }
            CameraViewMode.SAVING_BOOK.id -> {
                File(context.externalCacheDir, "$TAG-$SAVING_BOOk.jpg").absolutePath
            }
            else -> ""
        }
    }
}