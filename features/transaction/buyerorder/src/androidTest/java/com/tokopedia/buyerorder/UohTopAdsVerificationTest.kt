package com.tokopedia.buyerorder

import android.Manifest
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder.UohRecommendationItemViewHolder
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.*

class UohTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var grantPermission: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @get:Rule
    var activityRule = object: ActivityTestRule<UohListActivity>(UohListActivity::class.java) {
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

        login()
        waitForData()
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    @Test
    fun testTopAdsUoh() {
        waitForData()

        val uohRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_order_list)
        val itemCount = uohRecyclerView.adapter?.itemCount?:0

        for (i in 0 until itemCount) {
            scrollUohRecyclerViewToPosition(uohRecyclerView, i)
            checkProduct(uohRecyclerView, i)
        }
        waitForData()
        topAdsAssertion?.assert()
    }

    private fun checkProduct(uohRecyclerView: RecyclerView, i: Int) {
        when (uohRecyclerView.findViewHolderForAdapterPosition(i)) {
            is UohRecommendationItemViewHolder -> {
                clickProductRecommItem(uohRecyclerView, i)
            }
        }
    }

    private fun clickProductRecommItem(cartRecyclerView: RecyclerView, i: Int) {
        try {
            Espresso.onView(ViewMatchers.withId(cartRecyclerView.id))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollUohRecyclerViewToPosition(uohRecyclerView: RecyclerView, position: Int) {
        val layoutManager = uohRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }
}
