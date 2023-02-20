package com.tokopedia.recharge_credit_card

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.recharge_credit_card.utils.ResourceUtils
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeCCCassavaTest: BaseRechargeCCTest() {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mActivityRule: IntentsTestRule<RechargeCCActivity> = object: IntentsTestRule<RechargeCCActivity>(RechargeCCActivity::class.java) {
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
        clientNumberWidget_typeCreditCardNumber(VALID_CC_NUMBER)
        interactWithBottomSheet()
        interactWithConfirmationDialog()
        MatcherAssert.assertThat(
            cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY),
            hasAllSuccess()
        )
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

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_credit_card.json"
    }
}