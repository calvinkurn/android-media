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
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_CREDIT_CARD_ERROR_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_CREDIT_CARD_EXPIRED_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_CREDIT_CARD_MIN_AMOUNT_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_CREDIT_CARD_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_DEBIT_CARD_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_MULTIPLE_CREDIT_CARD_DELETED_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class OrderSummaryPageActivityCreditCardRevampTest {

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
    fun happyFlow_ZeroMonthDirectCheckout() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            assertPayment("Rp116.725", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp116.725",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1725.0,
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
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            clickAddProductQuantity(times = 4)

            clickChangeInstallmentRevamp {
                chooseInstallment(3)
            }

            assertInstallmentRevamp("3 Bulan x Rp175.959")

            assertPayment("Rp527.875", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp500.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp527.875",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 12875.0,
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
    fun errorFlow_CreditCardExpired() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_EXPIRED_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp(null)

            assertProfilePaymentErrorRevamp("Kedaluwarsa", "Ubah")

            assertPaymentButtonEnable(false)
        }
    }

    @Test
    fun errorFlow_CreditCardDeleted() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MULTIPLE_CREDIT_CARD_DELETED_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp(null)

            assertProfilePaymentErrorRevamp("Kartu Kredit telah dihapus", "Ubah")

            assertPaymentButtonEnable(false)
        }
    }

    @Test
    fun errorFlow_CreditCardScroogeError() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_ERROR_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp(null)

            assertPayment("Rp116.725", "Ganti Metode Bayar")

            assertProfilePaymentErrorRevamp("Maaf, metode bayar dengan kartu kredit sedang tidak bisa digunakan. Silakan pilih metode pembayaran lain.", null)
        }
    }

    @Test
    fun errorFlow_InstallmentMinimumAmount() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            clickAddProductQuantity(times = 4)

            clickChangeInstallmentRevamp {
                chooseInstallment(3)
            }

            assertInstallmentRevamp("3 Bulan x Rp175.959")

            clickMinusProductQuantity()

            assertInstallmentRevamp("3 Bulan x Rp141.792")

            assertPayment("Rp425.375", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp400.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp425.375",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 10375.0,
                                showTooltip = true
                            )
                        )
                )
                closeBottomSheet()
            }

            pay()

            assertPaymentButtonEnable(false)

            assertInstallmentErrorRevamp(
                    errorMessage = "Cicilan tidak tersedia.",
                    errorAction = "Ubah"
            )

            clickInstallmentErrorActionRevamp {
                chooseInstallment(0)
            }

            assertInstallmentRevamp("Bayar Penuh")

            assertPaymentButtonEnable(true)

            // Temporary to prevent onClick not triggered
            Thread.sleep(1000)
        } pay {
            assertGoToPayment(
                    redirectUrl = "https://www.tokopedia.com/payment",
                    queryString = "transaction_id=123",
                    method = "POST"
            )
        }
    }

    @Test
    fun errorFlow_CreditCardMinimumAmount() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_CREDIT_CARD_MIN_AMOUNT_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertPayment("Rp25.375", "Ganti Metode Bayar")

            assertProfilePaymentErrorRevamp("Belanjaanmu kurang dari min. transaksi Kartu Kredit.", "Ubah")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp10.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp25.375",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 375.0,
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
    fun happyFlow_DebitCard() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_DEBIT_CARD_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp(null)

            assertPayment("Rp116.725", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                        productPrice = "Rp100.000",
                        shippingPrice = "Rp15.000",
                        insurancePrice = "Rp0",
                        totalPrice = "Rp116.725",
                        isInstallment = true,
                        paymentFeeDetails = listOf(
                            OrderPaymentFee(
                                title = "Biaya Layanan",
                                tooltipInfo = "Biaya ini dikenakan khusus pembayaran dengan metode tertentu.",
                                fee = 1725.0,
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