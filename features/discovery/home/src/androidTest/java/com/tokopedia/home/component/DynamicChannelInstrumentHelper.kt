package com.tokopedia.home.component
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.tabs.TabLayout
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetView
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceAdapter
import com.tokopedia.home.beranda.presentation.view.helper.*
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselEmptyCardViewHolder
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeBUWidgetMixLeftViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.espresso_component.CommonMatcher
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.AllOf

const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_BANNER = "tracker/home/hpb.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_SCREEN = "tracker/home/homescreen.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TICKER = "tracker/home/ticker.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LIST_CAROUSEL = "tracker/home/list_carousel.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LEGO_BANNER = "tracker/home/lego_banner.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_POPULAR_KEYWORD = "tracker/home/popular_keyword.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_PRODUCT_HIGHLIGHT = "tracker/home/product_highlight.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_CATEGORY_WIDGET = "tracker/home/category_widget.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BU_WIDGET = "tracker/home/bu_widget.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT = "tracker/home/mix_left.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP = "tracker/home/mix_top.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_TAB = "tracker/home/recommendation_tab.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_BANNER = "tracker/home/recom_feed_banner.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_PRODUCT_LOGIN = "tracker/home/recom_feed_product_login.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_PRODUCT_NONLOGIN = "tracker/home/recom_feed_product_nonlogin.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_ICON = "tracker/home/recommendation_icon.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECHARGE_BU_WIDGET = "tracker/home/recharge_bu_widget.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_RECHARGE_CLOSE = "tracker/home/reminder_widget_recharge_close.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_SALAM_CLOSE = "tracker/home/reminder_widget_salam_close.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_RECHARGE = "tracker/home/reminder_widget_recharge.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REMINDER_WIDGET_SALAM = "tracker/home/reminder_widget_salam.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BANNER_CAROUSEL = "tracker/home/banner_carousel.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MERCHANT_VOUCHER = "tracker/home/merchant_voucher_widget.json"

private const val CHOOSE_ADDRESS_PREFERENCE_NAME = "coahmark_choose_address"
private const val CHOOSE_ADDRESS_EXTRA_IS_COACHMARK = "EXTRA_IS_COACHMARK"

/**
 * Created by yfsx on 2/9/21.
 */

fun disableCoachMark(context: Context){
    disableHomeAnimation()
    disableChooseAddressCoachmark(context)
    setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK, true)
    setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_INBOX, true)
    setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_CHOOSEADDRESS, true)
    setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_BALANCE, true)
    setCoachmarkSharedPrefValue(context, PREF_KEY_NEW_WALLETAPP_COACHMARK_BALANCE, true)
    setCoachmarkSharedPrefValue(context, PREF_KEY_NEW_TOKOPOINT_COACHMARK_BALANCE, true)
    setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_TOKONOW_COACHMARK, true)
}

fun enableCoachMark(context: Context){
    disableHomeAnimation()
    enableChooseAddressCoachmark(context)
    setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK, false)
    setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_INBOX, false)
    setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_CHOOSEADDRESS, false)
    setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_BALANCE, false)
    setCoachmarkSharedPrefValue(context, PREF_KEY_WALLETAPP_COACHMARK_BALANCE, false)
    setCoachmarkSharedPrefValue(context, PREF_KEY_WALLETAPP2_COACHMARK_BALANCE, false)
    setCoachmarkSharedPrefValue(context, PREF_KEY_NEW_WALLETAPP_COACHMARK_BALANCE, false)
    setCoachmarkSharedPrefValue(context, PREF_KEY_NEW_TOKOPOINT_COACHMARK_BALANCE, false)
    setHomeTokonowCoachmarkSharedPrefValue(context, false)
}

fun disableHomeAnimation() {
    BalanceWidgetView.disableAnimation = true
    BalanceAdapter.disableAnimation = true
}

fun setCoachmarkSharedPrefValue(context: Context, key: String, value: Boolean) {
    val sharedPrefs = context.getSharedPreferences(PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(key, value).apply()
}

fun setHomeTokonowCoachmarkSharedPrefValue(context: Context, value: Boolean) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_TOKONOW_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_HOME_TOKONOW_COACHMARK, value).apply()
}

