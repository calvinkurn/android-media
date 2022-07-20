package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.*
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class OrderSummaryPageActivityGopayTest {

    @get:Rule
    var activityRule = IntentsTestRule(TestOrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor

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
    fun happyFlow_GopayActivationFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOPAY_ACTIVATION_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent().apply {
                putExtra("status", "1")
            }))

        orderSummaryPage {
            assertShopCard(
                shopName = "mrfrs80kr-2",
                shopLocation = "Kota Yogyakarta",
                hasShopLocationImg = false,
                hasShopBadge = true,
                isFreeShipping = true,
                preOrderText = "",
                alertMessage = ""
            )
            assertProductCard(
                productName = "Produk BO KR 2",
                productPrice = "Rp1.500.000",
                productSlashPrice = null,
                productSlashPriceLabel = null,
                productVariant = null,
                productWarningMessage = null,
                productAlertMessage = null,
                productInfo = null,
                productQty = 1,
                productNotes = null
            )

            assertPayment("Rp1.515.000", "Ganti Metode Bayar")

            assertProfilePaymentOvoErrorRevamp(message = null, buttonText = "Aktivasi")
        } pay {
            assertGoToPayment(
                redirectUrl = "https://www.tokopedia.com/payment",
                queryString = "transaction_id=123",
                method = "POST"
            )
        }
    }

    @Test
    fun happyFlow_GopayNoPhoneFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOPAY_NO_PHONE_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShopCard(
                shopName = "mrfrs80kr-2",
                shopLocation = "Kota Yogyakarta",
                hasShopLocationImg = false,
                hasShopBadge = true,
                isFreeShipping = true,
                preOrderText = "",
                alertMessage = ""
            )
            assertProductCard(
                productName = "Produk BO KR 2",
                productPrice = "Rp1.500.000",
                productSlashPrice = null,
                productSlashPriceLabel = null,
                productVariant = null,
                productWarningMessage = null,
                productAlertMessage = null,
                productInfo = null,
                productQty = 1,
                productNotes = null
            )

            assertPayment("Rp1.515.000", "Ganti Metode Bayar")

            assertProfilePaymentOvoErrorRevamp(message = "Masukkan No. HP di halaman akun", buttonText = null)
        } pay {
            assertGoToPayment(
                redirectUrl = "https://www.tokopedia.com/payment",
                queryString = "transaction_id=123",
                method = "POST"
            )
        }
    }

    @Test
    fun happyFlow_GopayFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOPAY_ACTIVATED_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShopCard(
                shopName = "mrfrs80kr-2",
                shopLocation = "Kota Yogyakarta",
                hasShopLocationImg = false,
                hasShopBadge = true,
                isFreeShipping = true,
                preOrderText = "",
                alertMessage = ""
            )
            assertProductCard(
                productName = "Produk BO KR 2",
                productPrice = "Rp1.500.000",
                productSlashPrice = null,
                productSlashPriceLabel = null,
                productVariant = null,
                productWarningMessage = null,
                productAlertMessage = null,
                productInfo = null,
                productQty = 1,
                productNotes = null
            )

            assertPaymentRevamp(paymentName = "GoPay", paymentDetail = "Rp 10.000.000")

            assertPayment("Rp1.515.000", "Bayar")
        } pay {
            assertGoToPayment(
                redirectUrl = "https://www.tokopedia.com/payment",
                queryString = "transaction_id=123",
                method = "POST"
            )
        }
    }

    @Test
    fun happyFlow_GopayTopUpFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOPAY_LOW_WALLET_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShopCard(
                shopName = "mrfrs80kr-2",
                shopLocation = "Kota Yogyakarta",
                hasShopLocationImg = false,
                hasShopBadge = true,
                isFreeShipping = true,
                preOrderText = "",
                alertMessage = ""
            )
            assertProductCard(
                productName = "Produk BO KR 2",
                productPrice = "Rp1.500.000",
                productSlashPrice = null,
                productSlashPriceLabel = null,
                productVariant = null,
                productWarningMessage = null,
                productAlertMessage = null,
                productInfo = null,
                productQty = 1,
                productNotes = null
            )

            assertPayment("Rp1.515.000", "Ganti Metode Bayar")

            assertProfilePaymentOvoErrorRevamp("GoPay kamu tidak cukup.", "Top-up")

            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOPAY_ACTIVATED_RESPONSE_PATH
            clickOvoTopUpButtonRevamp()

            assertPaymentRevamp(paymentName = "GoPay", paymentDetail = "Rp 10.000.000")

            assertPayment("Rp1.515.000", "Bayar")
        } pay {
            assertGoToPayment(
                redirectUrl = "https://www.tokopedia.com/payment",
                queryString = "transaction_id=123",
                method = "POST"
            )
        }
    }
}