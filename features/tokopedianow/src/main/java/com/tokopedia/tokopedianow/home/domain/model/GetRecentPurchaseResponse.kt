package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.network.exception.Header
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct

data class GetRecentPurchaseResponse(
    @SerializedName("TokonowRepurchaseWidget")
    val response: RecentPurchaseResponse?
) {

    data class RecentPurchaseResponse(
        @SerializedName("header")
        val header: Header,
        @SerializedName("data")
        val data: RecentPurchaseData
    )

    data class RecentPurchaseData(
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("listProduct")
        val products: List<RepurchaseProduct> = emptyList()
    )
}