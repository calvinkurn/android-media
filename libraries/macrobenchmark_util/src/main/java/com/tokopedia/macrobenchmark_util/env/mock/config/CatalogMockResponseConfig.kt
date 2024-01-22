package com.tokopedia.macrobenchmark_util.env.mock.config

import android.content.Context
import android.content.Intent
import com.tokopedia.macrobenchmark_util.env.mock.MockModelConfig

/**
 * Created by yovi.putra on 08/11/22"
 * Project name: android-tokopedia-core
 **/

class CatalogMockResponseConfig : MockModelConfig() {
    companion object {
        const val KEY_CATALOG_PRODUCT_LIST = "SearchProduct"
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
        KEY_CATALOG_PRODUCT_LIST to intent.getStringExtra("SearchProduct"),
    )
}
