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

class OrderSummaryPageActivityTokonowTest {

    @get:Rule
    var activityRule = IntentsTestRule(TestOrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val logisticInterceptor = OneClickCheckoutInterceptor.logisticInterceptor
    private val promoInterceptor = OneClickCheckoutInterceptor.promoInterceptor

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
    fun happyFlow_AllInfo_DirectCheckout() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MULTI_PRODUCT_TOKONOW_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_TOKONOW_RP0_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShopCard(
                    shopName = "tokocgk",
                    shopLocation = "Kota Yogyakarta",
                    hasShopLocationImg = false,
                    hasShopBadge = true,
                    isFreeShipping = true,
                    preOrderText = "Pre Order 3 hari",
                    alertMessage = "Alert"
            )
            assertProductCard(
                    index = 0,
                    productName = "Product1",
                    productPrice = "Rp100.000",
                    productSlashPrice = "Rp100.000",
                    productSlashPriceLabel = "5%",
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah"),
                    productQty = 1,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )
            assertProductCard(
                    index = 1,
                    productName = "Product2",
                    productPrice = "Rp100.000",
                    productSlashPrice = "Rp100.000",
                    productSlashPriceLabel = "5%",
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah"),
                    productQty = 1,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )

            assertAddressRevamp(
                    addressName = "Address 1 - User 1 (1)",
                    addressDetail = "Address Street 1, District 1, City 1, Province 1 1",
                    isMainAddress = true
            )

            assertShipmentError("Gagal menampilkan pengiriman")

            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_TOKONOW_RP0_APPLIED_RESPONSE
            clickShipmentReloadAction()

            assertShipmentRevamp(
                    shippingDuration = null,
                    shippingCourier = "Now! 2 jam tiba (Rp0)",
                    shippingPrice = null,
                    shippingEta = "Tiba dalam 2 jam"
            )

            assertPaymentRevamp(paymentName = "Payment 1", paymentDetail = null)

            assertPayment("Rp201.300", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp200.000",
                        isBbo = true,
                        insurancePrice = "Rp300",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp201.300"
                )
                closeBottomSheet()
            }

            logisticInterceptor.customRatesResponsePath = RATES_TOKONOW_DISCOUNT_RESPONSE_PATH
            clickAddProductQuantity(0, 1)

            assertShipmentRevamp(
                    shippingDuration = null,
                    shippingCourier = "Now! 2 jam tiba (Rp20.000 Rp10.000)",
                    shippingPrice = null,
                    shippingEta = "Tiba dalam 2 jam"
            )
            Thread.sleep(5000)

            assertPaymentRevamp(paymentName = "Payment 1", paymentDetail = null)

            assertPayment("Rp311.300", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp300.000",
                        shippingPrice = "Rp20.000",
                        shippingDiscount = "Rp10.000",
                        insurancePrice = "Rp300",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp311.300"
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
}