fun disableChooseAddressCoachmark(context: Context) {
    val sharedPrefs = context.getSharedPreferences(CHOOSE_ADDRESS_PREFERENCE_NAME, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(
            CHOOSE_ADDRESS_EXTRA_IS_COACHMARK, false).apply()
}

fun enableChooseAddressCoachmark(context: Context) {
    val sharedPrefs = context.getSharedPreferences(CHOOSE_ADDRESS_PREFERENCE_NAME, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(
        CHOOSE_ADDRESS_EXTRA_IS_COACHMARK, true).apply()
}

fun waitForData() {
    Thread.sleep(4000)
}
fun waitForPopularKeywordData() {
    Thread.sleep(4000)
}

fun waitForLoadCassavaAssert() {
    Thread.sleep(2000)
}

fun addDebugEnd() {
    Thread.sleep(2000)
}

fun String.name(loggedIn: Boolean, darkMode: Boolean = false) = this + (if (loggedIn) "-login" else "-nonlogin") + (if (darkMode) "-dark" else "-light")


//==================================== item action =============================================

fun clickOnProductHighlightItem() {
    try {
        Espresso.onView(firstView(ViewMatchers.withId(R.id.master_product_card_deals)))
                .perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

fun clickOnPopularKeywordSection(viewHolder: RecyclerView.ViewHolder) {
    waitForPopularKeywordData()
    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_popular_keyword, 0)
    clickLihatSemuaPopularKeyword()
}

fun clickOnMixLeftSection(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickLihatSemuaButtonIfAvailable(viewHolder.itemView, itemPosition)
    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_product, 0)
}

fun clickOnMixTopSection(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickLihatSemuaButtonIfAvailable(viewHolder.itemView, itemPosition)
    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
    clickOnMixTopCTA(viewHolder.itemView)
}

fun clickOnLegoBannerSection(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickLihatSemuaButtonIfAvailable(viewHolder.itemView, itemPosition)
    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycleList, 0)
    clickSingleItemOnRecyclerView(R.id.recycleList)
}

fun clickOnRecommendationFeedSection(viewHolder: RecyclerView.ViewHolder) {
    waitForData()
    clickRecommendationFeedTab()
    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.home_feed_fragment_recycler_view, 0)
}

fun clickCloseOnReminderWidget(viewHolder: RecyclerView.ViewHolder, itemPosition: Int, homeRecyclerView: RecyclerView){
    waitForData()
    val adapter = (homeRecyclerView.adapter as HomeRecycleAdapter)
    val reminderWidgetModel = adapter.currentList.get(itemPosition)
    val reminderModel = reminderWidgetModel as ReminderWidgetModel
    if(reminderModel.source.equals(ReminderEnum.SALAM)) {
        clickClosedReminderWidgetSalam()
    } else {
        clickClosedReminderWidgetRecharge()
    }
}

fun clickOnReminderWidget(viewHolder: RecyclerView.ViewHolder, itemPosition: Int, homeRecyclerView: RecyclerView){
    waitForData()
    val adapter = (homeRecyclerView.adapter as HomeRecycleAdapter)
    val reminderWidgetModel = adapter.currentList.get(itemPosition)
    val reminderModel = reminderWidgetModel as ReminderWidgetModel
    if(reminderModel.source.equals(ReminderEnum.SALAM)) {
        clickReminderWidgetSalam()
    } else {
        clickReminderWidgetRecharge(itemPosition)
    }
}

fun clickOnCategoryWidgetSection(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickLihatSemuaButtonIfAvailable(viewHolder.itemView, itemPosition)
    clickSingleItemOnRecyclerView(R.id.recycleList)
}

fun clickOnBusinessWidgetSection(viewHolder: RecyclerView.ViewHolder) {
    clickBUWidgetTab()
    clickSingleItemOnRecyclerView(R.id.recycler_view)
}

fun clickOnTickerSection(viewHolder: RecyclerView.ViewHolder) {
    clickTickerItem(viewHolder.itemView)
}

fun clickHPBSection(viewHolder: RecyclerView.ViewHolder) {
    clickHomeBannerItemAndViewAll(viewHolder)
}

