package com.tokopedia.search.testcase

import android.content.Context
import com.tokopedia.search.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal class SearchMockModelConfigKeywordKfc: MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        val mapMockResponse = createMapOfMockResponse(context)

        for ((key, value) in mapMockResponse.entries) addMockResponse(key, value, FIND_BY_CONTAINS)

        return this
    }

    private fun createMapOfMockResponse(context: Context) = mapOf(
            "SearchProduct" to getRawString(context, R.raw.search_product_response_keyword_kfc)
    )
}