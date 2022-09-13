package com.tokopedia.deals.pdp.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ListView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.pdp.common.DealsPDPIdlingResource
import com.tokopedia.deals.pdp.mock.DealsPDPGQLMockResponse
import com.tokopedia.deals.pdp.mock.DealsPDPRestMockResponse
import com.tokopedia.deals.pdp.rule.DealsIdlingResourceTestRule
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.util.setupRestMockResponse
import com.tokopedia.deals.test.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class DealsPDPActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()
    private var idlingResource: IdlingResource? = null

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var activityRule =
        ActivityTestRule(DealsPDPActivity::class.java, false, false)

    @get:Rule
    val dealsIdlingResourceTestRule = DealsIdlingResourceTestRule()

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse(DealsPDPGQLMockResponse())
        setupRestMockResponse(DealsPDPRestMockResponse())
        idlingResource = DealsPDPIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)

        val intent = Intent(context, DealsPDPActivity::class.java).apply {
            putExtra(DealsPDPActivity.EXTRA_PRODUCT_ID, "lalalala")
        }

        activityRule.launchActivity(intent)
    }

    @Test
    fun testPDPDeals() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, null))
        clickAllLocation()
        clickSeeMoreDesc()
        clickSeeMoreTnc()
        clickSeeMoreRedeem()
        clickRecommendation()
        clickBtnCheckout()

        Assert.assertThat(cassavaTestRule.validate(
            ANALYTIC_VALIDATOR_QUERY_DEALS_PRODUCT_DETAIL_PAGE), hasAllSuccess())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        Intents.release()
    }

    private fun clickAllLocation() {
        onView(withId(R.id.tg_see_all_locations)).perform(click())
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.seemorebutton_description)).perform(NestedScrollViewExtension())
    }

    private fun clickSeeMoreDesc() {
        onView(withId(R.id.seemorebutton_description)).perform(click())
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.seemorebutton_tnc)).perform(NestedScrollViewExtension())
    }

    private fun clickSeeMoreTnc() {
        onView(withId(R.id.seemorebutton_tnc)).perform(click())
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.see_how_to_redeem_voucher)).perform(NestedScrollViewExtension())
    }

    private fun clickSeeMoreRedeem() {
        onView(withId(R.id.see_how_to_redeem_voucher)).perform(click())
        onView(withId(R.id.recycler_view)).perform(NestedScrollViewExtension())
    }

    private fun clickRecommendation() {
        onView(withId(R.id.recycler_view)).perform(click())
    }

    private fun clickBtnCheckout() {
        onView(withId(R.id.btn_buynow)).perform(click())
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_PRODUCT_DETAIL_PAGE = "tracker/entertainment/deals/deals_pdp_tracking.json"
    }

    inner class NestedScrollViewExtension(scrolltoAction: ViewAction = ViewActions.scrollTo()) : ViewAction by scrolltoAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                ViewMatchers.isDescendantOfA(Matchers.anyOf(ViewMatchers.isAssignableFrom(NestedScrollView::class.java),
                    ViewMatchers.isAssignableFrom(ScrollView::class.java),
                    ViewMatchers.isAssignableFrom(HorizontalScrollView::class.java),
                    ViewMatchers.isAssignableFrom(ListView::class.java))))
        }
    }

}