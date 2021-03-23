package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.*
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityCampaignTest {

    @get:Rule
    var activityRule = IntentsTestRule(TestOrderSummaryPageActivity::class.java, false, false)

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
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertProductCard(
                    shopName = "tokocgk",
                    shopLocation = "Kota Yogyakarta",
                    hasShopLocationImg = false,
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
    fun errorFlow_UpdateCartGotDialogPrompt() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_SLASH_PRICE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp116.000", "Bayar")

            cartInterceptor.customUpdateCartOccResponsePath = UPDATE_CART_OCC_DIALOG_PROMPT_RESPONSE_PATH

            pay()
            assertPromptDialogVisible("title prompt", "description prompt", "button")
        }
    }

    @Test
    fun errorFlow_CheckoutGotBottomSheetPrompt() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            checkoutInterceptor.customCheckoutResponsePath = CHECKOUT_BOTTOM_SHEET_PROMPT_RESPONSE_PATH

            pay()
            assertPromptBottomSheetVisible("title prompt", "description prompt", "button", "second button")
        }
    }

    @Test
    fun errorFlow_CheckoutGotDialogPrompt() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            checkoutInterceptor.customCheckoutResponsePath = CHECKOUT_DIALOG_PROMPT_RESPONSE_PATH

            pay()
            assertPromptDialogVisible("title prompt", "description prompt", "button", "second button")
        }
    }

    @Test
    fun errorFlow_OvoWalletInsufficientTopUp() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_LOW_WALLET_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp115.000", "Bayar")

            clickAddProductQuantity()

            assertProfilePaymentOvoError("Rp200.000. OVO Cash kamu tidak cukup.", "Top-Up")
            assertPaymentButtonEnable(false)

            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_TOP_UP_WALLET_RESPONSE_PATH
            clickOvoTopUpButton()

            assertPayment("Rp215.000", "Bayar")
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun errorFlow_OvoNoPhoneFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_NO_PHONE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertProfilePaymentOvoError(message = "Masukkan No. HP di halaman akun", buttonText = null)
            assertPaymentButtonEnable(false)
        }
    }

    @Test
    fun errorFlow_OvoActivationFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ACTIVATION_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertProfilePaymentOvoError(message = null, buttonText = "Aktivasi")
            assertPaymentButtonEnable(false)

            clickOvoActivationButton {
                cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ACTIVATED_RESPONSE_PATH
                performActivation(true)
            }

            assertPayment("Rp215.000", "Bayar")
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun errorFlow_ErrorTickerButtonPayDisabled() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CAMPAIGN_OVO_ONLY_ERROR_TICKER_DISABLE_BUTTON_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPaymentErrorTicker("OVO Error Ticker")
            assertPaymentButtonEnable(false)
        }
    }
}