package com.tokopedia.play.broadcaster.model

import com.google.gson.Gson
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.domain.model.LiveStats
import java.io.File

/**
 * Created by jegul on 25/09/20
 */
class ModelBuilder {

    private val gson = Gson()

    fun buildLiveStats(): LiveStats {
        return gson.fromJson(loadJsonToString("mock_live_stats.json"), LiveStats::class.java)
    }

    fun buildProductsInEtalase(): GetProductsByEtalaseResponse.GetProductListData {
        return gson.fromJson(loadJsonToString("mock_products_in_etalase.json"), GetProductsByEtalaseResponse::class.java).productList
    }

    private fun loadJsonToString(path: String): String {
        val file = File(this::class.java.classLoader!!.getResource(path).path)
        val stringBuilder = StringBuilder()
        file.useLines {
            it.forEach { text ->
                stringBuilder.append(text)
            }
        }
        return stringBuilder.toString()
    }
}