package com.tokopedia.buyerorder.topads

import android.Manifest
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.getUohItemAdapter
import com.tokopedia.buyerorder.runBot
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder.UohRecommendationItemViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Rule
import org.junit.Test

class UohTopAdsVerificationTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var topAdsCount = 0
    private var topAdsAssertion = TopAdsAssertion(context) { topAdsCount }

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
            InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
            setupTopAdsDetector()
        }
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion.after()
    }

    @Test
    fun testTopAdsUoh() {
        runBot {
            loading()
            // search for abnormal order ("zzzzz") to create empty order list
            // so recommendation items will show
            doSearch("zzzzz")

            val uohRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_order_list)
            val itemCount = uohRecyclerView.adapter?.itemCount ?: 0

            for (i in 0 until itemCount) {
                scrollUohRecyclerViewToPosition(uohRecyclerView, i)
                checkProduct(uohRecyclerView, i)
            }
            loading()
        }
        // Must be put outside of uoh robot scope, for class name report generator
        topAdsAssertion.assert()
    }

    private fun checkProduct(uohRecyclerView: RecyclerView, i: Int) {
        when (uohRecyclerView.findViewHolderForAdapterPosition(i)) {
            is UohRecommendationItemViewHolder -> {
                val recommItem = uohRecyclerView.getUohItemAdapter().getRecommendationItemAtIndex(i)
                if (recommItem.isTopAds) {
                    topAdsCount++
                    clickProductRecommItem(uohRecyclerView, i)
                }
            }
        }
    }

    private fun clickProductRecommItem(uohRecyclerView: RecyclerView, i: Int) {
        try {
            onView(withId(uohRecyclerView.id))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }

    private fun scrollUohRecyclerViewToPosition(uohRecyclerView: RecyclerView, position: Int) {
        val layoutManager = uohRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }
}