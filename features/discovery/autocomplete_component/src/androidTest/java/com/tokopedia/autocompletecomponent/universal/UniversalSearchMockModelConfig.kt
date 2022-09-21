package com.tokopedia.autocompletecomponent.universal

import android.content.Context
import com.tokopedia.autocompletecomponent.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

internal class UniversalSearchMockModelConfig: MockModelConfig() {
    private val mockModel: Int = R.raw.universal_search_common_response
    private val query: String = "universe_universal_search"

    override fun createMockModel(context: Context): MockModelConfig {
        val mapMockResponse = createMapOfMockResponse(context)

        for ((key, value) in mapMockResponse.entries) addMockResponse(key, value, FIND_BY_CONTAINS)

        return this
    }

    private fun createMapOfMockResponse(context: Context) = mapOf(
        query to InstrumentationMockHelper.getRawString(context, mockModel)
    )
}