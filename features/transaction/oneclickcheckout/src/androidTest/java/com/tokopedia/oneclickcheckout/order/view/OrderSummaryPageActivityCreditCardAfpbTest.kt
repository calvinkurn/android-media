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
import com.tokopedia.oneclickcheckout.common.interceptor.CREDIT_CARD_TENOR_LIST_ALL_ENABLED_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_CREDIT_CARD_AFPB_PAYMENT_FEE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_CREDIT_CARD_AFPB_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@UiTest
class OrderSummaryPageActivityCreditCardAfpbTest {

    @get:Rule
    var activityRule = IntentsTestRule(TestOrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val paymentInterceptor = OneClickCheckoutInterceptor.paymentInterceptor

    @Before
    fun setup() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
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
    fun happyFlow_ZeroMonthDirectCheckout() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_AFPB_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            assertPayment("Rp516.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp500.000",
                        shippingPrice = "Rp16.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp516.000",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 0.0,
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

    @Test
    fun happyFlow_ChangeInstallment() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_AFPB_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            clickChangeInstallmentRevamp {
                chooseInstallment(3)
            }

            assertInstallmentRevamp("3 Bulan x Rp167.000")

            assertPayment("Rp517.899", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp500.000",
                        shippingPrice = "Rp16.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp517.899",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1899.0,
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

    @Test
    fun happyFlow_ChangeInstallment_WithLargerAmount() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_AFPB_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            paymentInterceptor.customCreditCardTenorListResponsePath = CREDIT_CARD_TENOR_LIST_ALL_ENABLED_RESPONSE_PATH
            clickAddProductQuantity(times = 1)

            clickChangeInstallmentRevamp {
                chooseInstallment(12)
            }

            assertInstallmentRevamp("12 Bulan x Rp85.000")

            assertPayment("Rp1.020.599", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp1.000.000",
                        shippingPrice = "Rp16.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp1.020.599",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 4599.0,
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

    @Test
    fun errorFlow_ChangeQty_FailGetTenorList() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_AFPB_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            paymentInterceptor.customCreditCardTenorListThrowable = IOException()
            clickAddProductQuantity(times = 1)

            assertInstallmentRevamp("Pilih Bayar Penuh / Cicilan")

            assertPaymentButtonEnable(false)

            paymentInterceptor.customCreditCardTenorListThrowable = null
            paymentInterceptor.customCreditCardTenorListResponsePath = CREDIT_CARD_TENOR_LIST_ALL_ENABLED_RESPONSE_PATH
            clickChangeInstallmentRevamp {
                chooseInstallment(12)
            }

            assertInstallmentRevamp("12 Bulan x Rp85.000")

            assertPayment("Rp1.020.599", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp1.000.000",
                        shippingPrice = "Rp16.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp1.020.599",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 4599.0,
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