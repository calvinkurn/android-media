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
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.utils.image.ImageProcessingUtil

fun stubLiveness() {
    val ctx = ApplicationProvider.getApplicationContext<Context>()
    intending(hasData(ApplinkConstInternalUserPlatform.KYC_LIVENESS.replace("{projectId}", "-1")))
        .respondWithFunction {
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
