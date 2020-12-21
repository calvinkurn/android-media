package com.tokopedia.home_wishlist.topads

import android.util.Log
import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.activity.InstrumentationWishlistTestActivity
import com.tokopedia.home_wishlist.topads.TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog
import com.tokopedia.home_wishlist.view.viewholder.DynamicCarouselRecommendationViewHolder
import com.tokopedia.home_wishlist.view.viewholder.RecommendationItemViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.home_wishlist.view.viewholder.BannerTopAdsViewHolder
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WishlistTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule: IntentsTestRule<InstrumentationWishlistTestActivity> = IntentsTestRule(InstrumentationWishlistTestActivity::class.java)

    @Before
    fun setTopAdsAssertion() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

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
    fun testTopAds() {

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = recyclerView.adapter?.itemCount?:0

        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }
        topAdsAssertion?.assert()
    }


    private fun logTestMessage(message: String) {
        writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d("TopAdsVerificatorLog", message)
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)) {
            is RecommendationItemViewHolder -> {
                activityRule.runOnUiThread { viewHolder.itemView.findViewById<View>(R.id.product_item).performClick() }
            }
            is DynamicCarouselRecommendationViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.carouselProductCardRecyclerView, 0)
            }
            is BannerTopAdsViewHolder -> {
                activityRule.runOnUiThread { viewHolder.itemView.findViewById<View>(R.id.wishlist_topads_image_view).performClick() }
            }
        }
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }
}
