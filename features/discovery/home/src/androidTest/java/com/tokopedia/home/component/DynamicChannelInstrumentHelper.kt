package com.tokopedia.home.component
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.home.R
import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat

private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_BANNER = "tracker/home/hpb.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_SCREEN = "tracker/home/homescreen.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TICKER = "tracker/home/ticker.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LIST_CAROUSEL = "tracker/home/list_carousel.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LEGO_BANNER = "tracker/home/lego_banner.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_POPULAR_KEYWORD = "tracker/home/popular_keyword.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_PRODUCT_HIGHLIGHT = "tracker/home/product_highlight.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_CATEGORY_WIDGET = "tracker/home/category_widget.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BU_WIDGET = "tracker/home/bu_widget.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT = "tracker/home/mix_left.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP = "tracker/home/mix_top.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_TAB = "tracker/home/recommendation_tab.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_BANNER = "tracker/home/recom_feed_banner.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_PRODUCT_LOGIN = "tracker/home/recom_feed_product_login.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_PRODUCT_NONLOGIN = "tracker/home/recom_feed_product_nonlogin.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_ICON = "tracker/home/recommendation_icon.json"
/**
 * Created by yfsx on 2/9/21.
 */

fun disableCoachMark(context: Context){
    val sharedPrefs = context.getSharedPreferences(NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(
            NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false).apply()
}

fun waitForData() {
    Thread.sleep(3000)
}

fun waitForLoadCassavaAssert() {
    Thread.sleep(2000)
}

fun addDebugEnd() {
    Thread.sleep(2000)
}


//==================================== item action =============================================

fun clickOnProductHighlightItem() {
    try {
        Espresso.onView(firstView(ViewMatchers.withId(R.id.deals_product_card)))
                .perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

fun clickOnPopularKeywordSection(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickLihatSemuaPopularKeyword()
    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_popular_keyword, itemPosition)
}

private fun clickLihatSemuaPopularKeyword() {
    try {
        Espresso.onView(firstView(ViewMatchers.withId(R.id.tv_reload)))
                .perform(ViewActions.click())
    } catch (e: PerformException) {
        e.printStackTrace()
    }
}

fun clickOnMixLeftSection(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickLihatSemuaButtonIfAvailable(viewHolder.itemView, itemPosition)
    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_product, itemPosition)
}

fun clickOnMixTopSection(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickLihatSemuaButtonIfAvailable(viewHolder.itemView, itemPosition)
    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, itemPosition)
    clickOnMixTopCTA(viewHolder.itemView)
}

fun clickOnLegoBannerSection(viewHolder: RecyclerView.ViewHolder, itemPosition: Int) {
    clickLihatSemuaButtonIfAvailable(viewHolder.itemView, itemPosition)
    clickSingleItemOnRecyclerView(R.id.recycleList)
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


private fun clickLihatSemuaButtonIfAvailable(view: View, itemPos: Int) {
    val childView = view
    val seeAllButton = childView.findViewById<View>(R.id.see_all_button)
    if (seeAllButton.visibility == View.VISIBLE) {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemPos, clickOnViewChild(R.id.see_all_button)))
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

//==================================== end of item action ======================================


//==================================== cassava validator =======================================

fun getAssertCategoryWidget(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_CATEGORY_WIDGET),
            hasAllSuccess())
//    -> impression intermitten missing
}

fun getAssertRecommendationFeedTab(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_TAB),
            hasAllSuccess())
}

fun getAssertBUWiddet(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_BU_WIDGET),
            hasAllSuccess())
//    -> impression tab intermitten missing
}

fun getAssertTicker(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TICKER),
            hasAllSuccess())
//    -> impression intermitten missing
}

fun getAssertHomepageScreen(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_SCREEN),
            hasAllSuccess())
//    -> completely missing
}

fun getAssertHPB(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_HOMEPAGE_BANNER),
            hasAllSuccess())
//    -> impression missing
}

fun getAssertListCarousel(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LIST_CAROUSEL),
            hasAllSuccess())
//    -> cant mock occ response
}

fun getAssertRecommendationIcon(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_ICON),
            hasAllSuccess())
//    -> missing click
}

fun getAssertProductHighlight(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_PRODUCT_HIGHLIGHT),
            hasAllSuccess())
}

fun getAssertPopularKeyword(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_POPULAR_KEYWORD),
            hasAllSuccess())
}


fun getAssertMixLeft(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_LEFT),
            hasAllSuccess())
}

fun getAssertMixTop(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MIX_TOP),
            hasAllSuccess())
}

fun getAssertLegoBanner(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_LEGO_BANNER),
            hasAllSuccess())
}

fun getAssertRecommendationFeedBanner(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_BANNER),
            hasAllSuccess())
}

fun getAssertRecommendationFeedProductNonLogin(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_PRODUCT_NONLOGIN),
            hasAllSuccess())
}

fun getAssertRecommendationFeedProductLogin(gtmLogDBSource: GtmLogDBSource, context: Context) {
    assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_RECOMMENDATION_FEED_PRODUCT_LOGIN),
            hasAllSuccess())
}

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