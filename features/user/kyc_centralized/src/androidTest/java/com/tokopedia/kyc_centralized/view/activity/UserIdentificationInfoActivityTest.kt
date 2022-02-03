package com.tokopedia.kyc_centralized.view.activity

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.FakeKycActivityComponentFactory
import com.tokopedia.kyc_centralized.fakes.FakeKycUploadApi
import com.tokopedia.kyc_centralized.kycRobot
import com.tokopedia.kyc_centralized.stubKtpCamera
import com.tokopedia.kyc_centralized.stubLiveness
import com.tokopedia.kyc_centralized.upload
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class UserIdentificationInfoActivityTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UserIdentificationInfoActivity::class.java, false, false
    )

    @Before
    fun setup() {
        // no op
    }

    @Test
    fun happyFlowTest() {
        ActivityComponentFactory.instance = FakeKycActivityComponentFactory()
        activityTestRule.launchActivity(null)
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
        stubSampleForKtpAndLivenessPictures()

        kycRobot {
            checkTermsAndCondition()
            atInfoClickNext()
            atKtpIntroClickNext()
            // KTP Camera is stubbed here
            atFaceIntroClickNext()

            atFinalPressCta()
            // KTP Camera is stubbed here
        } upload {
            shouldShowPendingPage()
        }
    }

    @Test
    fun retryCausedByNetworkTest() {
        ActivityComponentFactory.instance = FakeKycActivityComponentFactory(
            case = FakeKycUploadApi.Case.NetworkFailed
        )
        activityTestRule.launchActivity(null)
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
        }
    }

    /**
     * In this scenario, we also stub the intent for requesting KTP Camera even though the
     * [UserIdentificationCameraActivity] is also part of this module, we avoid testing it
     * because capturing picture with CameraView requires physical device
     *
     * To test the camera, you can use [IssueSimulatorUserIdentification]
     * */
    private fun stubSampleForKtpAndLivenessPictures() {
        stubLiveness()
        stubKtpCamera()
    }
}