package com.tokopedia.cart.journey.topads

import android.Manifest
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.cart.R
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartTopAdsVerificationTest {

    private var topAdsAssertion: TopAdsAssertion? = null
    private var topAdsCount = 0

    @get:Rule
    var activityRule = object : ActivityTestRule<CartActivity>(CartActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupTopAdsDetector()
            // Should do login before activity launched to prevent racing condition with get cart
            login()
        }
    }

    @get:Rule
    var grantPermission: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @Before
    fun setTopAdsAssertion() {
        // reset top ads count
        topAdsCount = 0
        topAdsAssertion = TopAdsAssertion(activityRule.activity, TopAdsVerificatorInterface { topAdsCount })

        waitForData()
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    @Test
    fun testTopAdsCart() {
        waitForData()

        val cartRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_cart)
        val itemCount = cartRecyclerView.adapter?.itemCount ?: 0

        for (i in 0 until itemCount) {
            scrollCartRecyclerViewToPosition(cartRecyclerView, i)
            checkItemType(cartRecyclerView, i)
        }

        waitForData()

        topAdsAssertion?.assert()
    }

    private fun checkItemType(cartRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = cartRecyclerView.findViewHolderForAdapterPosition(i)) {
            is CartRecommendationViewHolder -> {
                if (viewHolder.isTopAds) {
                    topAdsCount++
                }
                clickProductRecommendationItem(cartRecyclerView, i)
            }
        }
    }

    private fun clickProductRecommendationItem(cartRecyclerView: RecyclerView, i: Int) {
        try {
            Espresso.onView(ViewMatchers.withId(cartRecyclerView.id))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }

    private fun scrollCartRecyclerViewToPosition(cartRecyclerView: RecyclerView, position: Int) {
        val layoutManager = cartRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }
}