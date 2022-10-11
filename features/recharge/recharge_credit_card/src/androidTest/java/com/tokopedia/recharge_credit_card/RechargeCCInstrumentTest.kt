package com.tokopedia.recharge_credit_card

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.recharge_credit_card.utils.ResourceUtils
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test

class RechargeCCInstrumentTest: BaseRechargeCCTest() {
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

    @Test
    fun next_button_is_enabled_on_inserted_valid_credit_card_number() {
        clientNumberWidget_checkIsButtonDisabled()
        clientNumberWidget_typeCreditCardNumber(VALID_CC_NUMBER)
        Thread.sleep(2500)
        clientNumberWidget_checkIsButtonEnabled()
    }

    @Test
    fun next_button_is_enabled_on_inserted_valid_amex_credit_card_number() {
        clientNumberWidget_checkIsButtonDisabled()
        clientNumberWidget_typeCreditCardNumber(VALID_AMEX_CC_NUMBER)
        Thread.sleep(2500)
        clientNumberWidget_checkIsButtonEnabled()
    }

    @Test
    fun next_button_is_enabled_on_type_and_clear_multiple_cc_number() {
        clientNumberWidget_checkIsButtonDisabled()
        clientNumberWidget_typeCreditCardNumber(VALID_AMEX_CC_NUMBER)
        Thread.sleep(2500)
        clientNumberWidget_checkIsButtonEnabled()

        Thread.sleep(2000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeCreditCardNumber(VALID_CC_NUMBER)
        Thread.sleep(2500)
        clientNumberWidget_checkIsButtonEnabled()
    }

    @Test
    fun next_button_is_disabled_on_inserted_invalid_credit_card_number() {
        clientNumberWidget_checkIsButtonDisabled()
        clientNumberWidget_typeCreditCardNumber(INVALID_CC_NUMBER)
        Thread.sleep(2500)
        clientNumberWidget_checkIsButtonDisabled()
        clientNumberWidget_checkIsShowInvalidNumberError()

        Thread.sleep(2000)
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_typeCreditCardNumber(INVALID_CC_NUMBER_2)
        Thread.sleep(2500)
        clientNumberWidget_checkIsButtonDisabled()
        clientNumberWidget_checkIsShowBankIsNotSupportedError()
    }

    @Test
    fun operator_icon_should_shown_on_found_prefix_credit_card_number() {
        // > 7 digit, valid number, prefix found should show operator icon
        clientNumberWidget_checkIsOperatorIconHidden()
        clientNumberWidget_typeCreditCardNumber("41111111")
        Thread.sleep(2500)
        clientNumberWidget_checkIsOperatorIconShown()
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_checkIsOperatorIconHidden()

        // invalid number, prefix not found, should hide operator icon
        clientNumberWidget_typeCreditCardNumber(INVALID_CC_NUMBER_2)
        Thread.sleep(2500)
        clientNumberWidget_checkIsOperatorIconHidden()
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_checkIsOperatorIconHidden()

        // invalid number, prefix found, should show operator icon
        clientNumberWidget_typeCreditCardNumber(INVALID_CC_NUMBER)
        Thread.sleep(2500)
        clientNumberWidget_checkIsOperatorIconShown()
        clientNumberWidget_clickClearIcon()
        clientNumberWidget_checkIsOperatorIconHidden()
    }
}