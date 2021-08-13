package com.tokopedia.promogamification.common

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.promogamification.common.test.R

class FloatingEggMockResponse : com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                "gamiFloating",
                InstrumentationMockHelper.getRawString(context, R.raw.floating_egg),
                FIND_BY_QUERY_NAME)

        return this
    }
}