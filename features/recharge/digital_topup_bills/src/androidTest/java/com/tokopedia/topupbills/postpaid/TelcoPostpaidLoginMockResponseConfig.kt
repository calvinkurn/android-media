package com.tokopedia.topupbills.postpaid

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.topupbills.test.R

class TelcoPostpaidLoginMockResponseConfig : MockModelConfig() {

    companion object {
        const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        const val KEY_QUERY_FAV_NUMBER = "favouriteNumber"
        const val KEY_QUERY_PREFIX_SELECT = "telcoPrefixSelect"
        const val KEY_QUERY_ENQUIRY = "enquiry"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_MENU_DETAIL,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_postpaid_menu_detail_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_FAV_NUMBER,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_postpaid_fav_number_login),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_PREFIX_SELECT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_postpaid_prefix_select),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_ENQUIRY,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_postpaid_enquiry),
                FIND_BY_CONTAINS)
        return this
    }
}