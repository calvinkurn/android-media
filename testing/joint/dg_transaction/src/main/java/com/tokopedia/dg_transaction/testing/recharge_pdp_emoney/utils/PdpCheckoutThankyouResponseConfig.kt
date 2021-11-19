package com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.utils

import android.content.Context
import com.tokopedia.dg_transaction.testing.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class PdpCheckoutThankyouResponseConfig(val isLogin: Boolean) : MockModelConfig() {

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
        addMockResponse(
            KEY_DG_CHECKOUT_GET_CART,
            InstrumentationMockHelper.getRawString(context, R.raw.response_mock_get_cart),
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
        /* recharge_emoney_pdp */
        const val KEY_QUERY_GET_MENU_DETAIL = "rechargeCatalogMenuDetail"
        const val KEY_QUERY_PREFIX_SELECT = "rechargeCatalogPrefixSelect"
        const val KEY_QUERY_GET_PRODUCT_INPUT = "rechargeCatalogProductInput"
        const val KEY_QUERY_GET_FAV_NUMBER = "recharge_favorite_number"

        /* digital_checkout */
        const val KEY_DG_CHECKOUT_GET_CART = "rechargeGetCart"
    }
}