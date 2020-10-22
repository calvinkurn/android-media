package com.tokopedia.product.manage.feature.campaignstock.domain.model.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.network.exception.Header

data class StockThumbnailResponse(
        @SerializedName("GetProduct")
        val getProduct: GetProduct = GetProduct()
)

data class GetProduct(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val data: StockThumbnailData = StockThumbnailData()
)

data class StockThumbnailData(
        @SerializedName("picture")
        val picture: List<StockThumbnailPicture> = listOf()
)

data class StockThumbnailPicture(
        @SerializedName("thumbnailURL")
        val thumbnailUrl: String = ""
)