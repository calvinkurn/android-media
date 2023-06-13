package com.tokopedia.kyc_centralized.ui

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kyc_centralized.ViewIdGenerator
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.FakeKycActivityComponentFactory
import com.tokopedia.kyc_centralized.fakes.FakeKycUploadApi
import com.tokopedia.kyc_centralized.kycRobot
import com.tokopedia.kyc_centralized.ui.tokoKyc.alacarte.UserIdentificationInfoSimpleActivity
import com.tokopedia.kyc_centralized.util.MockTimber
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@UiTest
@RunWith(AndroidJUnit4::class)
class KycAlaCarteGenerateIdTest {
    private lateinit var timber: MockTimber
    private val testComponent = FakeKycActivityComponentFactory()
    private val kycApi = testComponent.kycApi

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UserIdentificationInfoSimpleActivity::class.java, false, false
    )

    @Before
    fun setup() {
        timber = MockTimber()
        Timber.plant(timber)
        ActivityComponentFactory.instance = testComponent
    }

    @Test
    fun generate_view_id_file() {
        val projectId = "18"
        kycApi.case = FakeKycUploadApi.Case.Success
        val url = "https://tokopedia.com"
        val i = Intent().apply {
            data = Uri.parse(
                UriUtil.buildUri(ApplinkConstInternalUserPlatform.KYC_ALA_CARTE, projectId, "false", url, "ktpWithLiveness")
            )
        }
        activityTestRule.launchActivity(i)

        kycRobot {
            val rootView = findRootView(activityTestRule.activity)
            ViewIdGenerator.createViewIdFile(rootView, "kyc_centralized_alacarte.csv")
        }
    }
}
