package com.tokopedia.tkpd.category_levels.activity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule
import com.tokopedia.categorylevels.R
import com.tokopedia.common_category.viewholders.ProductCardViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import com.tokopedia.tkpd.category_levels.InstrumentationProductNavTestActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryLevelsTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationProductNavTestActivity>(InstrumentationProductNavTestActivity::class.java) {
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
    fun testTopAdsCategory() {
        waitForData()

        val productsRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.product_recyclerview)
        val itemCount = productsRecyclerView.adapter?.itemCount?:0

        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(productsRecyclerView, i)
            checkProductOnDynamicChannel(productsRecyclerView, i)
        }
        topAdsAssertion?.assert()
    }

    private fun checkProductOnDynamicChannel(recyclerView: RecyclerView, i: Int) {
        when (recyclerView.findViewHolderForAdapterPosition(i)) {
            is ProductCardViewHolder -> {
                CommonActions.clickChildViewWithId(R.id.productCardView)
            }
        }
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }


}