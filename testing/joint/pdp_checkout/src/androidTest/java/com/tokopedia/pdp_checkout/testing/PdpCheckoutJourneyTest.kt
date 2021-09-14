package com.tokopedia.pdp_checkout.testing

import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cart.testing.robot.CartPageMocks
import com.tokopedia.cart.testing.robot.cartPage
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.checkout.testing.R
import com.tokopedia.checkout.testing.robot.checkoutPage
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.product.detail.testing.ProductDetailInterceptor
import com.tokopedia.product.detail.testing.ProductDetailRobot
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PdpCheckoutJourneyTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<CartActivity>(CartActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun setup() {
        GraphqlClient.reInitRetrofitWithInterceptors(
                listOf(ProductDetailInterceptor(), MockInterceptor(
                        object : MockModelConfig() {
                            override fun createMockModel(context: Context): MockModelConfig {
                                addMockResponse(CartPageMocks.GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, CartPageMocks.GET_CART_LIST_MOCK_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
                                addMockResponse(CartPageMocks.UPDATE_CART_KEY, InstrumentationMockHelper.getRawString(context, CartPageMocks.UPDATE_CART_MOCK_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
                                addMockResponse(SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, R.raw.saf_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
                                addMockResponse(SAVE_SHIPMENT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.save_shipment_default_response), MockModelConfig.FIND_BY_CONTAINS)
                                addMockResponse(RATES_V3_KEY, InstrumentationMockHelper.getRawString(context, R.raw.ratesv3_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
                                addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.validate_use_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
                                addMockResponse(CHECKOUT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.checkout_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
                                return this
                            }
                        }
                )),
                context)
    }

    @Test
    fun loadCartAndGoToShipment_PassedAnalyticsTest() {
        activityRule.launchActivity(null)

        ProductDetailRobot().apply {
            clickLihatKeranjangBottomSheetAtc()
        }

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
            hasPassedAnalytics(cassavaRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }
    }

    @After
    fun cleanup() {
        activityRule.finishActivity()
    }

    companion object {
//        private const val GET_CART_LIST_KEY = "cart_revamp"
//        private const val UPDATE_CART_KEY = "update_cart_v2"

        private const val SHIPMENT_ADDRESS_FORM_KEY = "shipment_address_form"
        private const val SAVE_SHIPMENT_KEY = "save_shipment"
        private const val RATES_V3_KEY = "ratesV3"
        private const val VALIDATE_USE_KEY = "validate_use_promo_revamp"
        private const val CHECKOUT_KEY = "checkout"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/checkout.json"
    }
}