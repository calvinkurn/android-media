package com.tokopedia.cart

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
import com.tokopedia.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartTopAdsVerificationTest {

    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentTestCartActivity>(InstrumentTestCartActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupTopAdsDetector()
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
        when (cartRecyclerView.findViewHolderForAdapterPosition(i)) {
            is CartRecommendationViewHolder -> {
                clickProductRecommendationItem(cartRecyclerView, i)
            }
        }
    }

    private fun clickProductRecommendationItem(cartRecyclerView: RecyclerView, i: Int) {
        try {
            Espresso.onView(allOf(ViewMatchers.withId(cartRecyclerView.id), ViewMatchers.isDisplayingAtLeast(50)))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }

    private fun scrollCartRecyclerViewToPosition(buyerRecyclerView: RecyclerView, position: Int) {
        val layoutManager = buyerRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }
}