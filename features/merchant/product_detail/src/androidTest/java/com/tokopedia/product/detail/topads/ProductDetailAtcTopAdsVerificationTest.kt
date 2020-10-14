package com.tokopedia.product.detail.topads

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.presentation.InstrumentTestAddToCartBottomSheet
import com.tokopedia.product.detail.presentation.InstrumentTestProductDetailActivity
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneRecommendationViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import com.tokopedia.unifycomponents.UnifyButton
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductDetailAtcTopAdsVerificationTest {

    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentTestProductDetailActivity>(InstrumentTestProductDetailActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupTopAdsDetector()
        }

        override fun getActivityIntent(): Intent {
            return Intent(InstrumentationRegistry.getInstrumentation().targetContext, InstrumentTestProductDetailActivity::class.java)
        }
    }

    @Before
    fun doBeforeRun() {
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
    fun testTopAdsPdp() {
        login()
        waitForData()

        getRecyclerView()?.let {
            val itemCount = it.adapter?.itemCount ?: 0
            for (i in 0 until itemCount) {
                scrollRecyclerViewToPosition(it, i)
                checkTopAdsOnProductRecommendationViewHolder(it, i)
            }
            topAdsAssertion?.assert()
        }
    }

    private fun checkTopAdsOnProductRecommendationViewHolder(recyclerView: RecyclerView, i: Int) {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(i)
        if(viewHolder is AddToCartDoneRecommendationViewHolder) {
            waitForData()
            clickOnEachItemRecyclerView(viewHolder.itemView, R.id.carouselProductCardRecyclerView, 0)
        }
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun clickOnEachItemRecyclerView(view: View, recyclerViewId: Int, fixedItemPositionLimit: Int) {
        val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)
        var childItemCount = childRecyclerView.adapter!!.itemCount
        if (fixedItemPositionLimit > 0) {
            childItemCount = fixedItemPositionLimit
        }
        for (i in 0 until childItemCount) {
            try {
                Espresso.onView(Matchers.allOf(ViewMatchers.withId(recyclerViewId), ViewMatchers.isCompletelyDisplayed()))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
            } catch (e: PerformException) {
                e.printStackTrace()
            }
        }
    }

    private fun getRecyclerView(): RecyclerView? {
        val addToCartBottomSheet = activityRule.activity.supportFragmentManager.findFragmentByTag("ADD_TO_CART") as? InstrumentTestAddToCartBottomSheet
        return addToCartBottomSheet?.getRecyclerView()
    }
}