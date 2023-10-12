package com.tokopedia.kyc_centralized

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraActivity
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.utils.image.ImageProcessingUtil

fun stubLiveness(projectId: String = "-1", result: Int = Activity.RESULT_OK) {
    val ctx = ApplicationProvider.getApplicationContext<Context>()
    intending(hasData(ApplinkConstInternalUserPlatform.KYC_LIVENESS.replace("{projectId}", projectId)))
        .respondWithFunction {
            if (result == Activity.RESULT_OK) {
                val cameraResultFile = ImageProcessingUtil.getTokopediaPhotoPath(
                    Bitmap.CompressFormat.JPEG,
                    UserIdentificationFormActivity.FILE_NAME_KYC
                )
                val sampleJpeg = ctx.assets.open("sample.jpeg")
                sampleJpeg.copyTo(cameraResultFile.outputStream())
                Instrumentation.ActivityResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(
                        ApplinkConstInternalGlobal.PARAM_FACE_PATH,
                        cameraResultFile.absolutePath
                    )
                })
            } else {
                Instrumentation.ActivityResult(result, Intent())
            }
    }
}

fun stubLivenessFailed(projectId: String = "-1") {
    val ctx = ApplicationProvider.getApplicationContext<Context>()
    intending(hasData(ApplinkConstInternalUserPlatform.KYC_LIVENESS.replace("{projectId}", projectId)))
        .respondWithFunction {
            val cameraResultFile = ImageProcessingUtil.getTokopediaPhotoPath(
                Bitmap.CompressFormat.JPEG,
                UserIdentificationFormActivity.FILE_NAME_KYC
            )
            val sampleJpeg = ctx.assets.open("sample.jpeg")
            sampleJpeg.copyTo(cameraResultFile.outputStream())
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        }
}

fun stubKtpCamera() {
    val ctx = ApplicationProvider.getApplicationContext<Context>()
    intending(hasComponent(UserIdentificationCameraActivity::class.java.name))
        .respondWithFunction {
        val cameraResultFile = ImageProcessingUtil.getTokopediaPhotoPath(
            Bitmap.CompressFormat.JPEG,
            UserIdentificationFormActivity.FILE_NAME_KYC
        )
        val sampleJpeg = ctx.assets.open("sample.jpeg")
        sampleJpeg.copyTo(cameraResultFile.outputStream())
        Instrumentation.ActivityResult(Activity.RESULT_OK, Intent().apply {
            putExtra(
                KYCConstant.EXTRA_STRING_IMAGE_RESULT,
                cameraResultFile.absolutePath
            )
        })
    }
}
