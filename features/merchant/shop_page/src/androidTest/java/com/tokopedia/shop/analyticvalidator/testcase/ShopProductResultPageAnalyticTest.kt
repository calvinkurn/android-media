package com.tokopedia.shop.analyticvalidator.testcase

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_RESULT_CODE_WISHLIST
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTION_RESULT_PRODUCT
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.mock.ShopProductResultPageMockResponseConfig
import com.tokopedia.shop.mock.ShopProductResultPageMockResponseConfig.Companion.TYPE_NORMAL_PRODUCT
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.trackingoptimizer.constant.Constant
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ShopProductResultPageAnalyticTest {

    companion object {
        private const val SHOP_PAGE_PRODUCT_RESULT_PAGE_PRODUCT_CARD_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_product_result_product_card_tracker.json"
        private const val SHOP_PAGE_PRODUCT_RESULT_PAGE_CLICK_SORT_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_product_result_page_click_sort_tracker.json"
        private const val SAMPLE_SHOP_ID = "3418893"
    }

    @get:Rule
    var activityRule: IntentsTestRule<ShopProductListResultActivity> = IntentsTestRule(ShopProductListResultActivity::class.java, false, false)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.setString(Constant.TRACKING_QUEUE_SEND_TRACK_NEW_REMOTECONFIGKEY, "true")
        gtmLogDBSource.deleteAll().toBlocking().first()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        setupGraphqlMockResponse(ShopProductResultPageMockResponseConfig(TYPE_NORMAL_PRODUCT))
        activityRule.launchActivity(Intent().apply {
            putExtra(ShopParamConstant.EXTRA_SHOP_ID, SAMPLE_SHOP_ID)
        })
    }

    @Test
    fun testShopPageProductResultJourney() {
        waitForData(5000)
        testClickSortButton()
        testProductCard()
        validateTracker()
    }

    private fun validateTracker() {
        activityRule.activity.finish()
        waitForData(2000)
        //click sort tracker
        doAnalyticDebuggerTest(SHOP_PAGE_PRODUCT_RESULT_PAGE_CLICK_SORT_TRACKER_MATCHER_PATH)

        //product card tracker
        doAnalyticDebuggerTest(SHOP_PAGE_PRODUCT_RESULT_PAGE_PRODUCT_CARD_TRACKER_MATCHER_PATH)

    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    private fun waitForData(ms: Long) {
        Thread.sleep(ms)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }

    private fun testProductCard() {
        val clickedItemPosition = 2
        val sampleProductIdWishlist = "23151232"
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
        Espresso.onView(firstView(withId(R.id.recycler_view))).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(clickedItemPosition, click())
        )
        Espresso.onView(firstView(withId(R.id.recycler_view))).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(clickedItemPosition, CommonActions.clickChildViewWithId(R.id.imageThreeDots))
        )
    }

    private fun testClickSortButton() {
        val mockIntentData = Intent().apply {
            putExtra(ShopProductSortActivity.SORT_ID, "1")
            putExtra(ShopProductSortActivity.SORT_NAME, "Terbaru")
        }
        Intents.intending(IntentMatchers.hasComponent(
                ComponentNameMatchers.hasClassName("com.tokopedia.shop.sort.view.activity.ShopProductSortActivity"))
        ).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, mockIntentData))
        Espresso.onView(AllOf.allOf(withText("Urutkan"), isDescendantOfA(withId(R.id.sort_filter_items_wrapper))))
                .check(matches(isDisplayed())).perform(click())
    }

}