package com.tokopedia.autocomplete

import android.content.Context
import com.tokopedia.autocomplete.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal class AutocompleteMockModelConfig(
    private val mockModel: Int = R.raw.initial_state_common_response,
    private val query: String = "universe_initial_state"
): MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        val mapMockResponse = createMapOfMockResponse(context)

        for ((key, value) in mapMockResponse.entries) addMockResponse(key, value, FIND_BY_CONTAINS)

        return this
    }

    private fun createMapOfMockResponse(context: Context) = mapOf(
            query to getRawString(context, mockModel)
    )
}