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
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_CREDIT_CARD_AFPB_PAYMENT_FEE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_GOCICIL_PAYMENT_FEE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_PAYMENT_FEE_2_THOUSAND_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.interceptor.RATES_WITH_INSURANCE_RESPONSE_PATH
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
class OrderSummaryPageActivityPaymentFeeTest {

    @get:Rule
    var activityRule = IntentsTestRule(TestOrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val logisticInterceptor = OneClickCheckoutInterceptor.logisticInterceptor
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
    fun happyFlow_GocicilWithPaymentFee() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_PAYMENT_FEE_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp2.017.500", "Bayar")

            assertGoCicilInstallment("3 bulan x Rp673.867")

            clickButtonOrderDetail {
                assertSummary(
                    productPrice = "Rp2.000.000",
                    shippingPrice = "Rp15.000",
                    totalPrice = "Rp2.017.500",
                    isInstallment = true,
                    paymentFeeDetails = listOf(
                        OrderPaymentFee(
                            title = "Biaya Layanan",
                            tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                            fee = 1500.0,
                            showTooltip = true
                        ),
                        OrderPaymentFee(
                            fee = 1000.0,
                            showSlashed = true,
                            showTooltip = true,
                            slashedFee = 2000,
                            title = "Biaya Jasa Aplikasi",
                            tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu.",
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

                clickPaymentFeeInfo(index = 0) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya layanan",
                        tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu."
                    )
                }

                closeBottomSheet()

                clickPaymentFeeInfo(index = 1) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya jasa aplikasi",
                        tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu."
                    )
                }
            }
        }
    }

    @Test
    fun happyFlow_GocicilWithDynamicPaymentFee() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_GOCICIL_PAYMENT_FEE_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_WITH_INSURANCE_RESPONSE_PATH
        paymentInterceptor.customGetPaymentFeeResponsePath = GET_PAYMENT_FEE_2_THOUSAND_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPayment("Rp2.019.500", "Bayar")

            assertGoCicilInstallment("3 bulan x Rp673.867")

            clickButtonOrderDetail {
                assertSummary(
                    productPrice = "Rp2.000.000",
                    shippingPrice = "Rp15.000",
                    totalPrice = "Rp2.019.500",
                    isInstallment = true,
                    paymentFeeDetails = listOf(
                        OrderPaymentFee(
                            title = "Biaya Layanan",
                            tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                            fee = 1500.0,
                            showTooltip = true
                        ),
                        OrderPaymentFee(
                            fee = 1000.0,
                            showSlashed = true,
                            showTooltip = true,
                            slashedFee = 2000,
                            title = "Biaya Jasa Aplikasi",
                            tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu.",
                        ),
                        OrderPaymentFee(
                            fee = 2000.0,
                            showSlashed = false,
                            showTooltip = true,
                            slashedFee = 0,
                            title = "Biaya Jasa Aplikasi",
                            tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu.",
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

                clickPaymentFeeInfo(index = 0) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya layanan",
                        tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu."
                    )
                }

                closeBottomSheet()

                clickPaymentFeeInfo(index = 1) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya jasa aplikasi",
                        tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu."
                    )
                }

                closeBottomSheet()

                clickPaymentFeeInfo(index = 2) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya jasa aplikasi",
                        tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu."
                    )
                }
            }
        }
    }

    @Test
    fun happyFlow_CreditCardWithPaymentFee() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_AFPB_PAYMENT_FEE_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            assertPayment("Rp517.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                    productPrice = "Rp500.000",
                    shippingPrice = "Rp16.000",
                    insurancePrice = "Rp0",
                    totalPrice = "Rp517.000",
                    isInstallment = true,
                    paymentFeeDetails = listOf(
                        OrderPaymentFee(
                            title = "Biaya Layanan",
                            tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                            fee = 0.0,
                            showTooltip = true
                        ),
                        OrderPaymentFee(
                            fee = 1000.0,
                            showSlashed = true,
                            showTooltip = true,
                            slashedFee = 2000,
                            title = "Biaya Jasa Aplikasi",
                            tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu.",
                        )
                    )
                )
                clickPaymentFeeInfo(index = 0) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya layanan",
                        tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu."
                    )
                }

                closeBottomSheet()

                clickPaymentFeeInfo(index = 1) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya jasa aplikasi",
                        tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu."
                    )
                }
                closeBottomSheet()

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
    fun happyFlow_CreditCardWithDynamicPaymentFee() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_AFPB_PAYMENT_FEE_RESPONSE_PATH
        paymentInterceptor.customGetPaymentFeeResponsePath = GET_PAYMENT_FEE_2_THOUSAND_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            assertPayment("Rp519.000", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                    productPrice = "Rp500.000",
                    shippingPrice = "Rp16.000",
                    insurancePrice = "Rp0",
                    totalPrice = "Rp519.000",
                    isInstallment = true,
                    paymentFeeDetails = listOf(
                        OrderPaymentFee(
                            title = "Biaya Layanan",
                            tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                            fee = 0.0,
                            showTooltip = true
                        ),
                        OrderPaymentFee(
                            fee = 1000.0,
                            showSlashed = true,
                            showTooltip = true,
                            slashedFee = 2000,
                            title = "Biaya Jasa Aplikasi",
                            tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu.",
                        ),
                        OrderPaymentFee(
                            fee = 2000.0,
                            showSlashed = false,
                            showTooltip = true,
                            slashedFee = 0,
                            title = "Biaya Jasa Aplikasi",
                            tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu.",
                        )
                    )
                )
                clickPaymentFeeInfo(index = 0) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya layanan",
                        tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu."
                    )
                }

                closeBottomSheet()

                clickPaymentFeeInfo(index = 1) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya jasa aplikasi",
                        tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu."
                    )
                }
                closeBottomSheet()

                clickPaymentFeeInfo(index = 2) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya jasa aplikasi",
                        tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu."
                    )
                }
                closeBottomSheet()

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
    fun mixedFlow_FailedGetDynamicPaymentFee() {
        paymentInterceptor.customGetPaymentFeeThrowable = IOException()

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertPaymentButtonEnable(false)
            assertPaymentFeeErrorTicker(true)

            clickAddProductQuantity()

            assertPaymentButtonEnable(false)
            assertPaymentFeeErrorTicker(true)

            paymentInterceptor.customGetPaymentFeeThrowable = null
            clickMinusProductQuantity()

            assertPaymentButtonEnable(true)
            assertPaymentFeeErrorTicker(false)

            clickButtonOrderDetail {
                assertSummary(
                    productPrice = "Rp100.000",
                    shippingPrice = "Rp15.000",
                    insurancePrice = "Rp0",
                    totalPrice = "Rp116.000",
                    isInstallment = true,
                    paymentFeeDetails = listOf(
                        OrderPaymentFee(
                            title = "Biaya Layanan",
                            tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                            fee = 1000.0,
                            showTooltip = true
                        )
                    )
                )
                clickPaymentFeeInfo(index = 0) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya layanan",
                        tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu."
                    )
                }

                closeBottomSheet()

                closeBottomSheet()
            }

            paymentInterceptor.customGetPaymentFeeResponsePath = GET_PAYMENT_FEE_2_THOUSAND_RESPONSE_PATH
            clickAddProductQuantity()

            assertPaymentButtonEnable(true)
            assertPaymentFeeErrorTicker(false)

            clickButtonOrderDetail {
                assertSummary(
                    productPrice = "Rp200.000",
                    shippingPrice = "Rp15.000",
                    insurancePrice = "Rp0",
                    totalPrice = "Rp218.000",
                    isInstallment = true,
                    paymentFeeDetails = listOf(
                        OrderPaymentFee(
                            title = "Biaya Layanan",
                            tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                            fee = 1000.0,
                            showTooltip = true
                        ),
                        OrderPaymentFee(
                            fee = 2000.0,
                            showSlashed = false,
                            showTooltip = true,
                            slashedFee = 0,
                            title = "Biaya Jasa Aplikasi",
                            tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu.",
                        )
                    )
                )
                clickPaymentFeeInfo(index = 0) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya layanan",
                        tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu."
                    )
                }

                closeBottomSheet()
                clickPaymentFeeInfo(index = 1) {
                    assertPaymentFeeBottomSheetInfo(
                        tooltipTitle = "Tentang biaya jasa aplikasi",
                        tooltipInfo = "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu."
                    )
                }

                closeBottomSheet()

                closeBottomSheet()
            }
        }
    }
}
