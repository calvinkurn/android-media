package com.tokopedia.pdpCartCheckout.testing

import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.atc_common.testing.interceptor.AtcInterceptor
import com.tokopedia.cart.testing.robot.CartPageMocks
import com.tokopedia.cart.testing.robot.cartPage
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.checkout.testing.robot.checkoutPage
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.product.detail.testing.ProductDetailInterceptor
import com.tokopedia.product.detail.testing.RESPONSE_P1_PATH
import com.tokopedia.product.detail.testing.RESPONSE_P2_DATA_PATH
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PdpCartCheckoutJourneyTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<CartActivity>(CartActivity::class.java,
            false,
            false) {}

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaRule = CassavaTestRule(true, false)

    private val productDetailInterceptor = ProductDetailInterceptor()

    @Before
    fun setup() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        productDetailInterceptor.customP1ResponsePath = RESPONSE_P1_PATH
        productDetailInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_PATH
        GraphqlClient.reInitRetrofitWithInterceptors(
                listOf(MockInterceptor(
                        object : MockModelConfig() {
                            override fun createMockModel(context: Context): MockModelConfig {
                                addMockResponse(CartPageMocks.GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, CartPageMocks.GET_CART_LIST_MOCK_DEFAULT_RESPONSE), FIND_BY_CONTAINS)
                                addMockResponse(CartPageMocks.UPDATE_CART_KEY, InstrumentationMockHelper.getRawString(context, CartPageMocks.UPDATE_CART_MOCK_DEFAULT_RESPONSE), FIND_BY_CONTAINS)
                                addMockResponse(SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, R.raw.saf_analytics_default_response), FIND_BY_CONTAINS)
                                addMockResponse(SAVE_SHIPMENT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.save_shipment_default_response), FIND_BY_CONTAINS)
                                addMockResponse(RATES_V3_KEY, InstrumentationMockHelper.getRawString(context, R.raw.ratesv3_analytics_default_response), FIND_BY_CONTAINS)
                                addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.validate_use_analytics_default_response), FIND_BY_CONTAINS)
                                addMockResponse(CHECKOUT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.checkout_analytics_default_response), FIND_BY_CONTAINS)
                                return this
                            }
                        }.createMockModel(context)
                ), AtcInterceptor(context), productDetailInterceptor),
                context)
    }

    @Test
    fun loadCartAndGoToShipment_PassedAnalyticsTest() {
//        activityRule.launchActivity(ProductDetailActivity.createIntent(context, 123))
        activityRule.launchActivity(null)

//        ProductDetailRobot().apply {
////            clickBeli()
//            Thread.sleep(5_000)
////            Espresso.pressBack()
//            clickLihatKeranjangBottomSheetAtc()
//        }

        cartPage {
            waitForData()
            clickBuyButton()
        }

        // Prevent glide crash
        Thread.sleep(2000)

        checkoutPage {
            waitForData()
            clickChooseDuration(activityRule)
            waitForData()
            selectFirstShippingDurationOption()
            waitForData()
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            hasPassedAnalytics(cassavaRule, "44")
        }
    }

    @After
    fun cleanup() {
        activityRule.finishActivity()
    }

    companion object {
        private const val SHIPMENT_ADDRESS_FORM_KEY = "shipment_address_form"
        private const val SAVE_SHIPMENT_KEY = "save_shipment"
        private const val RATES_V3_KEY = "ratesV3"
        private const val VALIDATE_USE_KEY = "validate_use_promo_revamp"
        private const val CHECKOUT_KEY = "checkout"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/checkout.json"
    }
}