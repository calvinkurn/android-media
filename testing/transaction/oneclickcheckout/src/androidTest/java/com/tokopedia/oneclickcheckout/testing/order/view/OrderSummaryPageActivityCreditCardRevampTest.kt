package com.tokopedia.oneclickcheckout.testing.order.view

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
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
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_CREDIT_CARD_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp("Bayar Penuh")

            assertPayment("Rp116.725", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                    productPrice = "Rp100.000",
                    shippingPrice = "Rp15.000",
                    paymentFee = "Rp1.725",
                    totalPrice = "Rp116.725"
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
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_CREDIT_CARD_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

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
                    paymentFee = "Rp12.875",
                    totalPrice = "Rp527.875"
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
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_CREDIT_CARD_EXPIRED_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp(null)

            assertProfilePaymentErrorRevamp("Kedaluwarsa", "Ubah")

            assertPaymentButtonEnable(false)
        }
    }

    @Test
    fun errorFlow_CreditCardDeleted() {
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_MULTIPLE_CREDIT_CARD_DELETED_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp(null)

            assertProfilePaymentErrorRevamp("Kartu Kredit telah dihapus", "Ubah")

            assertPaymentButtonEnable(false)
        }
    }

    @Test
    fun errorFlow_CreditCardScroogeError() {
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_CREDIT_CARD_ERROR_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp(null)

            assertPayment("Rp116.725", "Ganti Metode Bayar")

            assertProfilePaymentErrorRevamp(
                "Maaf, metode bayar dengan kartu kredit sedang tidak bisa digunakan. Silakan pilih metode pembayaran lain.",
                null
            )
        }
    }

    @Test
    fun errorFlow_InstallmentMinimumAmount() {
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_CREDIT_CARD_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

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
                    paymentFee = "Rp10.375",
                    totalPrice = "Rp425.375"
                )
                closeBottomSheet()
            }

            pay()

            assertPaymentButtonEnable(false)

            assertInstallmentErrorRevamp()

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
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_CREDIT_CARD_MIN_AMOUNT_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertPayment("Rp25.375", "Ganti Metode Bayar")

            assertProfilePaymentErrorRevamp(
                "Belanjaanmu kurang dari min. transaksi Kartu Kredit.",
                "Ubah"
            )

            clickButtonOrderDetail {
                assertSummary(
                    productPrice = "Rp10.000",
                    shippingPrice = "Rp15.000",
                    paymentFee = "Rp375",
                    totalPrice = "Rp25.375"
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
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_DEBIT_CARD_REVAMP_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {

            assertInstallmentRevamp(null)

            assertPayment("Rp116.725", "Bayar")

            clickButtonOrderDetail {
                assertSummary(
                    productPrice = "Rp100.000",
                    shippingPrice = "Rp15.000",
                    paymentFee = "Rp1.725",
                    totalPrice = "Rp116.725"
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