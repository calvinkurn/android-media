package com.tokopedia.digital.home

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.digital.home.presentation.activity.RechargeHomepageActivity
import com.tokopedia.digital.home.presentation.adapter.RechargeItemCategoryAdapter
import com.tokopedia.digital.home.presentation.adapter.RechargeItemFavoriteAdapter
import com.tokopedia.digital.home.presentation.adapter.RechargeItemProductCardsAdapter
import com.tokopedia.digital.home.presentation.adapter.viewholder.*
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.ReminderWidgetViewHolder
import com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder.DigitalUnifyCardViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.banner.R as bannerR
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.unifycomponents.R as unifycomponentsR

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class RechargeHomepageInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    @get:Rule
    var activityRule: IntentsTestRule<RechargeHomepageActivity> = object : IntentsTestRule<RechargeHomepageActivity>(RechargeHomepageActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(context, RechargeHomepageActivity::class.java).apply {
                putExtra(RechargeHomepageActivity.PARAM_PLATFORM_ID, PARAM_PLATFORM_ID_DEFAULT)
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(RechargeHomepageMockResponseConfig())
        }
    }

    @Test
    fun verify_digital_homepage() {
        show_contents_digital_homepage()
        check_search_category()

        Thread.sleep(2000)
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        check_banner_section()
        check_favorite_section()
        check_category_section()
        check_trustmark_section()
        check_dual_banners_section()
        check_lego_banners_section()
        check_swipe_banner_section()
        check_product_cards_section()
        check_single_banner_section()
        check_product_banner_section()
        check_reminder_section()
        check_3_icons_section()
        check_product_card_custom_banner_section()

        assertThat(cassavaTestRule.validate(SUBHOME_ANALYTIC_VALIDATOR_QUERY), hasAllSuccess())
    }

    private fun show_contents_digital_homepage() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    private fun check_search_category() {
        onView(withId(R.id.digital_homepage_search_view)).check(matches(isDisplayed()))
            .perform(click())
        Thread.sleep(1000)
        onView(withId(unifycomponentsR.id.searchbar_textfield)).check(matches(isDisplayed())).perform(typeText("pulsa"))
        Thread.sleep(1000)
        val viewInteraction = onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<DigitalHomePageSearchViewHolder>(
                0,
                click()
            )
        )
    }

    private fun check_banner_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RechargeHomepageBannerViewHolder>(
                0,
                CommonActions.clickChildViewWithId(R.id.recharge_image_banner_homepage)
            )
        )
        Thread.sleep(1000)
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RechargeHomepageBannerViewHolder>(
                0,
                CommonActions.clickChildViewWithId(R.id.see_all_promo)
            )
        )
        Thread.sleep(1000)
    }

    private fun check_favorite_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RechargeHomepageFavoriteViewHolder>(1)
        )
        Thread.sleep(1000)
        val viewInteraction = onView(withId(R.id.rv_recharge_home_favorites)).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RechargeItemFavoriteAdapter.DigitalItemFavoriteViewHolder>(
                0,
                click()
            )
        )
    }

    private fun check_category_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RechargeHomepageCategoryViewHolder>(2)
        )
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(withId(R.id.rv_recharge_home_category), hasSibling(withText("Prabayar & Pascabayar")))).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RechargeItemCategoryAdapter.DigitalItemSubmenuCategoryViewHolder>(
                0,
                click()
            )
        )
        Thread.sleep(1000)
    }

    private fun check_trustmark_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RechargeHomepageDualIconsViewHolder>(7)
        )
        Thread.sleep(1000)
        val viewInteraction = onView(withId(R.id.rv_recharge_home_dual_icons)).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RechargeHomepageDualIconsViewHolder>(
                0,
                click()
            )
        )
        Thread.sleep(1000)
    }

    private fun check_reminder_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<ReminderWidgetViewHolder>(8)
        )
        Thread.sleep(1000)
        onView(withId(home_componentR.id.reminder_recommendation_widget_container)).check(matches(isDisplayed()))
        onView(withId(home_componentR.id.btn_reminder_recommendation)).perform(click())
        Thread.sleep(1000)
    }

    private fun check_dual_banners_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RechargeHomepageDualBannersViewHolder>(9)
        )
        Thread.sleep(3000)
        onView(withId(R.id.view_recharge_home_dual_banners_container)).check(matches(isDisplayed()))
        onView(withId(R.id.view_recharge_home_dual_banners_image_1)).check(matches(isDisplayed())).perform(click())
        Thread.sleep(1000)
    }

    private fun check_lego_banners_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<DynamicLegoBannerViewHolder>(10)
        )
        Thread.sleep(1000)
        val viewInteraction = onView(withId(R.id.recycleList)).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<DynamicLegoBannerViewHolder.LegoItemViewHolder>(
                0,
                click()
            )
        )
        Thread.sleep(1000)
    }

    private fun check_product_cards_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RechargeHomepageProductCardsViewHolder>(11)
        )
        Thread.sleep(1000)
        val viewInteraction = onView(withId(R.id.rv_recharge_home_product_cards)).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RechargeItemProductCardsAdapter.RechargeItemProductCardViewHolder>(
                0,
                click()
            )
        )
        Thread.sleep(1000)
    }

    private fun check_single_banner_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RechargeHomepageSingleBannerViewHolder>(14)
        )
        Thread.sleep(1000)
        onView(withId(R.id.view_recharge_home_single_banner_container)).check(matches(isDisplayed()))
        onView(withId(R.id.view_recharge_home_single_banner_image)).perform(click())
        Thread.sleep(1000)
    }

    private fun check_product_banner_section() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RechargeHomepageProductBannerViewHolder>(15)
        )
        Thread.sleep(1000)
        onView(withId(R.id.view_recharge_home_product_banner_product_card)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_recharge_home_product_banner_buy)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.iv_recharge_home_product_banner_close_button)).perform(click())
        Thread.sleep(1000)
    }

    private fun check_swipe_banner_section() {
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RechargeHomepageSwipeBannerViewHolder>(14)
        )
        Thread.sleep(2000)
        onView(withId(bannerR.id.banner_recyclerview)).check(matches(isDisplayed()))
        onView(withId(bannerR.id.banner_recyclerview)).perform(
            RecyclerViewActions
                .actionOnItemAtPosition<BannerViewPagerAdapter.BannerViewHolder>(0, click())
        )
    }

    private fun check_3_icons_section() {
        Thread.sleep(1000)
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RechargeHomepageThreeIconsViewHolder>(15)
        )
        onView(withId(R.id.rv_three_icons)).check(matches(isDisplayed()))
    }

    private fun check_product_card_custom_banner_section() {
        Thread.sleep(1000)
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollTo<RechargeHomepageProductCardCustomBannerV2ViewHolder>(
                hasDescendant(withText("Dummy Channel"))
            )
        )
        onView(withId(R.id.rv_recharge_product)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_recharge_product)).perform(
            RecyclerViewActions.scrollToPosition<DigitalUnifyCardViewHolder>(3)
        )
    }

    companion object {
        const val PARAM_PLATFORM_ID_DEFAULT = "31"
        private const val SUBHOME_ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_subhomepage.json"
    }
}
