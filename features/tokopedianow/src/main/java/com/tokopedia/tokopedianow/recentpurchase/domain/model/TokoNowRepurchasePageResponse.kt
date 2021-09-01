package com.tokopedia.tokopedianow.recentpurchase.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.network.exception.Header
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct

data class TokoNowRepurchasePageResponse(
    @SerializedName("TokonowRepurchasePageResponse")
    val response: TokonowRepurchasePageDataResponse
) {

    data class TokonowRepurchasePageDataResponse(
        @SerializedName("header")
        val header: Header,
        @SerializedName("data")
        val data: GetRepurchaseProductListResponse
    )

    data class GetRepurchaseProductListResponse(
        @SerializedName("listProduct")
        val productList: List<RepurchaseProduct>
    )
}