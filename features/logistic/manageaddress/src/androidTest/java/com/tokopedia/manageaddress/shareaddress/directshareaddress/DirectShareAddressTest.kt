package com.tokopedia.manageaddress.shareaddress.directshareaddress

import android.Manifest
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.manageaddress.di.*
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class DirectShareAddressTest {

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityTestRule = IntentsTestRule(ManageAddressActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    @Before
    fun setup() {
        val stub = ActivityComponentFactoryStub()
        ActivityComponentFactory.instance = stub
        stub.activityComponent.inject(this)
    }

    @Test
    fun share_address_direct_share_test() {
        shareAddress {
            launchFrom(mActivityTestRule, "123")
            selectFirstAddress()
            deleteSelectedItem()
            selectAllAddress()
            saveAddress()
            clickIconShareOnFirstAddress()
            clickIconContactPhoneNumber()
            clickShareButton()
            clickDisagreeButton()
        } validateAnalytics {
            hasPassedAnalytics(cassavaRule, QUERY_ID)
        }
    }

    companion object {
        private const val QUERY_ID = "343"
    }
}
