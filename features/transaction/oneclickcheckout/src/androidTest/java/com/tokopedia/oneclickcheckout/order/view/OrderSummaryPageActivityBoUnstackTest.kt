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
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.interceptor.RATES_ETA_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.RATES_NO_TICKER_BO_RESPONSE
import com.tokopedia.oneclickcheckout.common.interceptor.VALIDATE_USE_PROMO_REVAMP_CASHBACK_FULL_APPLIED_RESPONSE
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_DATA_RESULT
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class OrderSummaryPageActivityBoUnstackTest {

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
    fun usePromoBoClashing_dontShowTickerBo() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH
        logisticInterceptor.customRatesResponsePath = RATES_ETA_RESPONSE_PATH
        promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_CASHBACK_FULL_APPLIED_RESPONSE

        val validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "CASHBACK",
                        type = "type",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertShipmentPromoRevamp(
                hasPromo = true,
                promoTitle = "Tersedia Bebas Ongkir",
                promoSubtitle = "Estimasi tiba besok - 3 Feb"
            )

            logisticInterceptor.customRatesResponsePath = RATES_NO_TICKER_BO_RESPONSE

            intending(anyIntent()).respondWith(
                ActivityResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                putExtra(ARGS_VALIDATE_USE_DATA_RESULT, validateUsePromoRevampUiModel)
            }
                )
            )
            clickButtonPromo()
            assertShipmentPromoRevamp(false)
        }
    }
}