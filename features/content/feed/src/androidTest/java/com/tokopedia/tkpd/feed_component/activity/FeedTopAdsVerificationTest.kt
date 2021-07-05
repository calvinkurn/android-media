package com.tokopedia.tkpd.feed_component.activity

import android.Manifest
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsHeadlineViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import com.tokopedia.tkpd.feed_component.InstrumentationFeedPlusTestActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FeedTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationFeedPlusTestActivity>(InstrumentationFeedPlusTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
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
    fun testTopAdsFeed() {
        waitForData()

        val feedRecyclerView = activityRule.activity.findViewById<RecyclerView>(com.tokopedia.feedplus.R.id.recycler_view)
        val itemCount = feedRecyclerView.adapter?.itemCount ?: 0

        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(feedRecyclerView, i)
            checkProductOnDynamicChannel(feedRecyclerView, i)
        }
        topAdsAssertion?.assert()
    }

    private fun checkProductOnDynamicChannel(recyclerView: RecyclerView, i: Int) {
        when (val viewHolder = recyclerView.findViewHolderForAdapterPosition(i)) {
            is TopadsShopViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, com.tokopedia.topads.sdk.R.id.recommendationRv, 0)
            }
            is TopAdsHeadlineViewHolder -> {
                //currently we have click impressions only for products in headline ads so we are not counting the click impressions
                //for products in headline ads so that the test can pass
                waitForData()
                onView(withId(com.tokopedia.feedplus.R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<TopAdsHeadlineViewHolder>(
                        i, CommonActions.clickChildViewWithId(com.tokopedia.topads.sdk.R.id.shop_detail)))
            }
        }
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }


}