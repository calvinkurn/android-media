package com.tokopedia.kyc_centralized.view.activity

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kyc_centralized.di.*
import com.tokopedia.kyc_centralized.kycRobot
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.kyc_centralized.upload
import com.tokopedia.utils.image.ImageProcessingUtil

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
        stubSampleForLiveness()

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            atCameraClickCapture()
            atCameraClickNext()
            atFaceIntroClickNext()
            // In this segment, liveness face result is provided by intent stubbing
        } upload {
            shouldShowPendingPage()
        }
    }

    @Test
    fun retakeFaceOnlyTest() {
        ActivityComponentFactory.instance = FakeKycActivityComponentFactory(
            case = FakeKycUploadApi.Case.Retake(
                arrayListOf(2)
            )
        )
        activityTestRule.launchActivity(null)
        stubSampleForLiveness()

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            atCameraClickCapture()
            atCameraClickNext()
            atFaceIntroClickNext()

            // In this segment, liveness face result is provided by intent stubbing

            atFinalPressCta()

        } upload {
            shouldShowPendingPage()
        }
    }

    @Test
    fun retakeKtpAndFaceTest() {
        ActivityComponentFactory.instance = FakeKycActivityComponentFactory(
            case = FakeKycUploadApi.Case.Retake(
                arrayListOf(1, 2)
            )
        )
        activityTestRule.launchActivity(null)
        stubSampleForLiveness()

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
        } upload {
            shouldShowPendingPage()
        }
    }


    private fun stubSampleForLiveness() {
        intending(hasData(ApplinkConstInternalGlobal.LIVENESS_DETECTION)).respondWithFunction {
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
}