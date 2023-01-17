package com.tokopedia.macrobenchmark_util.env.mock.config

import android.content.Context
import android.content.Intent
import com.tokopedia.macrobenchmark_util.env.mock.MockModelConfig

/**
 * Created by yovi.putra on 08/11/22"
 * Project name: android-tokopedia-core
 **/

class ProductDetailMockResponseConfig : MockModelConfig() {
    companion object {
        const val KEY_PDP_LAYOUT = "pdpGetLayout"
        const val KEY_PDP_DATA = "pdpGetData"
        const val KEY_PDP_RECOMM = "productRecommendation"
        const val KEY_PDP_PLAY = "playGetWidgetV2"
    }

    override fun createMockModel(context: Context, intent: Intent): MockModelConfig {
        val mapMockResponse = createMapOfMockResponse(intent)

        for ((key, value) in mapMockResponse.entries) addMockResponse(
            key = key,
            value = value.orEmpty(),
            findType = FIND_BY_CONTAINS
        )

        return this
    }

    private fun createMapOfMockResponse(intent: Intent) = mapOf(
        KEY_PDP_LAYOUT to intent.getStringExtra(KEY_PDP_LAYOUT),
        KEY_PDP_DATA to intent.getStringExtra(KEY_PDP_DATA),
        KEY_PDP_RECOMM to intent.getStringExtra(KEY_PDP_RECOMM),
        KEY_PDP_PLAY to intent.getStringExtra(KEY_PDP_PLAY)
    )
}
