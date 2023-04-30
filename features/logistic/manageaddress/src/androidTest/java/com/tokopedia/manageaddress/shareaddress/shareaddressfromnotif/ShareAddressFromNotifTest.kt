package com.tokopedia.manageaddress.shareaddress.shareaddressfromnotif

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.manageaddress.di.*
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.KEY_SHARE_ADDRESS_LOGI
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class ShareAddressFromNotifTest {

    @get:Rule
    var mActivityTestRule = IntentsTestRule(ManageAddressActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val component = DaggerTestAppComponent.builder().fakeAppModule(FakeAppModule(ctx)).build()
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)
        setupAbTestRemoteConfig()
    }

    private fun setupAbTestRemoteConfig() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            KEY_SHARE_ADDRESS_LOGI,
            KEY_SHARE_ADDRESS_LOGI
        )
    }

    @Test
    fun share_address_from_notification() {
        shareAddress {
            launchFrom(mActivityTestRule, "123")
            selectFirstAddress()
            clickShareAddressButton()
            clickDisagreeButton()
            selectFirstAddress()
            clickShareAddressButton()
            clickAgreeButton()
        } validateAnalytics {
            hasPassedAnalytics(cassavaRule, QUERY_ID)
        }
    }

    companion object {
        private const val QUERY_ID = "346"
    }
}
