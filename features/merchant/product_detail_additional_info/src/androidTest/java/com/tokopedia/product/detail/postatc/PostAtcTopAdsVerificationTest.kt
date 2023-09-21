package com.tokopedia.product.detail.postatc

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.PostAtcHelper
import com.tokopedia.product.detail.postatc.base.PostAtcLayoutManager
import com.tokopedia.product.detail.postatc.view.PostAtcActivity
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet
import com.tokopedia.product.detail.postatc.view.component.recommendation.GlobalRecommendationViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

class PostAtcTopAdsVerificationTest {

    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule = object : IntentsTestRule<PostAtcActivity>(PostAtcActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupTopAdsDetector()
        }

        override fun getActivityIntent(): Intent {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            return PostAtcHelper.getIntent(context, "8787687641")
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

    @Test
    fun testTopAdsPostATC() {
        login()
        waitForData()

        val bottomSheet = activityRule.activity.supportFragmentManager.findFragmentByTag(PostAtcBottomSheet.TAG) as? PostAtcBottomSheet
        assert(bottomSheet != null)

        bottomSheet!!.onClickProduct = {}

        val recyclerView = bottomSheet.view?.findViewById<RecyclerView>(R.id.post_atc_rv)
        assert(recyclerView != null)

        val itemCount = recyclerView!!.adapter?.itemCount ?: 0

        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(recyclerView, i)
            checkTopAdsOnProductRecommendationViewHolder(recyclerView, i)
        }
        topAdsAssertion?.assert()
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as PostAtcLayoutManager
        activityRule.runOnUiThread {
            layoutManager.scrollToPositionWithOffset(position, 0)
        }
    }

    private fun checkTopAdsOnProductRecommendationViewHolder(recyclerView: RecyclerView, position: Int) {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
        if (viewHolder is GlobalRecommendationViewHolder) {
            waitForData()
            clickOnEachItemRecyclerView(
                viewHolder.itemView,
                recommendation_widget_commonR.id.recommendation_carousel_product,
                0
            )
        }
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }
}
