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
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GOCICIL_INSTALLMENT_OPTION_ALL_INACTIVE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GOCICIL_INSTALLMENT_OPTION_SOME_INACTIVE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityGoCicilTest {

    @get:Rule
    val activityRule = IntentsTestRule(OrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val paymentInterceptor = OneClickCheckoutInterceptor.paymentInterceptor

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
    fun happyFlow() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp2.016.500", "Bayar")

            assertGoCicilInstallment("3 bulan x Rp673.867")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp2.000.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        paymentFee = "Rp1.500",
                        totalPrice = "Rp2.016.500"
                )

                assertInstallmentSummary(
                        installmentFee = "Rp0",
                        installmentTerm = "3 months",
                        installmentPerPeriod = "Rp673.867",
                        installmentFirstDate = "28 Februari 2022",
                        installmentLastDate = "28 Mei 2022"
                )
            }
        }
    }

    @Test
    fun changeInstallment() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp2.016.500", "Bayar")

            assertGoCicilInstallment("3 bulan x Rp673.867")

            clickChangeGoCicilInstallment {
                chooseInstallment(2)
            }

            assertGoCicilInstallment("2 bulan x Rp1.066.353")

            assertPayment("Rp2.127.606", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp2.000.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        paymentFee = "Rp1.500",
                        totalPrice = "Rp2.127.606"
                )

                assertInstallmentSummary(
                        installmentFee = "Rp111.106",
                        installmentTerm = "2 months",
                        installmentPerPeriod = "Rp1.066.353",
                        installmentFirstDate = "28 Februari 2022",
                        installmentLastDate = "28 April 2022"
                )
            }
        }
    }

    @Test
    fun errorFlow_inactiveInstallment() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp2.016.500", "Bayar")

            assertGoCicilInstallment("3 bulan x Rp673.867")

            paymentInterceptor.customGoCicilInstallmentOptionResponsePath = GOCICIL_INSTALLMENT_OPTION_SOME_INACTIVE_RESPONSE_PATH

            clickAddProductQuantity()

            assertInstallmentErrorRevamp(
                    errorMessage = "Oops periode cicilan yang kamu pilih sebelumnya tidak tersedia. Pilih periode yang lain, ya.",
                    errorAction = null
            )

            assertPaymentButtonEnable(false)

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp4.000.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp4.015.000"
                )
                closeBottomSheet()
            }

            clickChangeGoCicilInstallment {
                chooseInstallment(2)
            }

            assertGoCicilInstallment("2 bulan x Rp1.066.353")

            assertPayment("Rp4.127.606", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp4.000.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        paymentFee = "Rp1.500",
                        totalPrice = "Rp4.127.606"
                )

                assertInstallmentSummary(
                        installmentFee = "Rp111.106",
                        installmentTerm = "2 months",
                        installmentPerPeriod = "Rp1.066.353",
                        installmentFirstDate = "28 Februari 2022",
                        installmentLastDate = "28 April 2022"
                )
            }
        }
    }

    @Test
    fun errorFlow_maximumAmount() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp2.016.500", "Bayar")

            assertGoCicilInstallment("3 bulan x Rp673.867")

            clickAddProductQuantity(times = 2)

            assertProfilePaymentErrorRevamp(
                    message = "Oops, limit nggak cukup. Coba metode bayar lain, ya.",
                    buttonText = null
            )

            assertPayment("Rp6.016.500", "Ganti Metode Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp6.000.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        paymentFee = "Rp1.500",
                        totalPrice = "Rp6.016.500"
                )
            }
        }
    }

    @Test
    fun errorFlow_allInactiveInstallment() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH
        paymentInterceptor.customGoCicilInstallmentOptionResponsePath = GOCICIL_INSTALLMENT_OPTION_ALL_INACTIVE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertProfilePaymentErrorRevamp(
                    message = "Oops, metode pembayaran ini nggak tersedia. Coba pilih yang lain, ya.",
                    buttonText = null
            )

            assertPayment("Rp2.015.000", "Ganti Metode Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp2.000.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp2.015.000"
                )
            }
        }
    }
}