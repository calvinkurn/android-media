package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.CHECKOUT_BOTTOM_SHEET_PROMPT_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.CHECKOUT_DIALOG_PROMPT_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_SLASH_PRICE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityCampaignTest {

    @get:Rule
    var activityRule = IntentsTestRule(OrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val preferenceInterceptor = OneClickCheckoutInterceptor.preferenceInterceptor
    private val logisticInterceptor = OneClickCheckoutInterceptor.logisticInterceptor
    private val promoInterceptor = OneClickCheckoutInterceptor.promoInterceptor
    private val checkoutInterceptor = OneClickCheckoutInterceptor.checkoutInterceptor

    @Before
    fun setup() {
        OneClickCheckoutInterceptor.resetAllCustomResponse()
        OneClickCheckoutInterceptor.setupGraphqlMockResponse(context)
        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun happyFlow_SlashPriceDirectCheckout() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_SLASH_PRICE_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertProductCard(
                    shopName = "tokocgk",
                    shopLocation = "Kota Yogyakarta",
                    hasShopBadge = true,
                    productName = "Product1",
                    productPrice = "Rp100.000",
                    productSlashPrice = "Rp200.000",
                    isFreeShipping = true,
                    productQty = 1
            )

            assertPayment("Rp116.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        shippingPrice = "Rp15.000",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp116.000"
                )
                closeBottomSheet()
            }
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun errorFlow_CheckoutGotBottomSheetPrompt() {
        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            checkoutInterceptor.customCheckoutResponsePath = CHECKOUT_BOTTOM_SHEET_PROMPT_RESPONSE_PATH

            pay()
            assertPromptBottomSheetVisible("title prompt", "description prompt", "button", "second button")
        }
    }

    @Test
    fun errorFlow_CheckoutGotDialogPrompt() {
        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            checkoutInterceptor.customCheckoutResponsePath = CHECKOUT_DIALOG_PROMPT_RESPONSE_PATH

            pay()
            assertPromptDialogVisible("title prompt", "description prompt", "button", "second button")
        }
    }
}