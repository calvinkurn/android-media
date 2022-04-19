package com.tokopedia.hotel.evoucher.presentation.activity.mock

import android.content.Context
import com.tokopedia.hotel.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class HotelEVoucherMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_HOTEL_ORDER_DETAIL = "OrderDetailsQuery"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_HOTEL_ORDER_DETAIL,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_hotel_order_detail),
                FIND_BY_CONTAINS
        )
        return this
    }
}