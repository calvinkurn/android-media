package com.tokopedia.kyc_centralized.ui

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_FORM
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_INFO
import com.tokopedia.kyc_centralized.*
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.FakeKycActivityComponentFactory
import com.tokopedia.kyc_centralized.fakes.FakeKycUploadApi
import com.tokopedia.kyc_centralized.ui.tokoKyc.info.UserIdentificationInfoActivity
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
        UserIdentificationInfoActivity::class.java,
        false,
        false
    )

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val testComponent = FakeKycActivityComponentFactory()
    private val kycApi = testComponent.kycApi
    private val projectId = "22"
    private var gocicilProjectId = "21"
    private val url = "https://google.com"

    @Before
    fun setup() {
        stubAppGraphqlRepo()
        ActivityComponentFactory.instance = testComponent
    }

    @Test
    fun happyFlowTest() {
        kycApi.case = FakeKycUploadApi.Case.Success
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_INFO, projectId))
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
        } validate {
            shouldShowPendingPage()
            hasCameraIntent()
            hasLivenessIntent(1, projectId)
        }
    }

    @Test
    fun retakeFaceOnlyTest() {
        kycApi.case = FakeKycUploadApi.Case.Retake(arrayListOf(KYCConstant.FACE_RETAKE))
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_INFO, projectId))
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
        } validate {
            shouldShowPendingPage()
            hasCameraIntent()
            hasLivenessIntent(2, projectId)
        }
    }

    @Test
    fun retakeKtpOnlyTest() {
        kycApi.case = FakeKycUploadApi.Case.Retake(arrayListOf(KYCConstant.KTP_RETAKE))
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_INFO, projectId))
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
        } validate {
            shouldShowPendingPage()
            hasCameraIntent(2)
            hasLivenessIntent(2, projectId)
        }
    }

    @Test
    fun retakeKtpAndFaceTest() {
        kycApi.case = FakeKycUploadApi.Case.Retake(arrayListOf(KYCConstant.KTP_RETAKE, KYCConstant.FACE_RETAKE))
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_INFO, projectId))
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
        } validate {
            shouldShowPendingPage()
            hasCameraIntent(2)
            hasLivenessIntent(2, projectId)
        }
    }

    @Test
    fun retryCausedByNetworkTest() {
        kycApi.case = FakeKycUploadApi.Case.NetworkFailed
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_INFO, projectId))
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
        } validate {
            shouldShowPendingPage()
            hasCameraIntent()
            hasLivenessIntent(2, projectId)
        }
    }

    /**
     * this function only used by gocicil project id
     */
    @Test
    fun blockRedirectionSelfieKycFlowTest() {
        kycApi.case = FakeKycUploadApi.Case.Success
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(KYC_FORM, gocicilProjectId, url))
        }
        activityTestRule.launchActivity(i)
        stubForKtpAndBlockRedirectionSelfie()

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            // KTP Camera is stubbed here
            atFaceIntroClickNext()
            // Liveness is stubbed here
        } validate {
            shouldShowErrorSnackbar(R.string.error_liveness_is_not_supported)
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

    /**
     * In this scenario, we stub liveness with result NOT_SUPPORT_LIVENESS
     * on the activity result, and we want to block user switching the KYC to selfie
     * if their device is not capable to do liveness
     * */
    private fun stubForKtpAndBlockRedirectionSelfie() {
        stubLiveness(gocicilProjectId, KYCConstant.NOT_SUPPORT_LIVENESS)
        stubKtpCamera()
    }
}
