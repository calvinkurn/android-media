package com.tokopedia.topupbills.prepaid

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.topupbills.test.R

class TelcoPrepaidMockResponseConfig : MockModelConfig() {

    companion object {
        const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        const val KEY_QUERY_FAV_NUMBER = "favouriteNumber"
        const val KEY_QUERY_PREFIX_SELECT = "telcoPrefixSelect"
        const val KEY_QUERY_PRODUCT_MULTI_TAB = "telcoProductMultiTab"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_MENU_DETAIL,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_telco_menu_detail),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_FAV_NUMBER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_telco_fav_number),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_PREFIX_SELECT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_telco_prefix_select),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_PRODUCT_MULTI_TAB,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_telco_product_multitab),
                FIND_BY_CONTAINS)
        return this
    }
}