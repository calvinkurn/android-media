package com.tokopedia.digital.home

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.digital.home.presentation.activity.DigitalHomePageActivity
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemCategoryAdapter
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemFavoriteAdapter
import com.tokopedia.digital.home.presentation.adapter.viewholder.*
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.ReminderWidgetViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DigitalHomepageInstrumentTest {

    @get:Rule
    var activityRule: IntentsTestRule<DigitalHomePageActivity> = object : IntentsTestRule<DigitalHomePageActivity>(DigitalHomePageActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(context, DigitalHomePageActivity::class.java).apply {
                putExtra(DigitalHomePageActivity.PARAM_PLATFORM_ID, PARAM_PLATFORM_ID_DEFAULT)
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(DigitalHomepageMockResponseConfig())
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()

//        setupGraphqlMockResponse(DigitalHomepageMockResponseConfig())

        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun verify_digital_homepage() {
        show_contents_digital_homepage()
        check_banner_section()
        check_favorite_section()
        check_category_section()
        check_trustmark_section()
//        check_reminder_section()
        check_dual_banners_section()
        check_lego_banners_section()
        check_product_cards_section()
        check_single_banner_section()
        check_product_banner_section()

//        ViewMatchers.assertThat(
//                getAnalyticsWithQuery(gtmLogDBSource, context, SUBHOME_ANALYTIC_VALIDATOR_QUERY),
//                hasAllSuccess()
//        )
    }

    private fun show_contents_digital_homepage() {
//        val context = InstrumentationRegistry.getInstrumentation().targetContext
//        activityRule.launchActivity(DigitalHomePageActivity.getCallingIntent(context, PARAM_PLATFORM_ID_DEFAULT))
//        val intent = Intent(context, DigitalHomePageActivity::class.java).apply {
//            putExtra(DigitalHomePageActivity.PARAM_PLATFORM_ID, PARAM_PLATFORM_ID_DEFAULT)
//        }
//        activityRule.launchActivity(intent)
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
        Thread.sleep(1000)
    }

    private fun check_banner_section() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<RechargeHomepageBannerViewHolder>(0,
                        CommonActions.clickChildViewWithId(R.id.see_all_promo)
        ))
        Thread.sleep(1000)
    }

    private fun check_favorite_section() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<RechargeHomepageFavoriteViewHolder>(1)
        )
        Thread.sleep(1000)
        val viewInteraction = onView(withId(R.id.rv_recharge_home_favorites)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<RechargeItemFavoriteAdapter.DigitalItemFavoriteViewHolder>(
                0, click())
        )
    }

    private fun check_category_section() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<RechargeHomepageCategoryViewHolder>(2)
        )
        Thread.sleep(1000)
        val viewInteraction = onView(withId(R.id.rv_recharge_home_category)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<RechargeItemCategoryAdapter.DigitalItemSubmenuCategoryViewHolder>(
                0, click())
        )
    }

    private fun check_trustmark_section() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<RechargeHomepageTrustMarkViewHolder>(3)
        )
        Thread.sleep(1000)
        onView(withId(R.id.rv_recharge_home_trust_mark)).check(matches(isDisplayed()))
    }

    private fun check_reminder_section() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<ReminderWidgetViewHolder>(4)
        )
        Thread.sleep(1000)
        onView(withId(R.id.reminder_recommendation_widget_container)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_reminder_recommendation)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.ic_close_reminder_recommendation)).perform(click())
        Thread.sleep(1000)
    }

    private fun check_dual_banners_section() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<RechargeHomepageDualBannersViewHolder>(4)
        )
        Thread.sleep(1000)
        onView(withId(R.id.view_recharge_home_dual_banners_container)).check(matches(isDisplayed()))
        onView(withId(R.id.view_recharge_home_dual_banners_image_1)).perform(click())
        Thread.sleep(1000)
    }

    private fun check_lego_banners_section() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<DynamicLegoBannerViewHolder>(5)
        )
        Thread.sleep(1000)
        val viewInteraction = onView(withId(R.id.recycleList)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<RechargeItemCategoryAdapter.DigitalItemSubmenuCategoryViewHolder>(
                0, click())
        )
        Thread.sleep(1000)
    }

    private fun check_product_cards_section() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<RechargeHomepageProductCardsViewHolder>(6)
        )
        Thread.sleep(1000)
        val viewInteraction = onView(withId(R.id.rv_recharge_home_product_cards)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<RechargeItemCategoryAdapter.DigitalItemSubmenuCategoryViewHolder>(
                0, click())
        )
        Thread.sleep(1000)
    }

    private fun check_single_banner_section() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<RechargeHomepageSingleBannerViewHolder>(7)
        )
        Thread.sleep(1000)
        onView(withId(R.id.view_recharge_home_single_banner_container)).check(matches(isDisplayed()))
        onView(withId(R.id.view_recharge_home_single_banner_image)).perform(click())
        Thread.sleep(1000)
    }

    private fun check_product_banner_section() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.scrollToPosition<RechargeHomepageSingleBannerViewHolder>(8)
        )
        Thread.sleep(1000)
        onView(withId(R.id.view_recharge_home_product_banner_product_card)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_recharge_home_product_banner_buy)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.iv_recharge_home_product_banner_close_button)).perform(click())
        Thread.sleep(1000)
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        const val PARAM_PLATFORM_ID_DEFAULT = "31"
        private const val SUBHOME_ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_subhomepage.json"
    }
}