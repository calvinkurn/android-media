package com.tokopedia.shop.analyticvalidator.testcase

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_RESULT_CODE_WISHLIST
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTION_RESULT_PRODUCT
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.shop.R
import com.tokopedia.shop.analyticvalidator.util.ShopUiTestUtil
import com.tokopedia.shop.analyticvalidator.util.ViewActionUtil
import com.tokopedia.shop.analyticvalidator.util.ViewActionUtil.clickTabLayoutPosition
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.mock.ShopPageMockResponseConfig
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity.Companion.SHOP_ID
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ShopPageBuyerAnalyticTest {

    companion object {
        private const val SHOP_PAGE_CLICK_TABS_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_click_tabs_tracker.json"
        private const val SHOP_PAGE_PRODUCT_TAB_CLICK_SORT_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_product_tab_click_sort_tracker.json"
        private const val SHOP_PAGE_PRODUCT_TAB_CLICK_ETALASE_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_product_tab_click_etalase_tracker.json"
        private const val SHOP_PAGE_PRODUCT_TAB_PRODUCT_CARD_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_product_tab_product_card_tracker.json"
        private const val SHOP_PAGE_HOME_TAB_DISPLAY_WIDGET_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_home_tab_display_widget_tracker.json"
        private const val SHOP_PAGE_HOME_TAB_FEATURED_PRODUCT_WIDGET_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_home_tab_featured_product_widget_tracker.json"
        private const val SHOP_PAGE_HOME_TAB_NPL_WIDGET_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_home_tab_npl_widget_tracker.json"

    }

    @get:Rule
    var activityRule: IntentsTestRule<ShopPageActivity> = IntentsTestRule(ShopPageActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    private val SAMPLE_SHOP_ID = "3418893"

    @Before
    fun beforeTest() {
        setupGraphqlMockResponse(ShopPageMockResponseConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        val intent = Intent().apply {
            putExtra(SHOP_ID, SAMPLE_SHOP_ID)
        }
        activityRule.launchActivity(intent)
    }

    @Test
    fun testShopHeaderJourney() {
        testHeader()
        validateTrackerShopHeaderJourney()
    }

    @Test
    fun testShopProductTabJourney() {
        testProductTab()
        validateTrackerShopProductTabJourney()
    }

    //TODO temporary fix, will be updated later with proper fix
//    @Test
//    fun testShopHomeTabJourney() {
//        testHomeTab()
//        validateTrackerShopHomeTabJourney()
//    }

    private fun validateTrackerShopHeaderJourney() {
        activityRule.activity.finish()
        doAnalyticDebuggerTest(SHOP_PAGE_CLICK_TABS_TRACKER_MATCHER_PATH)
    }

    private fun validateTrackerShopProductTabJourney() {
        activityRule.activity.finish()
        doAnalyticDebuggerTest(SHOP_PAGE_PRODUCT_TAB_CLICK_SORT_TRACKER_MATCHER_PATH)
        doAnalyticDebuggerTest(SHOP_PAGE_PRODUCT_TAB_CLICK_ETALASE_TRACKER_MATCHER_PATH)
        doAnalyticDebuggerTest(SHOP_PAGE_PRODUCT_TAB_PRODUCT_CARD_TRACKER_MATCHER_PATH)
    }

    private fun validateTrackerShopHomeTabJourney() {
        activityRule.activity.finish()
        doAnalyticDebuggerTest(SHOP_PAGE_HOME_TAB_DISPLAY_WIDGET_TRACKER_MATCHER_PATH)
        doAnalyticDebuggerTest(SHOP_PAGE_HOME_TAB_FEATURED_PRODUCT_WIDGET_TRACKER_MATCHER_PATH)
        doAnalyticDebuggerTest(SHOP_PAGE_HOME_TAB_NPL_WIDGET_TRACKER_MATCHER_PATH)
    }

    private fun testHomeTab() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(CommonActions.selectTabLayoutPosition(0))
        testDisplayWidget()
        testProductWidget()
        testNplWidget()
    }

    private fun testNplWidget() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        val playWidgetPosition = 2
        Espresso.onView(firstView(AllOf.allOf(
                withId(R.id.recycler_view),
                isDisplayed())
        )).perform(ShopUiTestUtil.rvScrollToPositionWithOffset(playWidgetPosition))
        Espresso.onView(firstView(AllOf.allOf(
                withId(R.id.rv_product_carousel))
        )).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,  click()))
        Espresso.onView(firstView(AllOf.allOf(
                withId(R.id.text_see_all))
        )).perform(click())
        Espresso.onView(firstView(AllOf.allOf(
                withText("Tutup"))
        )).perform(click())
        Espresso.onView(firstView(AllOf.allOf(
                withId(R.id.layout_remind_me_un_notified))
        )).perform(click())
        Espresso.onView(firstView(AllOf.allOf(
                withId(R.id.snackbar_btn))
        )).perform(click())
        Espresso.onView(firstView(AllOf.allOf(
                withId(R.id.image_tnc))
        )).perform(click())
        Espresso.onView(firstView(AllOf.allOf(
                withId(R.id.bottom_sheet_close))
        )).perform(click())
    }

    private fun testProductWidget() {
        val productWidgetPosition = 1
        val recyclerViewHomeWidgetInteraction = Espresso.onView(firstView(AllOf.allOf(
                withId(R.id.recycler_view),
                isDisplayed())
        ))
        recyclerViewHomeWidgetInteraction.perform(ShopUiTestUtil.rvScrollToPositionWithOffset(productWidgetPosition))

        Espresso.onView(AllOf.allOf(
                withId(R.id.carouselProductCardRecyclerView))
        ).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Espresso.onView(AllOf.allOf(
                withId(R.id.carouselProductCardRecyclerView))
        ).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, CommonActions.clickChildViewWithId(R.id.buttonAddToCart)))
        Espresso.onView(firstView(AllOf.allOf(
                withId(R.id.tvSeeAll),
                isDisplayed()))
        ).perform(click())
    }

    private fun testDisplayWidget() {
        ViewActionUtil.waitUntilViewIsDisplayed((AllOf.allOf(
                withId(R.id.rvShopHomeMultiple)
        )))
        Espresso.onView(AllOf.allOf(
                withId(R.id.rvShopHomeMultiple),
                isDisplayed())
        ).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    private fun testHeader() {
        ViewActionUtil.waitUntilViewIsDisplayed((AllOf.allOf(
                withId(R.id.tabLayout)
        )))
        testClickTabs()
    }

    private fun testProductTab() {
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(CommonActions.selectTabLayoutPosition(1))
        ViewActionUtil.waitUntilViewIsDisplayed((AllOf.allOf(
                withText("Urutkan"),
                isDescendantOfA(withId(R.id.sort_filter_items_wrapper))
        )))
        ViewActionUtil.waitUntilViewIsDisplayed((AllOf.allOf(
                withText("Etalase Toko"),
                isDescendantOfA(withId(R.id.sort_filter_items_wrapper))
        )))
        testSelectSortOption()
        testClickEtalase()
        testProductCard()
    }

    private fun testProductCard() {
        val sampleProductIdWishlist = "23151232"
        val clickedItemPosition = 2
        val mockIntentData = Intent().apply {
            putExtra(PRODUCT_CARD_OPTION_RESULT_PRODUCT, ProductCardOptionsModel(
                    isWishlisted = true,
                    productId = sampleProductIdWishlist
            ).apply {
                wishlistResult = ProductCardOptionsModel.WishlistResult(
                        isUserLoggedIn = true,
                        isAddWishlist = true,
                        isSuccess = true
                )
            })
        }
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(PRODUCT_CARD_OPTIONS_RESULT_CODE_WISHLIST, mockIntentData))
        Espresso.onView(AllOf.allOf(
                withId(R.id.recycler_view),
                isDisplayed())
        ).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(clickedItemPosition, click()))
        Espresso.onView(AllOf.allOf(
                withId(R.id.recycler_view),
                isDisplayed())
        ).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(clickedItemPosition, CommonActions.clickChildViewWithId(R.id.imageThreeDots)))
    }

    private fun testClickTabs() {
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(clickTabLayoutPosition(0))
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(clickTabLayoutPosition(1))
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(clickTabLayoutPosition(2))
        Espresso.onView(firstView(withId(R.id.tabLayout)))
                .perform(clickTabLayoutPosition(3))
        Espresso.onView(firstView(withId(R.id.tabLayout)))
            .perform(clickTabLayoutPosition(4))
    }

    private fun testSelectSortOption() {
        val mockIntentData = Intent().apply {
            putExtra(ShopProductSortActivity.SORT_ID, "1")
            putExtra(ShopProductSortActivity.SORT_NAME, "Terbaru")
        }
        Intents.intending(IntentMatchers.hasComponent(
                ComponentNameMatchers.hasClassName("com.tokopedia.shop.sort.view.activity.ShopProductSortActivity"))
        ).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, mockIntentData))
        Espresso.onView(firstView(AllOf.allOf(
                withText("Urutkan"),
                isDescendantOfA(withId(R.id.sort_filter_items_wrapper))
        ))).perform(click())
    }

    private fun testClickEtalase() {
        val mockIntentData = Intent().apply {
            putExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID, "1")
            putExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_NAME, "Etalase")
        }
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, mockIntentData))
        Espresso.onView(firstView(AllOf.allOf(
                withText("Etalase Toko"),
                isDescendantOfA(withId(R.id.sort_filter_items_wrapper))
        ))).perform(click())
    }

    @After
    fun afterTest() {
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        assertThat(cassavaRule.validate(fileName), hasAllSuccess())
    }

}