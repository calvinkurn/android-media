package com.tokopedia.product.detail

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneRecommendationViewHolder
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonMatcher
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
    var activityRule = object: IntentsTestRule<ProductDetailActivity>(ProductDetailActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupTopAdsDetector()
        }

        override fun getActivityIntent(): Intent {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            return ProductDetailActivity.createIntent(context, "604161938")
        }
    }

    @Before
    fun doBeforeRun() {
        topAdsAssertion = TopAdsAssertion(
                activityRule.activity,
                activityRule.activity.application as TopAdsVerificatorInterface
        )
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    @Test
    fun testTopAdsPdp() {
        login()
        waitForData()

        val addToCartButton = activityRule.activity.findViewById<UnifyButton>(R.id.btn_add_to_cart)
        try {
            Espresso.onView(Matchers.allOf(ViewMatchers.withId(addToCartButton.id), ViewMatchers.isDisplayingAtLeast(70)))
                    .perform(ViewActions.click())
        } catch (e: PerformException) {
            e.printStackTrace()
        }

        waitForData()

        val pdpFragment = activityRule.activity.supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as? DynamicProductDetailFragment
        val addToCartBottomSheet = pdpFragment?.fragmentManager?.findFragmentByTag("TAG") as? AddToCartDoneBottomSheet
        val recyclerView = addToCartBottomSheet?.getRecyclerView()

        recyclerView?.let {
            val itemCount = recyclerView.adapter?.itemCount ?: 0

            for (i in 0 until itemCount) {
                scrollRecyclerViewToPosition(recyclerView, i)
                checkTopAdsOnProductRecommendationViewHolder(recyclerView, i)
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
        Thread.sleep(7000)
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
}