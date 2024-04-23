package com.tokopedia.search

import android.content.Context
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal class SearchMockModelConfig(
    private val mockModel: Int = R.raw.search_product_common_response
): MockModelConfig() {
    init {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            RollenceKey.REVERSE_PRODUCT_CARD,
            RollenceKey.REVERSE_PRODUCT_CARD_V4
        )
    }
    override fun createMockModel(context: Context): MockModelConfig {
        val mapMockResponse = createMapOfMockResponse(context)

        for ((key, value) in mapMockResponse.entries) addMockResponse(key, value, FIND_BY_CONTAINS)

        return this
    }

    private fun createMapOfMockResponse(context: Context) = mapOf(
        "SearchProduct" to getRawString(context, mockModel),
        "add_to_cart_v2" to getRawString(context, R.raw.search_product_atc_success)
    )
}
