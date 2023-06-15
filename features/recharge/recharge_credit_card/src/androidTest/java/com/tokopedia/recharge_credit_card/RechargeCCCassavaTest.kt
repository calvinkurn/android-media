package com.tokopedia.recharge_credit_card

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.recharge_credit_card.utils.ResourceUtils
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test

class RechargeCCCassavaTest : BaseRechargeCCTest() {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mActivityRule: IntentsTestRule<RechargeCCActivity> = object : IntentsTestRule<RechargeCCActivity>(RechargeCCActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
            return RouteManager.getIntent(targetContext, ApplinkConsInternalDigital.CREDIT_CARD_TEMPLATE)
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            graphqlCacheManager.deleteAll()
            setupGraphqlMockResponse {
                addMockResponse(
                    KEY_QUERY_BANK_LIST,
                    ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_BANK_LIST),
                    MockModelConfig.FIND_BY_CONTAINS
                )
                addMockResponse(
                    KEY_QUERY_MENU_DETAIL,
                    ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_CATALOG_MENU_DETAIL),
                    MockModelConfig.FIND_BY_CONTAINS
                )
                addMockResponse(
                    KEY_QUERY_PREFIXES,
                    ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_CATALOG_PREFIXES),
                    MockModelConfig.FIND_BY_CONTAINS
                )
            }
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Test
    fun validate_cassava() {
        interactWithInputField()
        intendingIntent()
        interactWithBottomSheet()
        interactWithConfirmationDialog()
        interactWithMenuTab()
        interactWithRecentTransactionSection()
        interactWithPromoSection()
        MatcherAssert.assertThat(
            cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY),
            hasAllSuccess()
        )
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
    }

    private fun interactWithInputField() {
        clientNumberWidget_typeCreditCardNumber(VALID_CC_NUMBER)
        Espresso.closeSoftKeyboard()
    }

    private fun interactWithBottomSheet() {
        Thread.sleep(2000)
        pdp_clickBankListButton()
        Thread.sleep(1000)
        bankListBottomSheet_clickCloseButton()
    }

    private fun interactWithConfirmationDialog() {
        Thread.sleep(2000)
        clientNumberWidget_clickLanjutButton()
        Thread.sleep(1000)
        confirmationDialog_clickCloseButton()

        Thread.sleep(2000)
        clientNumberWidget_clickLanjutButton()
        Thread.sleep(1000)
        confirmationDialog_clickConfirmButton()
    }

    private fun interactWithMenuTab() {
        Thread.sleep(3000)
        tabLayout_clickTabWithText(PROMO_LIST_LABEL)
        Thread.sleep(3000)
        tabLayout_clickTabWithText(RECENT_TRANSACTION_LABEL)
    }

    private fun interactWithRecentTransactionSection() {
        tabLayout_clickTabWithText(RECENT_TRANSACTION_LABEL)
        recentTransaction_scrollToPosition(0)
        recentTransaction_clickItemWithPosition(0)
        confirmationDialog_clickCloseButton()
    }

    private fun interactWithPromoSection() {
        tabLayout_clickTabWithText(PROMO_LIST_LABEL)
        promo_scrollToPosition(0)
        promo_clickCopyPromoWithPosition(0)
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_credit_card.json"
        private const val PROMO_LIST_LABEL = "Promo"
        private const val RECENT_TRANSACTION_LABEL = "Transaksi Terakhir"
    }
}
