package com.tokopedia.updateinactivephone.revamp.common

import android.content.Context
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import java.io.File

object InactivePhoneConstant {

    const val TAG = "InactivePhone"
    const val ID_CARD = "IdCard"
    @Deprecated("Deleted soon")
    const val SELFIE = "Selfie"
    const val SAVING_BOOk = "SavingBook"

    const val REQUEST_CAPTURE_ID_CARD = 100
    @Deprecated("Deleted soon")
    const val REQUEST_CAPTURE_SELFIE = 200
    const val REQUEST_CAPTURE_SAVING_BOOK = 300

    fun filePath(context: Context, fileType: Int): String {
        return when(fileType) {
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