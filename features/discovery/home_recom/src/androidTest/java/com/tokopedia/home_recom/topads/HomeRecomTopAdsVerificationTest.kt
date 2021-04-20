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
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.activity.HomeRecommendationActivityTest
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.view.adapter.HomeRecommendationAdapter
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

    @get:Rule
    var activityRule = object : IntentsTestRule<HomeRecommendationActivityTest>(HomeRecommendationActivityTest::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            login()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var topAdsCount = 0
    private val topAdsAssertion = TopAdsAssertion(context, TopAdsVerificatorInterface { topAdsCount })

    @Before
    fun setTopAdsAssertion() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        waitForData()
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion.after()
    }

    @Test
    fun testTopAdsHome() {
        waitForData()

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = recyclerView.adapter?.itemCount?:0

        val itemList = recyclerView.getItemList()
        topAdsCount = calculateTopAdsCount(itemList)

        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }
        topAdsAssertion.assert()
    }

    private fun calculateTopAdsCount(itemList: List<Visitable<*>>) : Int {
        var count = 0
        for (item in itemList) {
            count += countTopAdsInItem(item)
        }
        return count
    }

    private fun countTopAdsInItem(item: Visitable<*>) : Int {
        var count = 0

        when (item) {
            is RecommendationItemDataModel -> {
                if (item.productItem.isTopAds) count++
            }
            is RecommendationCarouselDataModel -> {
                for (product in item.products)
                    if (product.productItem.isTopAds) count++
            }
        }
        return count
    }

    private fun waitForData() {
        Thread.sleep(5000)
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
            is RecommendationCarouselViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.carouselProductCardRecyclerView, 0)
            }
        }
    }


    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }

    private fun RecyclerView.getItemList(): List<Visitable<*>> {
        val homeAdapter = this.adapter as? HomeRecommendationAdapter

        if (homeAdapter == null) {
            val detailMessage = "Adapter is not ${HomeRecommendationAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return homeAdapter.list
    }
}
