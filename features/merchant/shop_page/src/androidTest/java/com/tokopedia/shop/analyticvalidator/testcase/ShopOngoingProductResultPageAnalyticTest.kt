package com.tokopedia.shop.analyticvalidator.testcase

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
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
import com.tokopedia.shop.mock.ShopProductResultPageMockResponseConfig.Companion.TYPE_ONGOING_PRODUCT
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.trackingoptimizer.constant.Constant
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ShopOngoingProductResultPageAnalyticTest {

    companion object {
        private const val SHOP_PAGE_ONGOING_PRODUCT_RESULT_PAGE_PRODUCT_CARD_TRACKER_MATCHER_PATH = "tracker/shop/shop_page_ongoing_product_result_product_card_tracker.json"
        private const val SAMPLE_SHOP_ID = "3418893"
        private const val SAMPLE_ETALASE_ID_CAMPAIGN = "cmp_26326"
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
        setupGraphqlMockResponse(ShopProductResultPageMockResponseConfig(TYPE_ONGOING_PRODUCT))
        activityRule.launchActivity(Intent().apply {
            putExtra(ShopParamConstant.EXTRA_SHOP_ID, SAMPLE_SHOP_ID)
            putExtra(ShopParamConstant.EXTRA_ETALASE_ID, SAMPLE_ETALASE_ID_CAMPAIGN)
        })
    }

    @Test
    fun testShopPageOngoingProductResultJourney() {
        waitForData(5000)
        testProductCard()
        validateTrackerOngoingProduct()
    }

    private fun validateTrackerOngoingProduct() {
        activityRule.activity.finish()
        waitForData(2000)
        doAnalyticDebuggerTest(SHOP_PAGE_ONGOING_PRODUCT_RESULT_PAGE_PRODUCT_CARD_TRACKER_MATCHER_PATH)
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

}