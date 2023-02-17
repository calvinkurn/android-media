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
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_GOCICIL_LIMITED_WALLET_AMOUNT_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GOCICIL_INSTALLMENT_OPTION_ALL_INACTIVE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GOCICIL_INSTALLMENT_OPTION_SOME_INACTIVE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GOCICIL_INSTALLMENT_OPTION_WITH_TICKER_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.interceptor.RATES_WITH_INSURANCE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class OrderSummaryPageActivityGoCicilTest {

    @get:Rule
    val activityRule = IntentsTestRule(OrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val logisticInterceptor = OneClickCheckoutInterceptor.logisticInterceptor
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
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp2.016.500", "Bayar")

            assertGoCicilInstallment("3 bulan x Rp673.867")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp2.000.000",
                        shippingPrice = "Rp15.000",
                        totalPrice = "Rp2.016.500",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1500.0,
                                showTooltip = true
                            )
                        )
                )

                assertInstallmentSummary(
                        installmentFee = "Rp0",
                        installmentTerm = "3 Bulan",
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
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH

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
                        totalPrice = "Rp2.127.606",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1500.0,
                                showTooltip = true
                            )
                        )
                )

                assertInstallmentSummary(
                        installmentFee = "Rp111.106",
                        installmentTerm = "2 Bulan",
                        installmentPerPeriod = "Rp1.066.353",
                        installmentFirstDate = "28 Februari 2022",
                        installmentLastDate = "28 April 2022"
                )
            }
        }
    }

    @Test
    fun changeQtyAndTicker() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp2.016.500", "Bayar")

            assertGoCicilInstallment("3 bulan x Rp673.867")

            clickChangeGoCicilInstallment {
                assertTicker(false)
                chooseInstallment(2)
            }

            assertGoCicilInstallment("2 bulan x Rp1.066.353")

            assertPayment("Rp2.127.606", "Bayar")

            paymentInterceptor.customGoCicilInstallmentOptionResponsePath = GOCICIL_INSTALLMENT_OPTION_WITH_TICKER_RESPONSE_PATH
            clickAddProductQuantity()

            clickChangeGoCicilInstallment {
                assertTicker(true)
                chooseInstallment(2)
            }

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp4.000.000",
                        shippingPrice = "Rp15.000",
                        totalPrice = "Rp4.127.606",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1500.0,
                                showTooltip = true
                            )
                        )
                )

                assertInstallmentSummary(
                        installmentFee = "Rp111.106",
                        installmentTerm = "2 Bulan",
                        installmentPerPeriod = "Rp2.066.353",
                        installmentFirstDate = "28 Februari 2022",
                        installmentLastDate = "28 April 2022"
                )
            }
        }
    }

    @Test
    fun errorFlow_inactiveInstallment() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH

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
                        totalPrice = "Rp4.015.000",
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

            clickChangeGoCicilInstallment {
                chooseInstallment(2)
            }

            assertGoCicilInstallment("2 bulan x Rp1.066.353")

            assertPayment("Rp4.127.606", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp4.000.000",
                        shippingPrice = "Rp15.000",
                        totalPrice = "Rp4.127.606",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1500.0,
                                showTooltip = true
                            )
                        )
                )

                assertInstallmentSummary(
                        installmentFee = "Rp111.106",
                        installmentTerm = "2 Bulan",
                        installmentPerPeriod = "Rp1.066.353",
                        installmentFirstDate = "28 Februari 2022",
                        installmentLastDate = "28 April 2022"
                )
            }
        }
    }

    @Test
    fun errorFlow_aboveLimit() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_LIMITED_WALLET_AMOUNT_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH

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
                        totalPrice = "Rp6.016.500",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1500.0,
                                showTooltip = true
                            )
                        )
                )
            }
        }
    }

    @Test
    fun errorFlow_maximumAmount() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp2.016.500", "Bayar")

            assertGoCicilInstallment("3 bulan x Rp673.867")

            clickAddProductQuantity(times = 2)

            assertProfilePaymentErrorRevamp(
                    message = "Belanjaanmu melebihi limit transaksi GoPayLaterCicil.",
                    buttonText = "Ubah"
            )

            assertPayment("Rp6.016.500", "Ganti Metode Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp6.000.000",
                        shippingPrice = "Rp15.000",
                        totalPrice = "Rp6.016.500",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1500.0,
                                showTooltip = true
                            )
                        )
                )
            }
        }
    }

    @Test
    fun errorFlow_allInactiveInstallment() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH
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
                        totalPrice = "Rp2.015.000",
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
            }
        }
    }
}