fun actionOnBannerCarouselWidget(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickLihatSemuaButtonIfAvailable(viewHolder.itemView, itemPosition)
    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_banner, 0)
}

fun actionOnMerchantVoucherWidget(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickOnEachItemRecyclerViewMerchantVoucher(viewHolder.itemView, R.id.rv_banner, 0)
    clickLihatSemuaButtonIfAvailable(viewHolder.itemView, itemPosition)
}

fun clickOnEachItemRecyclerViewMerchantVoucher(view: View, recyclerViewId: Int, fixedItemPositionLimit: Int) {
    val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)

    var childItemCountExcludeViewAllCard = (childRecyclerView.adapter?.itemCount?: 0) - 1
    if (fixedItemPositionLimit > 0) {
        childItemCountExcludeViewAllCard = fixedItemPositionLimit
    }
    for (i in 0 until childItemCountExcludeViewAllCard) {
        try {
            Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(i, clickOnViewChild(R.id.container_shop))
            )
            Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(i, clickOnViewChild(R.id.container_product))
            )
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }
}

fun checkRechargeBUWidget(viewHolder: RecyclerView.ViewHolder, itemPosition: Int){
    clickEmptyBannerRechargeBUWidget()
    impressionRechargeBUWidget()
    clickProductRechargeBUWidget()
    clickAllProductCardRechargeBUWidget()
    clickSeeAllRechargeBUWidget(viewHolder, itemPosition)
}

