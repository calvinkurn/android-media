package com.tokopedia.product.detail.topads

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.CenterLayoutManager
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.viewholder.PdpRecommendationWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductDetailTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule =
        object : IntentsTestRule<ProductDetailActivity>(ProductDetailActivity::class.java) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                setupTopAdsDetector()
            }

            override fun getActivityIntent(): Intent {
                val context = InstrumentationRegistry.getInstrumentation().targetContext
                return ProductDetailActivity.createIntent(context, "2170611118").apply {
                    putExtra("layoutID", "177")
                }
            }
        }

    @Before
    fun doBeforeRun() {
        topAdsAssertion = TopAdsAssertion(
            activityRule.activity,
            activityRule.activity.application as TopAdsVerificatorInterface
        )
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    @Test
    fun testTopAdsPdp() {
        login()
        waitForData()

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_pdp)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        for (i in 0 until itemCount) {
            waitForData(500)
            scrollRecyclerViewToPosition(recyclerView, i)
            checkTopAdsOnProductRecommendationViewHolder(recyclerView, i)
        }
        topAdsAssertion?.assert()
    }

    private fun checkTopAdsOnProductRecommendationViewHolder(recyclerView: RecyclerView, i: Int) {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(i)
        if (
            viewHolder is ProductRecommendationViewHolder ||
            viewHolder is PdpRecommendationWidgetViewHolder
        ) {
            waitForData()
            val childRecyclerView: RecyclerView? = viewHolder.getChildRecyclerView()

            // check if adapter null, means recom widget data is empty from backend
            if (childRecyclerView != null && childRecyclerView.adapter != null) {
                clickOnEachItemRecyclerView(
                    view = viewHolder.itemView,
                    recyclerViewId = childRecyclerView.id,
                    fixedItemPositionLimit = 0
                )
            }
        }
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as CenterLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun waitForData(millis: Long = 15000) {
        Thread.sleep(millis)
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun RecyclerView.ViewHolder.getChildRecyclerView(): RecyclerView? {
        return itemView
            .findViewById(R.id.carouselProductCardRecyclerView) ?: itemView
            .findViewById(R.id.rv_recommendation_vertical)
    }
}
