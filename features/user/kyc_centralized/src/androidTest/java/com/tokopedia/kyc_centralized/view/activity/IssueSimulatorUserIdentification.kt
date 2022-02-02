package com.tokopedia.kyc_centralized.view.activity

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.FakeKycActivityComponentFactory
import com.tokopedia.kyc_centralized.di.FakeKycActivityComponentFactorySimulateNullPref
import com.tokopedia.kyc_centralized.kycRobot
import com.tokopedia.kyc_centralized.test.R
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.utils.image.ImageProcessingUtil
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IssueSimulatorUserIdentification {

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
    fun simTestExpectedIvLength() {
        ActivityComponentFactory.instance = FakeKycActivityComponentFactorySimulateNullPref()
        activityTestRule.launchActivity(null)
        val cameraResultFile = ImageProcessingUtil.getTokopediaPhotoPath(
            Bitmap.CompressFormat.JPEG,
            UserIdentificationFormActivity.FILE_NAME_KYC
        )
        val sampleJpeg = ctx.assets.open("sample.jpeg")
        sampleJpeg.copyTo(cameraResultFile.outputStream())
        Intents.intending(IntentMatchers.hasData(ApplinkConstInternalGlobal.LIVENESS_DETECTION)).respondWith(
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
}