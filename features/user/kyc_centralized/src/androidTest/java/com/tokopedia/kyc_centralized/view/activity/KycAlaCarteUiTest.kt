package com.tokopedia.kyc_centralized.view.activity

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_ALA_CARTE
import com.tokopedia.kyc_centralized.*
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.FakeKycActivityComponentFactory
import com.tokopedia.kyc_centralized.fakes.FakeKycUploadApi
import com.tokopedia.kyc_centralized.ui.cKyc.alacarte.UserIdentificationInfoSimpleActivity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class KycAlaCarteUiTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UserIdentificationInfoSimpleActivity::class.java, false, false
    )

    private val testComponent = FakeKycActivityComponentFactory()
    private val kycApi = testComponent.kycApi

    @Before
    fun setup() {
        ActivityComponentFactory.instance = testComponent
    }

    @Test
    fun whenAlaCarte_shouldSuccessWithCorrectRedirect() {
        val projectId = "18"
        kycApi.case = FakeKycUploadApi.Case.Success
        val url = "https://tokopedia.com"
        val i = Intent().apply {
            data = Uri.parse(
                UriUtil.buildUri(KYC_ALA_CARTE, projectId, "false", url, "ktpWithLiveness")
            )
        }
        activityTestRule.launchActivity(i)
        stubKtpCamera()
        stubLivenessFailed(projectId)
        kycRobot {
            atKtpIntroClickNext()
            atFaceIntroClickNext()
            stubLiveness(projectId)
            atFaceIntroClickNext()
        } upload {
            hasRedirectUrl(activityTestRule, url)
        }
    }

}
