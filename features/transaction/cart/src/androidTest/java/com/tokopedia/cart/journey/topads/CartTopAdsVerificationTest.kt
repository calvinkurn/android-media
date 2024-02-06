package com.tokopedia.cart.journey.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.cart.CartActivity
import com.tokopedia.cart.R
import com.tokopedia.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.test.application.annotations.TopAdsTest
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@TopAdsTest
class CartTopAdsVerificationTest {

    private var topAdsAssertion: TopAdsAssertion? = null
    private var topAdsCount = 0

    @get:Rule
    var activityRule = object : IntentsTestRule<CartActivity>(CartActivity::class.java) {
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

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
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
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        i,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View> = ViewMatchers.isClickable()

                            override fun getDescription(): String = "click product item card"

                            override fun perform(uiController: UiController?, view: View?) {
                                try {
                                    view?.performClick()
                                } catch (e: PerformException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    )
                )
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
