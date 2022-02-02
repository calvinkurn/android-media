package com.tokopedia.kyc_centralized.view.activity

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import com.tokopedia.kyc_centralized.di.*
import com.tokopedia.kyc_centralized.kycRobot
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.kyc_centralized.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.utils.image.ImageProcessingUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@UiTest
@RunWith(AndroidJUnit4::class)
class UserIdentificationInfoActivityTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UserIdentificationInfoActivity::class.java, false, false
    )

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA,
    )

    private val ctx = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        // no op
    }


    @Test
    fun happyFlowTest() {
        ActivityComponentFactory.instance = FakeKycActivityComponentFactory()
        activityTestRule.launchActivity(null)
        val cameraResultFile = ImageProcessingUtil.getTokopediaPhotoPath(
            Bitmap.CompressFormat.JPEG,
            UserIdentificationFormActivity.FILE_NAME_KYC
        )
        val sampleJpeg = ctx.assets.open("sample.jpeg")
        sampleJpeg.copyTo(cameraResultFile.outputStream())
        intending(hasData(ApplinkConstInternalGlobal.LIVENESS_DETECTION)).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, Intent().apply {
                putExtra(
                    ApplinkConstInternalGlobal.PARAM_FACE_PATH,
                    cameraResultFile.absolutePath
                )
            })
        )

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            atCameraClickCapture()
            atCameraClickNext()
            atFaceIntroClickNext()
        }

        Thread.sleep(3_000)
    }

    @Test
    fun retakeKtpTest() {
        ActivityComponentFactory.instance = FakeKycActivityComponentFactory(
            case = FakeKycUploadApi.Case.Retake(
                arrayListOf(2)
            )
        )
        activityTestRule.launchActivity(null)
        val cameraResultFile = ImageProcessingUtil.getTokopediaPhotoPath(
            Bitmap.CompressFormat.JPEG,
            UserIdentificationFormActivity.FILE_NAME_KYC
        )
        val sampleJpeg = ctx.assets.open("sample.jpeg")
        sampleJpeg.copyTo(cameraResultFile.outputStream())
        intending(hasData(ApplinkConstInternalGlobal.LIVENESS_DETECTION)).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, Intent().apply {
                putExtra(
                    ApplinkConstInternalGlobal.PARAM_FACE_PATH,
                    cameraResultFile.absolutePath
                )
            })
        )

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            atCameraClickCapture()
            atCameraClickNext()
            atFaceIntroClickNext()
            atFinalPressCta()

            atCameraClickCapture()
            atCameraClickNext()
        }

        Thread.sleep(5_000)
    }
}