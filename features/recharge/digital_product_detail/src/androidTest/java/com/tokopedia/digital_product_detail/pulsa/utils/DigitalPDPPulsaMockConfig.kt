package com.tokopedia.digital_product_detail.pulsa.utils

import android.content.Context
import com.tokopedia.digital_product_detail.utils.ResourceUtils
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

class DigitalPDPPulsaMockConfig: MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_QUERY_MENU_DETAIL,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_MENU_DETAIL),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_RECOMMENDATION,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECOMMENDATION),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_FAVORITE_NUMBER,
            ResourceUtils.getJsonFromResource(PATH_RESPONSE_FAVORITE_NUMBER),
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
        return this
    }

    companion object {
        private const val KEY_QUERY_MENU_DETAIL = "rechargeCatalogMenuDetail"
        // need to be differentiate between recom and favnum
        private const val KEY_QUERY_RECOMMENDATION = "digiPersoGetPersonalizedItems"
        private const val KEY_QUERY_FAVORITE_NUMBER = "digiPersoGetPersonalizedItems"
        private const val KEY_QUERY_OPERATOR_PREFIX = "rechargeCatalogPrefixSelect"
        private const val KEY_QUERY_CATALOG_INPUT_MULTITAB = "rechargeCatalogProductInputMultiTab"

        private const val PATH_RESPONSE_MENU_DETAIL = "pulsa/get_menu_detail_mock.json"
        private const val PATH_RESPONSE_RECOMMENDATION = "pulsa/get_recommendation_mock.json"
        private const val PATH_RESPONSE_FAVORITE_NUMBER = "pulsa/get_favorite_number_mock.json"
        private const val PATH_RESPONSE_OPERATOR_PREFIX = "pulsa/get_operator_prefix_mock.json"
        private const val PATH_RESPONSE_CATALOG_INPUT_MULTITAB = "pulsa/get_catalog_input_multitab_mock.json"
    }
}