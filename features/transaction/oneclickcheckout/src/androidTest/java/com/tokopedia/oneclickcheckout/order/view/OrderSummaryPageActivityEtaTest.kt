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
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class OrderSummaryPageActivityEtaTest {

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
        }
    }

    @Test
    fun happyFlow_BebasOngkirExtra() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_WITH_BOE_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_ETA_WITH_BOE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShopCard(
                    shopName = "tokocgk",
                    shopLocation = "TokoCabang",
                    hasShopLocationImg = true,
                    hasShopBadge = true,
                    isFreeShipping = true,
                    preOrderText = "",
                    alertMessage = ""
            )
            assertProductCard(
                    productName = "Product1",
                    productPrice = "Rp100.000",
                    productSlashPrice = null,
                    productSlashPriceLabel = null,
                    productVariant = null,
                    productWarningMessage = null,
                    productAlertMessage = null,
                    productInfo = null,
                    productQty = 1,
                    productNotes = null
            )

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler",
                    shippingCourier = "AnterAja (Rp10.000)",
                    shippingPrice = null,
                    shippingEta = "Estimasi tiba besok - 3 Feb"
            )

            assertShipmentPromoRevamp(
                    hasPromo = true,
                    promoDescription = "Tersedia Bebas Ongkir Extra")

            assertPayment("Rp111.300", "Bayar")

            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_BOE_APPLIED_RESPONSE
            clickApplyShipmentPromoRevamp()

            assertShipmentPromoRevamp(hasPromo = false)

            assertShipmentRevamp(
                    shippingDuration = null,
                    shippingCourier = "Pengiriman Bebas Ongkir Extra",
                    shippingPrice = null,
                    shippingEta = null
            )

            assertPayment("Rp101.300", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        isBbo = true,
                        insurancePrice = "Rp300",
                        totalPrice = "Rp101.300",
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1000.0,
                                showTooltip = true
                            )
                        )
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