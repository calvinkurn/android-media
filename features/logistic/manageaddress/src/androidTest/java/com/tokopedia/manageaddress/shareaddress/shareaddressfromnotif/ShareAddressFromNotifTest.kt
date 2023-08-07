package com.tokopedia.manageaddress.shareaddress.shareaddressfromnotif

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.manageaddress.di.*
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@CassavaTest
@RunWith(AndroidJUnit4::class)
class ShareAddressFromNotifTest {

    @get:Rule
    var mActivityTestRule = IntentsTestRule(ManageAddressActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    @Inject
    lateinit var repo: FakeGraphqlRepository

    @Before
    fun setup() {
        val stub = ActivityComponentFactoryStub()
        ActivityComponentFactory.instance = stub
        stub.activityComponent.inject(this)
    }

    @Test
    fun share_address_from_notification() {
        shareAddress {
            launchFrom(mActivityTestRule, "123")
            selectFirstAddress()
            clickShareAddressButton()
            intendingIntent()
            clickDisagreeButton()
            repo.isValidAddressFromNotif = true
            selectFirstAddress()
            intendingIntent()
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
