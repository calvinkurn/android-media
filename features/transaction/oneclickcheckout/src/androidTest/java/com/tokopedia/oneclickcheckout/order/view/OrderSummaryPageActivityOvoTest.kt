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

class OrderSummaryPageActivityOvoTest {

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
    fun happyFlow_OvoActivationFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_OVO_ACTIVATION_RESPONSE_PATH

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
                    productSlashPrice = null,
                    isFreeShipping = true,
                    productQty = 1
            )

            assertPayment("Rp115.000", "Pilih Pembayaran")

            assertPaymentErrorTicker("OVO kamu belum aktif. Silahkan aktifkan atau pilih pembayaran lain.")

            assertProfilePaymentOvoError(message = null, buttonText = "Aktivasi")

            clickOvoActivationButton {
                cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_OVO_ACTIVATED_RESPONSE_PATH
                performActivation(true)
            }

            assertProfilePayment("OVO")
            assertProfilePaymentDetail("Rp 1.000.000")

            assertPayment("Rp115.000", "Bayar")
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun happyFlow_OvoNoPhoneFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_OVO_NO_PHONE_RESPONSE_PATH

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
                    productSlashPrice = null,
                    isFreeShipping = true,
                    productQty = 1
            )

            assertPayment("Rp115.000", "Pilih Pembayaran")

            assertProfilePaymentOvoError(message = "Masukkan No. HP di halaman akun", buttonText = null)
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun errorFlow_OvoActivationFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_OVO_ACTIVATION_RESPONSE_PATH

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
                    productSlashPrice = null,
                    isFreeShipping = true,
                    productQty = 1
            )

            assertPayment("Rp115.000", "Pilih Pembayaran")

            assertPaymentErrorTicker("OVO kamu belum aktif. Silahkan aktifkan atau pilih pembayaran lain.")

            assertProfilePaymentOvoError(message = null, buttonText = "Aktivasi")

            clickOvoActivationButton {
                performActivation(false)
            }

            assertPayment("Rp115.000", "Pilih Pembayaran")

            assertPaymentErrorTicker("OVO kamu belum aktif. Silahkan aktifkan atau pilih pembayaran lain.")

            assertProfilePaymentOvoError(message = null, buttonText = "Aktivasi")
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun happyFlow_OvoTopUpFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_OVO_LOW_WALLET_RESPONSE_PATH

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
                    productSlashPrice = null,
                    isFreeShipping = true,
                    productQty = 1
            )

            assertPayment("Rp115.000", "Pilih Pembayaran")

            assertPaymentErrorTicker("OVO cash kamu tidak cukup. Silakan top up atau pilih pembayaran lain.")

            assertProfilePaymentOvoError(message = "Rp1.000. OVO Cash kamu tidak cukup.", buttonText = "Top-Up")

            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_OVO_TOP_UP_RESPONSE_PATH
            clickOvoTopUpButton()

            assertPayment("Rp115.000", "Bayar")
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }
}