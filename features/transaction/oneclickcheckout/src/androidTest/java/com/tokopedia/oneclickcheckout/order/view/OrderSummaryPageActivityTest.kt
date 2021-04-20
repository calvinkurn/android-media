package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.interceptor.RATES_WITH_INSURANCE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.RATES_WITH_NO_PROFILE_DURATION_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class OrderSummaryPageActivityTest {

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
    fun happyFlow_DirectCheckout() {
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

            assertProfileAddress(
                    headerMessage = "Pilihan 1",
                    addressName = "Address 1 - User 1 (1)",
                    addressDetail = "Address Street 1, District 1, City 1, Province 1 1",
                    isMainPreference = true
            )

            assertShipment(
                    shippingName = "Pengiriman Reguler",
                    shippingDuration = "Durasi 2-4 hari - Kurir Rekomendasi",
                    shippingPrice = "Rp15.000",
                    hasPromo = true
            )

            assertProfilePayment("Payment 1")

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
    fun happyFlow_ChangeCourier() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShipment(
                    shippingName = "Pengiriman Reguler",
                    shippingDuration = "Durasi 2-4 hari - Kurir Rekomendasi",
                    shippingPrice = "Rp15.000",
                    hasPromo = true
            )

            clickChangeCourier {
                chooseCourierWithText("AnterAja")
            }

            assertShipment(
                    shippingName = "Pengiriman Reguler",
                    shippingDuration = "Durasi 2-4 hari - AnterAja",
                    shippingPrice = "Rp16.000",
                    hasPromo = true
            )

            assertPayment("Rp117.000", "Bayar")
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun happyFlow_AddQuantity() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            clickAddProductQuantity()
            assertProductQuantity(2)

            assertPayment("Rp216.000", "Bayar")
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun happyFlow_CheckInsurance() {
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertInsurance(false)
            clickInsurance()
            assertInsurance(true)

            assertPayment("Rp117.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp1.000",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp117.000"
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
    fun happyFlow_ChooseBboFromTicker() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertBboTicker("Tersedia Bebas Ongkir (4-6 hari)")

            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE

            clickBboTicker()

            assertShipment(
                    shippingName = "Pengiriman Bebas Ongkir",
                    shippingDuration = "Durasi 4-6 hari",
                    shippingPrice = context.getString(R.string.lbl_osp_free_shipping_only_price),
                    hasPromo = false
            )

            assertPayment("Rp101.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        shippingPrice = "Rp0",
                        isBbo = true,
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp101.000"
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
    fun happyFlow_ChangeDurationAndCourier() {
        logisticInterceptor.customRatesResponsePath = RATES_WITH_NO_PROFILE_DURATION_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShipment(
                    shippingName = "Pengiriman Reguler",
                    shippingDuration = "Durasi 2-4 hari",
                    shippingPrice = null,
                    hasPromo = false
            )

            assertShipmentError(OrderSummaryPageViewModel.NO_DURATION_AVAILABLE)

            clickUbahDuration {
                chooseDurationWithText("Ekonomi (3-4 hari)")
            }

            assertShipmentWithCustomDuration(
                    shippingNameAndDuration = "Ekonomi (3-4 hari)",
                    shippingCourierAndPrice = "JNE OKE - Rp13.000",
                    hasPromo = false
            )

            assertPayment("Rp114.000", "Bayar")
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun errorFlow_ErrorGetOccCartPage() {
        cartInterceptor.customGetOccCartThrowable = IOException()

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertGlobalErrorVisible()
        }
    }
}