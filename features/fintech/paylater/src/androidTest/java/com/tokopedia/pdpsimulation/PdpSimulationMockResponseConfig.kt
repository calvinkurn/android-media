package com.tokopedia.pdpsimulation

import android.content.Context
import com.tokopedia.pdpsimulation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

internal open class PdpSimulationMockResponseConfig : MockModelConfig() {

    companion object {
        const val SIMULATION_V3_KEY = "paylater_getSimulationV3"
        const val PRODUCT_V3_KEY = "GetProductV3"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            PRODUCT_V3_KEY,
            InstrumentationMockHelper.getRawString(context, R.raw.product_detail_response),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            SIMULATION_V3_KEY,
            InstrumentationMockHelper.getRawString(context, R.raw.simulationv3response),
            FIND_BY_CONTAINS
        )
        return this
    }

}