package com.tokopedia.orderhistory.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.view.activity.base.OrderHistoryTest
import com.tokopedia.orderhistory.view.adapter.viewholder.OrderHistoryViewHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Test

class OrderHistoryCassavaTest : OrderHistoryTest() {

    private val gtmLogDbSource = GtmLogDBSource(context)
    private val cassavaDir = "tracker/user/orderhistory"

    private val cassavaImpressionProduct = "$cassavaDir/product_card_impression.json"
    private val cassavaClickProduct = "$cassavaDir/product_card_click.json"

    @ExperimentalCoroutinesApi
    @Before
    override fun before() {
        super.before()
        gtmLogDbSource.deleteAll().subscribe()
    }

    @Test
    fun assess_product_card_impression() {
        // Given
        setupOrderHistoryActivity()
        getProductOrderHistoryUseCase.response = chatHistoryProductResponse
        intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        inflateTestFragment()

        // Then
        verifyRecyclerViewDisplayed(R.id.recycler_view)
        assertThat(
                getAnalyticsWithQuery(gtmLogDbSource, context, cassavaImpressionProduct),
                hasAllSuccess()
        )
    }

    @Test
    fun assess_product_card_click() {
        // Given
        setupOrderHistoryActivity()
        getProductOrderHistoryUseCase.response = chatHistoryProductResponse
        intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        inflateTestFragment()

        // When
        performClickOnProductCard(R.id.recycler_view)

        // Then
        verifyRecyclerViewDisplayed(R.id.recycler_view)
        assertThat(
                getAnalyticsWithQuery(gtmLogDbSource, context, cassavaClickProduct),
                hasAllSuccess()
        )
    }

    private fun verifyRecyclerViewDisplayed(@IdRes recyclerViewId: Int) {
        onView(allOf(isDisplayed(), withId(recyclerViewId)))
                .check(matches(isDisplayed()))
    }

    private fun performClickOnProductCard(@IdRes recyclerViewId: Int) {
        val viewAction =
                RecyclerViewActions.actionOnItemAtPosition<OrderHistoryViewHolder>(
                        0, ViewActions.click()
                )
        onView(AllOf.allOf(isDisplayed(), withId(recyclerViewId)))
                .perform(viewAction)
    }
}