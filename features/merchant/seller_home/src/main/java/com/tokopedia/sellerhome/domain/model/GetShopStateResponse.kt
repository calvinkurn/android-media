package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.SerializedName

data class GetShopStateResponse(
    @SerializedName("fetchInfoWidgetData")
    val fetchInfoWidgetData: FetchInfoWidgetModel = FetchInfoWidgetModel()
) {
    data class FetchInfoWidgetModel(
        @SerializedName("data")
        val data: List<FetchInfoWidgetDataModel> = listOf()
    ) {
        data class FetchInfoWidgetDataModel(
            @SerializedName("meta")
            val meta: MetaModel
        ) {

            data class MetaModel(
                @SerializedName("shopState")
                val shopState: Long = 0L
            )
        }
    }
}
