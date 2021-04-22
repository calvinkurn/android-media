package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.*
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityRemoveProfileTest {

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
    fun typePre_ShowTicker() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_REMOVE_PROFILE_PRE_RESPONSE_PATH

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

            assertProfileTicker(
                    isShown = true,
                    title = "Proses belanjamu akan lebih mudah!",
                    description = "Konsep template bakal diganti sama rekomendasi pengiriman & pembayaran. Update terus aplikasimu!",
                    closeButtonVisible = true
            )

//            Deprecated Test (will remove on next iteration)
//            assertProfileRevampWording("Template Beli Langsung")
//
//            assertProfileRevampActionWording("Pilih template lain")

            assertAddressRevamp(
                    addressName = "Address 1 - User 1 (1)",
                    addressDetail = "Address Street 1, District 1, City 1, Province 1 1",
            )

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler (2-4 hari)",
                    shippingCourier = "Kurir Rekomendasi",
                    shippingPrice = "Rp15.000",
                    shippingEta = null
            )

            assertPaymentRevamp(paymentName = "Payment 1", paymentDetail = null)

            assertPayment("Rp116.000", "Bayar")
        }
    }

    @Test
    fun typePre_CloseTicker() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_REMOVE_PROFILE_PRE_RESPONSE_PATH

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

            assertProfileTicker(
                    isShown = true,
                    title = "Proses belanjamu akan lebih mudah!",
                    description = "Konsep template bakal diganti sama rekomendasi pengiriman & pembayaran. Update terus aplikasimu!",
                    closeButtonVisible = true
            )

            clickCloseProfileTicker()

            clickChangeAddressRevamp {
                clickAddress(1)
            }

            assertProfileTicker(isShown = false)
        }
    }

    @Test
    fun typePost_NoTicker() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_REMOVE_PROFILE_POST_RESPONSE_PATH

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

            assertProfileTicker(isShown = false)

            assertProfileRevampNewHeader()

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

            assertPayment("Rp116.000", "Bayar")
        }
    }

    @Test
    fun typePost_ShowTicker() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_REMOVE_PROFILE_POST_WITH_TICKER_RESPONSE_PATH

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

            assertProfileTicker(
                    isShown = true,
                    title = "Tinggal cek, terus langsung bayar!",
                    description = "Kamu bisa beli langsung dengan alamat, pengiriman dan pembayaran yang kami rekomendasikan.",
                    closeButtonVisible = true
            )

            assertProfileRevampNewHeader()

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

            assertPayment("Rp116.000", "Bayar")

            clickCloseProfileTicker()

            clickChangeAddressRevamp {
                clickAddress(1)
            }

            assertProfileTicker(isShown = false)
        }
    }

    @Test
    fun typePost_NoAddress() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_REMOVE_PROFILE_POST_NO_ADDRESS_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertProfileTicker(isShown = false)

            assertNoAddressLayoutVisible()

            clickAddNewAddress()

            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_REMOVE_PROFILE_POST_RESPONSE_PATH
            intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, Intent().putExtra(LogisticConstant.EXTRA_ADDRESS_NEW, SaveAddressDataModel())))

            clickAddNewAddress()

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

            assertProfileTicker(isShown = false)

            assertProfileRevampNewHeader()

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

            assertPayment("Rp116.000", "Bayar")
        }
    }
}