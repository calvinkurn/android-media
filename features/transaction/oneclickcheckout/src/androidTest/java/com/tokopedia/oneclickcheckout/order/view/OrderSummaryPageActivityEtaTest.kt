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

class OrderSummaryPageActivityEtaTest {

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
    fun happyFlow_WithEta() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MANY_PROFILE_REVAMP_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_ETA_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler",
                    shippingCourier = "AnterAja (Rp10.000)",
                    shippingPrice = null,
                    shippingEta = "Estimasi tiba besok - 3 Feb")

            assertShipmentPromoRevamp(
                    hasPromo = true,
                    promoTitle = "Tersedia Bebas Ongkir",
                    promoSubtitle = "Estimasi tiba besok - 3 Feb")

            clickChangeCourierRevamp {
                chooseCourierWithText("JNE Reg (Rp9.000)")
            }

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler",
                    shippingCourier = "JNE Reg (Rp9.000)",
                    shippingPrice = null,
                    shippingEta = "Estimasi tiba besok - 4 Feb")

            clickChangeDurationRevamp {
                chooseDurationWithText("Same Day (Rp14.000)")
            }

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Same Day",
                    shippingCourier = "Gojek (Rp14.000)",
                    shippingPrice = null,
                    shippingEta = "Estimasi tiba hari ini - besok")

            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE
            clickApplyShipmentPromoRevamp()

            assertShipmentPromoRevamp(hasPromo = false)

            assertShipmentRevamp(
                    shippingDuration = null,
                    shippingCourier = "Pengiriman Bebas Ongkir",
                    shippingPrice = null,
                    shippingEta = "Estimasi tiba besok - 3 Feb")

            //    Deprecated Test (will delete in next iteration)
//            preferenceInterceptor.customGetPreferenceListResponsePath = GET_PREFERENCE_LIST_WITH_ETA_RESPONSE_PATH
//
//            clickAddOrChangePreferenceRevamp {
//                assertProfile(0) {
//                    val shippingDuration = it.findViewById<Typography>(R.id.tv_new_shipping_duration)
//                    assertEquals("Estimasi tiba besok", shippingDuration.text)
//                }
//                assertProfile(1) {
//                    val shippingDuration = it.findViewById<Typography>(R.id.tv_new_shipping_duration)
//                    assertEquals("Estimasi tiba besok - 3 Feb", shippingDuration.text)
//                }
//            }
        }
    }

    @Test
    fun happyFlow_BebasOngkirExtra() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_WITH_BOE_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_ETA_WITH_BOE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertProductCard(
                    shopName = "tokocgk",
                    shopLocation = "TokoCabang",
                    hasShopLocationImg = true,
                    hasShopBadge = true,
                    productName = "Product1",
                    productPrice = "Rp100.000",
                    productSlashPrice = null,
                    isFreeShipping = true,
                    productQty = 1
            )

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler",
                    shippingCourier = "AnterAja (Rp10.000)",
                    shippingPrice = null,
                    shippingEta = "Estimasi tiba besok - 3 Feb"
            )

            assertShipmentPromoRevamp(
                    hasPromo = true,
                    promoTitle = "Tersedia Bebas Ongkir Extra",
                    promoSubtitle = "Estimasi tiba besok - 3 Feb")

            assertPayment("Rp111.300", "Bayar")

            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_BOE_APPLIED_RESPONSE
            clickApplyShipmentPromoRevamp()

            assertShipmentPromoRevamp(hasPromo = false)

            assertShipmentRevamp(
                    shippingDuration = null,
                    shippingCourier = "Pengiriman Bebas Ongkir Extra",
                    shippingPrice = null,
                    shippingEta = "Estimasi tiba besok - 3 Feb"
            )

            assertPayment("Rp101.300", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        isBbo = true,
                        insurancePrice = "Rp300",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp101.300"
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