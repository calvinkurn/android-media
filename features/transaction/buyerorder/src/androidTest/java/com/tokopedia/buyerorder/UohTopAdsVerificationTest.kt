package com.tokopedia.buyerorder

import android.Manifest
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder.UohRecommendationItemViewHolder
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.*

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 * Verify Topads for HomePage
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class UohTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationUohTestActivity>(InstrumentationUohTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            loginInstrumentationTestTopAdsUser()
            setupTopAdsDetector()
        }
    }

    @Before
    fun setTopAdsAssertion() {
        topAdsAssertion = TopAdsAssertion(
                activityRule.activity,
                activityRule.activity.application as TopAdsVerificatorInterface
        )
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    @Test
    fun testTopAdsHome() {
        waitForData()

        val uohRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_order_list)
        val itemCount = uohRecyclerView.adapter?.itemCount?:0

        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(uohRecyclerView, i)
            checkProductOnDynamicChannel(uohRecyclerView, i)
        }
        topAdsAssertion?.assert()
    }

    private fun checkProductOnDynamicChannel(uohRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = uohRecyclerView.findViewHolderForAdapterPosition(i)) {
            is UohRecommendationItemViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_order_list, 0)
            }
        }
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }
}
