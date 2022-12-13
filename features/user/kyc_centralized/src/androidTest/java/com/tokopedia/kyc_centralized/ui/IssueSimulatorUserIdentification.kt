package com.tokopedia.kyc_centralized.ui

import android.Manifest
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.FakeKycActivityComponentFactorySimulateNullPref
import com.tokopedia.kyc_centralized.kycRobot
import com.tokopedia.kyc_centralized.stubLiveness
import com.tokopedia.kyc_centralized.ui.tokoKyc.info.UserIdentificationInfoActivity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
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

    @Before
    fun setup() {
        // no op
    }

    @Test
    fun simTestExpectedIvLengthWithEmptyPreference() {
        ActivityComponentFactory.instance = FakeKycActivityComponentFactorySimulateNullPref()
        activityTestRule.launchActivity(null)
        stubLiveness()

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
