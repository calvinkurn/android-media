package com.tokopedia.recharge_pdp_emoney.utils

import android.content.Context
import com.tokopedia.recharge_pdp_emoney.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

/**
 * @author by jessica on 19/04/21
 */
class EmoneyPdpResponseConfig(val isLogin: Boolean) : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_GET_MENU_DETAIL,
                getMenuDetailResponse(context),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_PREFIX_SELECT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_recharge_catalog_prefix_select),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_QUERY_GET_PRODUCT_INPUT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_recharge_catalog_product_input),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_QUERY_GET_FAV_NUMBER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_recharge_fav_number),
                FIND_BY_CONTAINS
        )

        return this
    }

    private fun getMenuDetailResponse(context: Context): String {
        return if (isLogin) {
            InstrumentationMockHelper.getRawString(context, R.raw.response_mock_recharge_catalog_get_menu_detail)
        } else {
            InstrumentationMockHelper.getRawString(context, R.raw.response_mock_recharge_catalog_get_menu_detail_non_login)
        }
    }


    companion object {
        const val KEY_QUERY_GET_MENU_DETAIL = "rechargeCatalogMenuDetail"
        const val KEY_QUERY_PREFIX_SELECT = "rechargeCatalogPrefixSelect"
        const val KEY_QUERY_GET_PRODUCT_INPUT = "rechargeCatalogProductInput"
        const val KEY_QUERY_GET_FAV_NUMBER = "recharge_favorite_number"
    }
}