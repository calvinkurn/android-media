package com.tokopedia.play.broadcaster.model

import com.google.gson.Gson
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.domain.model.LiveStats
import com.tokopedia.play.broadcaster.type.ProductStock
import com.tokopedia.play.broadcaster.type.StockAvailable
import java.io.File

/**
 * Created by jegul on 25/09/20
 */
class ModelBuilder {

    private val gson = Gson()

    /**
     * Network Data
     */
    fun buildLiveStats(): LiveStats {
        return gson.fromJson(loadJsonToString("mock_live_stats.json"), LiveStats::class.java)
    }

    fun buildProductsInEtalase(): GetProductsByEtalaseResponse.GetProductListData {
        return gson.fromJson(loadJsonToString("mock_products_in_etalase.json"), GetProductsByEtalaseResponse::class.java).productList
    }

    /**
     * Pojo
     */
    fun buildProductData(
            id: Long = 1L,
            name: String = "Product 1",
            imageUrl: String = "https://www.tokopedia.com",
            originalImageUrl: String = "https://www.tokopedia.com",
            stock: ProductStock = StockAvailable(1)
    ) = ProductData(id, name, imageUrl, originalImageUrl, stock)

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