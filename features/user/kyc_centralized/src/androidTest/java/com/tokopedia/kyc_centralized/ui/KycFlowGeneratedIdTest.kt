package com.tokopedia.kyc_centralized.ui

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kyc_centralized.ViewIdGenerator
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.FakeKycActivityComponentFactory
import com.tokopedia.kyc_centralized.fakes.FakeKycUploadApi
import com.tokopedia.kyc_centralized.kycRobot
import com.tokopedia.kyc_centralized.ui.tokoKyc.info.UserIdentificationInfoActivity
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class KycFlowGeneratedIdTest {

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
    fun generate_view_id_file() {
        kycApi.case = FakeKycUploadApi.Case.Success
        val i = Intent().apply {
            data = Uri.parse(UriUtil.buildUri(ApplinkConstInternalUserPlatform.KYC_FORM, projectId, url))
        }
        activityTestRule.launchActivity(i)

        kycRobot {
            val rootView = findRootView(activityTestRule.activity)
            ViewIdGenerator.createViewIdFile(rootView, "kyc_centralized_flow.csv")
        }
    }
}

