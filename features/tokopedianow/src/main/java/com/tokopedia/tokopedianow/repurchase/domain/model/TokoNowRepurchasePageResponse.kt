package com.tokopedia.tokopedianow.repurchase.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.network.exception.Header
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct

data class TokoNowRepurchasePageResponse(
    @SerializedName("TokonowRepurchasePage")
    val response: TokonowRepurchasePageDataResponse
) {

    data class TokonowRepurchasePageDataResponse(
        @SerializedName("header")
        val header: Header,
        @SerializedName("data")
        val data: GetRepurchaseProductListResponse
    )

    data class GetRepurchaseProductListResponse(
        @SerializedName("meta")
        val meta: GetRepurchaseProductMetaResponse,
        @SerializedName("listProduct")
        val products: List<RepurchaseProduct>
    )

    data class GetRepurchaseProductMetaResponse(
        @SerializedName("page")
        val page: Int,
        @SerializedName("hasNext")
        val hasNext: Boolean,
        @SerializedName("totalScan")
        val totalScan: Int
    )
}