package com.tokopedia.rechargegeneral

import android.content.Context
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.rechargegeneral.test.R

class RechargeGeneralLoginMockResponseConfig(
        private val product: RechargeGeneralProduct
) : MockModelConfig() {

    companion object {
        const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        const val KEY_QUERY_FAV_NUMBER = "favouriteNumber"
        const val KEY_QUERY_OPERATOR_SELECT_GROUP = "rechargeCatalogOperatorSelectGroup"
        const val KEY_QUERY_CATALOG_DYNAMIC_INPUT = "rechargeCatalogDynamicInput"
        const val KEY_QUERY_CATALOG_PLUGIN = "rechargeCatalogPlugin"

    }

    override fun createMockModel(context: Context): MockModelConfig {
        return when (product) {
            RechargeGeneralProduct.PBB -> createMockProductPbb(context)
            RechargeGeneralProduct.LISTRIK -> createMockProductListrik(context)
            RechargeGeneralProduct.AIR_PDAM -> createMockProductAirPDAM(context)
            RechargeGeneralProduct.ANGSURAN_KREDIT -> createMockProductAngsuranKredit(context)
        }
    }

    private fun createMockProductPbb(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_MENU_DETAIL,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_menu_detail_pbb_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_FAV_NUMBER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_fav_number_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_OPERATOR_SELECT_GROUP,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_operator_select_group_pbb),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_CATALOG_DYNAMIC_INPUT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_dynamic_input_pbb),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_CATALOG_PLUGIN,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_plugin),
                FIND_BY_CONTAINS)
        return this
    }

    private fun createMockProductListrik(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_MENU_DETAIL,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_menu_detail_listrik_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_FAV_NUMBER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_fav_number_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_OPERATOR_SELECT_GROUP,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_operator_select_group_listrik),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_CATALOG_DYNAMIC_INPUT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_dynamic_input_listrik_token),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_CATALOG_PLUGIN,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_plugin),
                FIND_BY_CONTAINS)
        return this
    }

    private fun createMockProductAirPDAM(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_MENU_DETAIL,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_menu_detail_airpdam_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_FAV_NUMBER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_fav_number_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_OPERATOR_SELECT_GROUP,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_operator_select_group_airpdam),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_CATALOG_DYNAMIC_INPUT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_dynamic_input_airpdam),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_CATALOG_PLUGIN,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_plugin),
                FIND_BY_CONTAINS)
        return this
    }

    private fun createMockProductAngsuranKredit(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_MENU_DETAIL,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_menu_detail_angsuran_kredit_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_FAV_NUMBER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_fav_number_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_OPERATOR_SELECT_GROUP,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_operator_select_group_angsuran_kredit),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_CATALOG_DYNAMIC_INPUT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_dynamic_input_angsuran_kredit),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_CATALOG_PLUGIN,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_general_template_catalog_plugin),
                FIND_BY_CONTAINS)
        return this
    }
}