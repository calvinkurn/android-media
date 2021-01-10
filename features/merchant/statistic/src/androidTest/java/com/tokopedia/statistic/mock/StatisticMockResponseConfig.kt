package com.tokopedia.statistic.mock

import android.content.Context
import com.tokopedia.statistic.common.TestConst
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

/**
 * Created By @ilhamsuaib on 07/01/21
 */

class StatisticMockResponseConfig : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(TestConst.MockResponseKey.USER_ROLE, InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_statistic_user_role), FIND_BY_CONTAINS)
        addMockResponse(TestConst.MockResponseKey.GET_SELLER_DASHBOARD_PAGE_LAYOUT, InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_statistic_lyaout), FIND_BY_CONTAINS)
        addMockResponse(TestConst.MockResponseKey.FETCH_CARD_WIDGET_DATA, InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_statistic_card_widget), FIND_BY_CONTAINS)
        addMockResponse(TestConst.MockResponseKey.FETCH_LINE_GRAPH_WIDGET_DATA, InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_statistic_line_graph_widget), FIND_BY_CONTAINS)
        addMockResponse(TestConst.MockResponseKey.FETCH_PIE_CHART_WIDGET_DATA, InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_statistic_pie_chart_widget), FIND_BY_CONTAINS)
        addMockResponse(TestConst.MockResponseKey.FETCH_BAR_CHART_WIDGET_DATA, InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_statistic_bar_chart_widget), FIND_BY_CONTAINS)
        addMockResponse(TestConst.MockResponseKey.GET_TICKER, InstrumentationMockHelper.getRawString(context, com.tokopedia.instrumentation.test.R.raw.response_mock_data_seller_home_get_ticker), FIND_BY_CONTAINS)
        return this
    }
}