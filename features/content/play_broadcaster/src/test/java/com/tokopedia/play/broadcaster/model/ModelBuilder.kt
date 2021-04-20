package com.tokopedia.play.broadcaster.model

import com.google.gson.Gson
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.CreateLiveStreamChannelResponse
import com.tokopedia.play.broadcaster.domain.model.GetLiveFollowersResponse
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.type.ProductStock
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import java.io.File

/**
 * Created by jegul on 25/09/20
 */
class ModelBuilder {

    private val gson = Gson()

    /**
     * Network Data
     */
    fun buildLiveStats(): GetLiveStatisticsResponse.ReportChannelSummary {
        return gson.fromJson(loadJsonToString("mock_live_stats.json"), GetLiveStatisticsResponse.ReportChannelSummary::class.java)
    }

    fun buildProductsInEtalase(): GetProductsByEtalaseResponse.GetProductListData {
        return gson.fromJson(loadJsonToString("mock_products_in_etalase.json"), GetProductsByEtalaseResponse::class.java).productList
    }

    fun buildCreateLiveStreamGetMedia(): CreateLiveStreamChannelResponse.GetMedia {
        return gson.fromJson(loadJsonToString("mock_create_live_stream.json"), CreateLiveStreamChannelResponse::class.java).media
    }

    fun buildGetLiveFollowers(): GetLiveFollowersResponse {
        return gson.fromJson(loadJsonToString("mock_get_live_followers.json"), GetLiveFollowersResponse::class.java)
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

    fun buildPlayCoverUiModel(
            croppedCover: CoverSetupState = CoverSetupState.Blank,
            title: String = "",
            state: SetupDataState = SetupDataState.Draft
    ) = PlayCoverUiModel(
            croppedCover = croppedCover,
            title = title,
            state = state
    )
}