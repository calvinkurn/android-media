package com.tokopedia.recharge_credit_card.mock

import android.content.Context
import com.tokopedia.recharge_credit_card.RechargeCCInstrumentTest
import com.tokopedia.recharge_credit_card.utils.ResourceUtils
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class CCMockResponseConfig: MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_BANK_LIST,
                ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_BANK_LIST),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_QUERY_MENU_DETAIL,
                ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_CATALOG_MENU_DETAIL),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_QUERY_PREFIXES,
                ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_CATALOG_PREFIXES),
                FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        private const val KEY_QUERY_BANK_LIST = "rechargeBankList"
        private const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        private const val KEY_QUERY_PREFIXES = "catalogPrefix"

        private const val PATH_RESPONSE_RECHARGE_BANK_LIST = "response_mock_data_cc_bank_list.json"
        private const val PATH_RESPONSE_RECHARGE_CATALOG_MENU_DETAIL = "response_mock_data_cc_menu_detail.json"
        private const val PATH_RESPONSE_RECHARGE_CATALOG_PREFIXES = "response_mock_data_cc_prefixes.json"
    }
}