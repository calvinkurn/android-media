package com.tokopedia.flight.orderdetail.presentation.activity

import android.content.Context
import com.tokopedia.flight.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

/**
 * @author by furqan on 23/12/2020
 */
class FlightOrderDetailMockResponse : MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_CONTAINS_ORDER_DETAIL,
                getRawString(context, R.raw.response_mock_data_flight_order_detail),
                FIND_BY_CONTAINS
        )
        addMockResponse(
                KEY_CONTAINS_ORDER_DETAIL_GET_E_TICKET,
                getRawString(context, R.raw.response_mock_data_flight_order_detail_e_ticket),
                FIND_BY_CONTAINS
        )

        return this
    }

    companion object {
        private const val KEY_CONTAINS_ORDER_DETAIL = "flightGetOrderDetail"
        private const val KEY_CONTAINS_ORDER_DETAIL_GET_E_TICKET = "flightGetETicket"
    }
}