private fun impressionRechargeBUWidget() {
    waitForData()
    try {
    Espresso.onView(ViewMatchers.withId(R.id.rv_recharge_bu_product))
            .perform(RecyclerViewActions.scrollToPosition<RechargeBUWidgetMixLeftViewHolder>(5))
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickProductRechargeBUWidget(){
    waitForData()
    try {
        Espresso.onView(ViewMatchers.withId(R.id.rv_recharge_bu_product))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RechargeBUWidgetMixLeftViewHolder>(4, ViewActions.click()))
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickAllProductCardRechargeBUWidget(){
    waitForData()
    try {
        Espresso.onView(ViewMatchers.withId(R.id.rv_recharge_bu_product)).perform(scrollToPosition<RechargeBUWidgetMixLeftViewHolder>(5))
        Espresso.onView(ViewMatchers.withId(R.id.card_see_more_banner_mix))
            .perform(ViewActions.click());
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickEmptyBannerRechargeBUWidget(){
    waitForData()
    try {
        Espresso.onView(ViewMatchers.withId(R.id.rv_recharge_bu_product))
                .perform(RecyclerViewActions.actionOnItemAtPosition<CarouselEmptyCardViewHolder>(0, ViewActions.click()))
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickSeeAllRechargeBUWidget(viewHolder: RecyclerView.ViewHolder, itemPosition: Int){
    waitForData()
    try {
        Espresso.onView(allOf(ViewMatchers.withId(R.id.see_all_button),
                ViewMatchers.hasSibling(ViewMatchers.withText("Produk digital khusus kamu")))).perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickRecommendationFeedTab() {
    try {
        Espresso.onView(ViewMatchers.withId(R.id.tab_layout_home_feeds)).perform(selectTabAtPosition(0))
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickOnMixTopCTA(view: View) {
    val childView = view
    val bannerButton = childView.findViewById<View>(R.id.banner_button)
    if (bannerButton.visibility == View.VISIBLE) {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.banner_button)))
                    .perform(ViewActions.click())
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }
}

private fun clickLihatSemuaPopularKeyword() {
    try {
        Espresso.onView(firstView(ViewMatchers.withId(R.id.tv_reload)))
                .perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickBUWidgetTab() {
    try {
        Espresso.onView(ViewMatchers.withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickTickerItem(view: View) {
    try {
        Espresso.onView(firstView(AllOf.allOf(ViewMatchers.withId(R.id.ticker_description), ViewMatchers.isDisplayed()))).perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }

    try {
        Espresso.onView(firstView(ViewMatchers.withId(R.id.ticker_close_icon)))
                .perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickHomeBannerItemAndViewAll(viewHolder: RecyclerView.ViewHolder) {
    val view = viewHolder.itemView
    val childView = view
    val seeAllButton = childView.findViewById<View>(R.id.see_more_label)

    //banner item click
    val bannerViewPager = childView.findViewById<CircularViewPager>(R.id.circular_view_pager)
    val itemCount = bannerViewPager.getViewPager().adapter?.itemCount ?: 0
    bannerViewPager.pauseAutoScroll()
    try {
        Espresso.onView(firstView(ViewMatchers.withId(R.id.circular_view_pager)))
                .perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
    //see all promo button click
    if (seeAllButton.visibility == View.VISIBLE) {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.see_more_label)))
                    .perform(ViewActions.click())
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }
}

private fun clickLihatSemuaButtonIfAvailable(view: View, itemPos: Int) {
    val childView = view
    val seeAllButton = childView.findViewById<View>(R.id.see_all_button)
    if (seeAllButton.visibility == View.VISIBLE) {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view))
                    .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(itemPos, clickOnViewChild(R.id.see_all_button)))
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }
}

private fun clickSingleItemOnRecyclerView(recyclerViewId: Int) {
    try {
        Espresso.onView(firstView(ViewMatchers.withId(recyclerViewId)))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickClosedReminderWidgetSalam(){
    try {
        Espresso.onView(CommonMatcher.getElementFromMatchAtPosition(AllOf.allOf(ViewMatchers.withId(R.id.ic_close_reminder_recommendation),
                ViewMatchers.isDisplayed()),0)).perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickClosedReminderWidgetRecharge(){
    try {
        Espresso.onView(CommonMatcher.getElementFromMatchAtPosition(AllOf.allOf(ViewMatchers.withId(R.id.ic_close_reminder_recommendation),
                ViewMatchers.isDisplayed()),0)).perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickReminderWidgetSalam(){
    try {
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.btn_reminder_recommendation), ViewMatchers.isDisplayed(),
                ViewMatchers.withText("Berbagi Sekarang"))).perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

private fun clickReminderWidgetRecharge(i:Int){
    try {
        Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(i-1))
        Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(i))
        Espresso.onView(CommonMatcher.getElementFromMatchAtPosition(AllOf.allOf(ViewMatchers.withId(R.id.btn_reminder_recommendation), ViewMatchers.isDisplayed(),
                ViewMatchers.withText("Bayar Sekarang")),0)).perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

//==================================== end of item action ======================================


//==================================== cassava validator =======================================

//
//fun getAssertBUWiddet(context: Context) {
//    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BU_WIDGET),
//            hasAllSuccess())
////    -> impression tab intermitten missing
//}
//
//fun getAssertHPB(context: Context) {
//    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_BANNER),
//            hasAllSuccess())
////    -> impression missing
//}
//
//fun getAssertListCarousel(context: Context) {
//    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LIST_CAROUSEL),
//            hasAllSuccess())
////    -> cant mock occ response
//}
//
//fun getAssertRecommendationIcon(context: Context) {
//    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_ICON),
//            hasAllSuccess())
////    -> missing click
//}
//
//fun getAssertRecommendationFeedTab(context: Context) {
//    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_TAB),
//            hasAllSuccess())
//}
//==================================== end of cassava validator ================================

private fun <T> firstView(matcher: Matcher<T>): Matcher<T>? {
    return object : BaseMatcher<T>() {
        var isFirst = true
        override fun matches(item: Any?): Boolean {
            if (isFirst && matcher.matches(item)) {
                isFirst = false
                return true
            }
            return false
        }

        override fun describeTo(description: Description) {
            description.appendText("should return first matching item")
        }
    }
}

private fun clickOnViewChild(viewId: Int) = object: ViewAction {
    override fun getDescription(): String  = ""

    override fun getConstraints() = null

    override fun perform(uiController: UiController, view: View)
            = ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
}

private fun selectTabAtPosition(tabIndex: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription() = "with tab at index $tabIndex"

        override fun getConstraints() = AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(TabLayout::class.java))

        override fun perform(uiController: UiController, view: View) {
            val tabLayout = view as TabLayout
            val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                            .withCause(Throwable("No tab at index $tabIndex"))
                            .build()

            tabAtIndex.select()
        }
    }
}