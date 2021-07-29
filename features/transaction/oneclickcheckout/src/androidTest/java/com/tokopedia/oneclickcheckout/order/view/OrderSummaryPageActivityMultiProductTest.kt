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
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_MULTI_PRODUCT_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_MULTI_PRODUCT_WHOLESALE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityMultiProductTest {

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
    fun happyFlow_AllInfo_DirectCheckout() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MULTI_PRODUCT_RESPONSE_PATH

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

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler (2-4 hari)",
                    shippingCourier = "Kurir Rekomendasi",
                    shippingPrice = "Rp15.000",
                    shippingEta = null
            )

            assertPaymentRevamp(paymentName = "Payment 1", paymentDetail = null)

            assertPayment("Rp216.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp200.000",
                        shippingPrice = "Rp15.000",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp216.000"
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
    fun happyFlow_WholeSale() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MULTI_PRODUCT_WHOLESALE_RESPONSE_PATH

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
                    productPrice = "Rp10.000",
                    productSlashPrice = null,
                    productSlashPriceLabel = null,
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
                    productPrice = "Rp10.000",
                    productSlashPrice = null,
                    productSlashPriceLabel = null,
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah"),
                    productQty = 1,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )

            assertPayment("Rp36.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp20.000",
                        shippingPrice = "Rp15.000",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp36.000"
                )
                closeBottomSheet()
            }

            clickAddProductQuantity(index = 0, times = 1)

            assertProductCard(
                    index = 0,
                    productName = "Product1",
                    productPrice = "Rp9.000",
                    productSlashPrice = "Rp10.000",
                    productSlashPriceLabel = null,
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah, ", "Harga Grosir"),
                    productQty = 2,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )
            assertProductCard(
                    index = 1,
                    productName = "Product2",
                    productPrice = "Rp9.000",
                    productSlashPrice = "Rp10.000",
                    productSlashPriceLabel = null,
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah, ", "Harga Grosir"),
                    productQty = 1,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )

            assertPayment("Rp43.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp27.000",
                        shippingPrice = "Rp15.000",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp43.000"
                )
                closeBottomSheet()
            }

            clickAddProductQuantity(index = 1, times = 2)

            assertProductCard(
                    index = 0,
                    productName = "Product1",
                    productPrice = "Rp5.000",
                    productSlashPrice = "Rp10.000",
                    productSlashPriceLabel = null,
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah, ", "Harga Grosir"),
                    productQty = 2,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )
            assertProductCard(
                    index = 1,
                    productName = "Product2",
                    productPrice = "Rp5.000",
                    productSlashPrice = "Rp10.000",
                    productSlashPriceLabel = null,
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah, ", "Harga Grosir"),
                    productQty = 3,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )

            assertPayment("Rp41.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp25.000",
                        shippingPrice = "Rp15.000",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp41.000"
                )
                closeBottomSheet()
            }

            clickMinusProductQuantity(index = 0, times = 1)

            assertProductCard(
                    index = 0,
                    productName = "Product1",
                    productPrice = "Rp9.000",
                    productSlashPrice = "Rp10.000",
                    productSlashPriceLabel = null,
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah, ", "Harga Grosir"),
                    productQty = 1,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )
            assertProductCard(
                    index = 1,
                    productName = "Product2",
                    productPrice = "Rp9.000",
                    productSlashPrice = "Rp10.000",
                    productSlashPriceLabel = null,
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah, ", "Harga Grosir"),
                    productQty = 3,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )

            assertPayment("Rp52.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp36.000",
                        shippingPrice = "Rp15.000",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp52.000"
                )
                closeBottomSheet()
            }

            clickMinusProductQuantity(index = 1, times = 2)

            assertProductCard(
                    index = 0,
                    productName = "Product1",
                    productPrice = "Rp10.000",
                    productSlashPrice = null,
                    productSlashPriceLabel = null,
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
                    productPrice = "Rp10.000",
                    productSlashPrice = null,
                    productSlashPriceLabel = null,
                    productVariant = "hitam, putih",
                    productWarningMessage = "sisa < 1",
                    productAlertMessage = "alert",
                    productInfo = listOf("cashback 10%, ", "harga berubah"),
                    productQty = 1,
                    productNotes = "notes apa aja yang isinya panjang banget sekali jauh disana"
            )

            assertPayment("Rp36.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp20.000",
                        shippingPrice = "Rp15.000",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp36.000"
                )
                closeBottomSheet()
            }
        }
    }
}