package com.tokopedia.buyerorder

import android.content.Context
import com.tokopedia.buyerorder.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal class UohMockModelConfig: MockModelConfig() {
    companion object {
        const val KEY_UOH_ORDERS = "uohOrders"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_UOH_ORDERS,
                getRawString(context, R.raw.response_mock_uoh_orders_with_selesai_buttons),
                FIND_BY_QUERY_NAME)

        return this
    }
}