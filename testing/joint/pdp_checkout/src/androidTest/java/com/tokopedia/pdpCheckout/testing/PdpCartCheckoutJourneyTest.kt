package com.tokopedia.pdpCheckout.testing

import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.pdpCheckout.testing.atc_common.interceptor.AtcInterceptor
import com.tokopedia.pdpCheckout.testing.cart.robot.CartPageMocks
import com.tokopedia.pdpCheckout.testing.cart.robot.cartPage
import com.tokopedia.pdpCheckout.testing.checkout.robot.CheckoutPageMocks
import com.tokopedia.pdpCheckout.testing.checkout.robot.checkoutPage
import com.tokopedia.pdpCheckout.testing.product.detail.*
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
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
    var activityRule = ProductDetailIntentRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

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
                                addMockResponse(CheckoutPageMocks.SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, CheckoutPageMocks.SHIPMENT_ADDRESS_FORM_DEFAULT_RESPONSE), FIND_BY_CONTAINS)
                                addMockResponse(CheckoutPageMocks.SAVE_SHIPMENT_KEY, InstrumentationMockHelper.getRawString(context, CheckoutPageMocks.SAVE_SHIPMENT_DEFAULT_RESPONSE), FIND_BY_CONTAINS)
                                addMockResponse(CheckoutPageMocks.RATES_V3_KEY, InstrumentationMockHelper.getRawString(context, CheckoutPageMocks.RATES_V3_DEFAULT_RESPONSE), FIND_BY_CONTAINS)
                                addMockResponse(CheckoutPageMocks.VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, CheckoutPageMocks.VALIDATE_USE_DEFAULT_RESPONSE), FIND_BY_CONTAINS)
                                addMockResponse(CheckoutPageMocks.CHECKOUT_KEY, InstrumentationMockHelper.getRawString(context, CheckoutPageMocks.CHECKOUT_DEFAULT_RESPONSE), FIND_BY_CONTAINS)
                                return this
                            }
                        }.createMockModel(context)
                ), AtcInterceptor(context), productDetailInterceptor),
                context)
    }

    @Test
    fun openPdpAndGoToShipment_PassedAnalyticsTest() {
        activityRule.launchActivity(ProductDetailActivity.createIntent(context, 123))

        ProductDetailRobot().apply {
            Thread.sleep(5_000)
            clickBuyNormal()
            Thread.sleep(5_000)
            Espresso.pressBack()
            clickLihatKeranjangBottomSheetAtc(productDetailInterceptor)
        }

        cartPage {
            waitForData()
            clickBuyButton()
        }

        // Prevent glide crash
        Thread.sleep(2000)

        checkoutPage {
            waitForData()
            clickChooseDuration()
            waitForData()
            selectFirstShippingDurationOption()
            waitForData()
            clickChoosePaymentButton()
        } validateAnalytics {
            waitForData()
            hasPassedAnalytics(cassavaRule, ANALYTIC_VALIDATOR_QUERY_ID)
        }
    }

    @After
    fun cleanup() {
        activityRule.finishActivity()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_ID = "44"
    }
}