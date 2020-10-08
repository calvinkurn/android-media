package com.tokopedia.home_recom.topads

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.activity.HomeRecommendationActivityTest
import com.tokopedia.home_recom.view.viewholder.RecommendationCarouselViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationItemViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 26/07/20.
 */

class HomeRecomTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule: IntentsTestRule<HomeRecommendationActivityTest> = IntentsTestRule(HomeRecommendationActivityTest::class.java)

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
    fun testTopAdsHome() {
        waitForData()

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = recyclerView.adapter?.itemCount?:0

        val nestedScrollView = activityRule.activity.findViewById<RecyclerView>(R.id.recomNestedScrollView)

        for (i in 0 until itemCount) {
            scrollNestedToPosition(recyclerView, nestedScrollView, i)
            //scrollHomeRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }
        topAdsAssertion?.assert()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun scrollNestedToPosition(recyclerView: RecyclerView, nestedScrollView: NestedScrollView, position: Int) {
        val targetItem = recyclerView.findViewHolderForAdapterPosition(position)?.itemView
        nestedScrollView.requestChildFocus(targetItem, targetItem)
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)) {
            is RecommendationItemViewHolder -> {
                activityRule.runOnUiThread { viewHolder.itemView.findViewById<View>(R.id.product_item).performClick() }
            }
            is RecommendationCarouselViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.carouselProductCardRecyclerView, 0)
            }
        }
    }


    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }
}
