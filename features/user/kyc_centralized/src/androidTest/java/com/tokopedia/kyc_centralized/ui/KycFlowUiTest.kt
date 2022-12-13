package com.tokopedia.kyc_centralized.ui

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_FORM
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.FakeKycActivityComponentFactory
import com.tokopedia.kyc_centralized.fakes.FakeKycUploadApi
import com.tokopedia.kyc_centralized.kycRobot
import com.tokopedia.kyc_centralized.stubKtpCamera
import com.tokopedia.kyc_centralized.stubLiveness
import com.tokopedia.kyc_centralized.ui.tokoKyc.info.UserIdentificationInfoActivity
import com.tokopedia.kyc_centralized.upload
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class KycFlowUiTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UserIdentificationInfoActivity::class.java, false, false
    )

    private val testComponent = FakeKycActivityComponentFactory()
    private val kycApi = testComponent.kycApi
    private val projectId = "22"
    private val url = "https://google.com"

    @Before
    fun setup() {
        ActivityComponentFactory.instance = testComponent
    }

    @Test
    fun happyFlowTest() {
        kycApi.case = FakeKycUploadApi.Case.Success
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_FORM, projectId, url))
        }
        activityTestRule.launchActivity(i)
        stubSampleForKtpAndLivenessPictures()

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            // KTP Camera is stubbed here
            atFaceIntroClickNext()
            // Liveness is stubbed here
        } upload {
            shouldShowPendingPage()
            hasCameraIntent()
            hasLivenessIntent(1, projectId)
        }
    }

    @Test
    fun retakeFaceOnlyTest() {
        kycApi.case = FakeKycUploadApi.Case.Retake(arrayListOf(KYCConstant.FACE_RETAKE))
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_FORM, projectId, url))
        }
        activityTestRule.launchActivity(i)
        stubSampleForKtpAndLivenessPictures()

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            // KTP Camera is stubbed here
            atFaceIntroClickNext()
            // Liveness is stubbed here
            atFinalPressCta()

        } upload {
            shouldShowPendingPage()
            hasCameraIntent()
            hasLivenessIntent(2, projectId)
        }
    }

    @Test
    fun retakeKtpOnlyTest() {
        kycApi.case = FakeKycUploadApi.Case.Retake(arrayListOf(KYCConstant.KTP_RETAKE))
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_FORM, projectId, url))
        }
        activityTestRule.launchActivity(i)
        stubSampleForKtpAndLivenessPictures()

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            // KTP Camera is stubbed here
            atFaceIntroClickNext()

            atFinalPressCta()
            // KTP Camera and Liveness is stubbed here
        } upload {
            shouldShowPendingPage()
            hasCameraIntent(2)
            hasLivenessIntent(2, projectId)
        }
    }

    @Test
    fun retakeKtpAndFaceTest() {
        kycApi.case = FakeKycUploadApi.Case.Retake(arrayListOf(KYCConstant.KTP_RETAKE, KYCConstant.FACE_RETAKE))
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_FORM, projectId, url))
        }
        activityTestRule.launchActivity(i)
        stubSampleForKtpAndLivenessPictures()

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            // KTP Camera is stubbed here
            atFaceIntroClickNext()

            atFinalPressCta()
            // KTP Camera and Liveness is stubbed here
        } upload {
            shouldShowPendingPage()
            hasCameraIntent(2)
            hasLivenessIntent(2, projectId)
        }
    }

    @Test
    fun retryCausedByNetworkTest() {
        kycApi.case = FakeKycUploadApi.Case.NetworkFailed
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_FORM, projectId, url))
        }
        activityTestRule.launchActivity(i)
        stubSampleForKtpAndLivenessPictures()

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            // KTP Camera is stubbed here
            atFaceIntroClickNext()
            // Liveness is stubbed here
            atFinalPressErrorButton()
        } upload {
            shouldShowPendingPage()
            hasCameraIntent()
            hasLivenessIntent(2, projectId)
        }
    }

    /**
     * In this scenario, we also stub the intent for KTP Camera request even though the
     * [UserIdentificationCameraActivity] is also part of this module, we avoid testing it
     * because capturing picture with CameraView always failed on emulator devices
     *
     * To simulate the real camera, you can use [IssueSimulatorUserIdentification]
     * */
    private fun stubSampleForKtpAndLivenessPictures() {
        stubLiveness(projectId)
        stubKtpCamera()
    }
}
