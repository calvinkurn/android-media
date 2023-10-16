package com.tokopedia.digital_product_detail.dataplan.utils

import android.content.Context
import com.tokopedia.digital_product_detail.pulsa.utils.DigitalPDPPulsaMockConfig
import com.tokopedia.digital_product_detail.utils.ResourceUtils
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

class DigitalPDPDataPlanMockConfig: MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_QUERY_MENU_DETAIL,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_MENU_DETAIL),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CHANNEL_RECOMMENDATION,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECOMMENDATION),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CHANNEL_FAVORITE_NUMBER,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_FAVORITE_NUMBER),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CHANNEL_FAVORITE_NUMBER_WITHOUT_PREFILL,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_FAVORITE_NUMBER_WITHOUT_PREFILL),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_OPERATOR_PREFIX,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_OPERATOR_PREFIX),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_CATALOG_INPUT_MULTITAB,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_CATALOG_INPUT_MULTITAB),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_CHANNEL_INDOSAT_CHECK_BALANCE,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_INDOSAT_CHECK_BALANCE),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_SAVE_USER_BALANCE_ACCESS_TOKEN,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_SAVE_INDOSAT_ACCESS_TOKEN),
            FIND_BY_CONTAINS
        )
        return this
    }

    companion object {
        private const val KEY_QUERY_MENU_DETAIL = "rechargeCatalogMenuDetail"
        private const val KEY_QUERY_OPERATOR_PREFIX = "rechargeCatalogPrefixSelect"
        private const val KEY_QUERY_CATALOG_INPUT_MULTITAB = "rechargeCatalogProductInputMultiTab"
        private const val KEY_QUERY_SAVE_USER_BALANCE_ACCESS_TOKEN = "rechargeSaveTelcoUserBalanceAccessToken"
        private const val KEY_CHANNEL_RECOMMENDATION = "recharge_pdp_last_trx_client_number"
        private const val KEY_CHANNEL_FAVORITE_NUMBER = "favorite_number_prefill"
        private const val KEY_CHANNEL_FAVORITE_NUMBER_WITHOUT_PREFILL = "favorite_number_chips"
        private const val KEY_CHANNEL_INDOSAT_CHECK_BALANCE = "indosat_check_balance"

        private const val PATH_RESPONSE_MENU_DETAIL = "dataplan/get_menu_detail_mock.json"
        private const val PATH_RESPONSE_OPERATOR_PREFIX = "dataplan/get_operator_prefix_mock.json"
        private const val PATH_RESPONSE_CATALOG_INPUT_MULTITAB = "dataplan/get_catalog_input_multitab_mock.json"
        private const val PATH_RESPONSE_RECOMMENDATION = "dataplan/get_recommendation_mock.json"
        private const val PATH_RESPONSE_FAVORITE_NUMBER = "dataplan/get_favorite_number_mock.json"
        private const val PATH_RESPONSE_FAVORITE_NUMBER_WITHOUT_PREFILL = "dataplan/get_favorite_number_mock_without_prefill.json"
        private const val PATH_RESPONSE_INDOSAT_CHECK_BALANCE = "dataplan/get_indosat_check_balance.json"
        private const val PATH_RESPONSE_SAVE_INDOSAT_ACCESS_TOKEN = "dataplan/save_indosat_access_token.json"
    }
}
