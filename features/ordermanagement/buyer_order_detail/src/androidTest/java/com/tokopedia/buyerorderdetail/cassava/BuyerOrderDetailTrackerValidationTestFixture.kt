package com.tokopedia.buyerorderdetail.cassava

import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorderdetail.Utils
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderDetailActivity
import com.tokopedia.cassavatest.CassavaTestRule
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit

abstract class BuyerOrderDetailTrackerValidationTestFixture {
    @get:Rule
    var activityRule: IntentsTestRule<BuyerOrderDetailActivity> = object : IntentsTestRule<BuyerOrderDetailActivity>(BuyerOrderDetailActivity::class.java, false, false) {}

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun init() {
        IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
        clearGtmLog()
        Utils.login()
    }

    private fun clearGtmLog() {
        gtmLogDBSource.deleteAll().toBlocking().first()
    }
}