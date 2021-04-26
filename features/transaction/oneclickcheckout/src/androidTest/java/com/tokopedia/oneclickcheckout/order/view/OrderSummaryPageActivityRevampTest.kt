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
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.*
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class OrderSummaryPageActivityRevampTest {

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
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH

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

        //    Deprecated Test (will delete in next iteration)
//            assertProfileRevampWording("Template Beli Langsung")
//            assertProfileRevampUtama(true)
//
//            assertProfileRevampActionWording("Tambah template")

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
    fun happyFlow_ChangeAddress() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertAddressRevamp(
                    addressName = "Address 1 - User 1 (1)",
                    addressDetail = "Address Street 1, District 1, City 1, Province 1 1",
            )

            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_WITH_ADDRESS_2_RESPONSE_PATH
            clickChangeAddressRevamp {
                clickAddress(1)
            }

            assertAddressRevamp(
                    addressName = "Address 2 - User 1 (2)",
                    addressDetail = "Address Street 2, District 2, City 2, Province 2 2",
            )

            assertPayment("Rp116.000", "Bayar")
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun happyFlow_ChangeDuration() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler (2-4 hari)",
                    shippingCourier = "Kurir Rekomendasi",
                    shippingPrice = "Rp15.000",
                    shippingEta = null
            )

            clickChangeDurationRevamp {
                chooseDurationWithText("Next Day (1 hari)")
            }

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Next Day (1 hari)",
                    shippingCourier = "JNE",
                    shippingPrice = "Rp38.000",
                    shippingEta = null
            )

            assertPayment("Rp139.000", "Bayar")
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
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler (2-4 hari)",
                    shippingCourier = "Kurir Rekomendasi",
                    shippingPrice = "Rp15.000",
                    shippingEta = null
            )

            clickChangeCourierRevamp {
                chooseCourierWithText("AnterAja")
            }

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler (2-4 hari)",
                    shippingCourier = "AnterAja",
                    shippingPrice = "Rp16.000",
                    shippingEta = null
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
    fun happyFlow_ChangePayment() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, Intent().apply {
            putExtra(PreferenceEditActivity.EXTRA_RESULT_GATEWAY, "payment2")
            putExtra(PreferenceEditActivity.EXTRA_RESULT_METADATA, "metadata")
        }))

        orderSummaryPage {
            assertPaymentRevamp(
                    paymentName = "Payment 1",
                    paymentDetail = null
            )

            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_WITH_PAYMENT_2_RESPONSE_PATH
            clickChangePaymentRevamp()

            assertPaymentRevamp(
                    paymentName = "Payment 2",
                    paymentDetail = "second payment"
            )

            assertPayment("Rp116.000", "Bayar")
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
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH

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
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH
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

            clickInsurance()
            assertInsurance(false)

            assertPayment("Rp116.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = null,
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
    fun happyFlow_ChooseBboFromTicker() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShipmentPromoRevamp(
                    hasPromo = true,
                    promoDescription = "Tersedia Bebas Ongkir (4-6 hari)")

            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE

            clickApplyShipmentPromoRevamp()

            assertShipmentPromoRevamp(hasPromo = false)

            assertShipmentRevamp(
                    shippingDuration = null,
                    shippingCourier = "Pengiriman Bebas Ongkir (4-6 hari)",
                    shippingPrice = null,
                    shippingEta = null
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
    fun happyFlow_UnchooseBbo() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShipmentPromoRevamp(
                    hasPromo = true,
                    promoDescription = "Tersedia Bebas Ongkir (4-6 hari)")

            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE

            clickApplyShipmentPromoRevamp()

            assertShipmentPromoRevamp(hasPromo = false)

            assertShipmentRevamp(
                    shippingDuration = null,
                    shippingCourier = "Pengiriman Bebas Ongkir (4-6 hari)",
                    shippingPrice = null,
                    shippingEta = null
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

            clickChangeCourierRevamp {
                chooseCourierWithText("Next Day (1 hari)")
            }

            assertShipmentPromoRevamp(
                    hasPromo = true,
                    promoDescription = "Tersedia Bebas Ongkir (4-6 hari)")

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Next Day (1 hari)",
                    shippingCourier = "JNE",
                    shippingPrice = "Rp38.000",
                    shippingEta = null
            )

            assertPayment("Rp139.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        shippingPrice = "Rp38.000",
                        isBbo = false,
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp139.000"
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
    fun happyFlow_UseRecommendationSpId() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_WITH_RECOMMENDATION_SPID_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Reguler (2-4 hari)",
                    shippingCourier = "AnterAja",
                    shippingPrice = "Rp16.000",
                    shippingEta = null
            )
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

    @Test
    fun errorFlow_GetOccCartPageReturnNoShipmentData() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_REMOVE_PROFILE_POST_NO_SHIPMENT_RESPONSE_PATH

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

            assertAddressRevamp(
                    addressName = "Address 1 - User 1 (1)",
                    addressDetail = "Address Street 1, District 1, City 1, Province 1 1",
                    isMainAddress = true
            )

            assertShipmentError(
                    errorMessage = "Durasi pengiriman tidak tersedia Ubah"
            )

            assertPaymentRevamp(paymentName = "Payment 1", paymentDetail = null)

            assertPaymentButtonEnable(false)

            clickShipmentErrorAction {
                chooseDurationWithText("Next Day (1 hari)")
            }

            assertShipmentRevamp(
                    shippingDuration = "Pengiriman Next Day (1 hari)",
                    shippingCourier = "JNE",
                    shippingPrice = "Rp38.000",
                    shippingEta = null
            )

            assertPayment("Rp139.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        shippingPrice = "Rp38.000",
                        paymentFee = "Rp1.000",
                        totalPrice = "Rp139.000"
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