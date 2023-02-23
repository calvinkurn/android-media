package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by @ilhamsuaib on 23/11/22.
 */

data class GetShopStateInfoResponse(
    @SerializedName("fetchInfoWidgetData")
    val fetchInfoWidgetData: FetchInfoWidgetModel = FetchInfoWidgetModel()
)

data class FetchInfoWidgetModel(
    @SerializedName("data")
    val data: List<FetchInfoWidgetDataModel> = listOf()
)

data class FetchInfoWidgetDataModel(
    @SerializedName("dataKey")
    val dataKey: String = String.EMPTY,
    @SerializedName("imageUrl")
    val imageUrl: String = String.EMPTY,
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("subtitle")
    val subtitle: String = String.EMPTY,
    @SerializedName("button")
    val button: InfoWidgetButtonModel = InfoWidgetButtonModel(),
    @SerializedName("buttonAlt")
    val buttonAlt: InfoWidgetButtonModel = InfoWidgetButtonModel(),
    @SerializedName("widgetDataSign")
    val dataSign: String = String.EMPTY,
    @SerializedName("subType")
    val subType: Long = Int.ZERO.toLong(),
    @SerializedName("meta")
    val meta: MetaModel
) {

    data class MetaModel(
        @SerializedName("shopState")
        val shopState: Long = 0L,
    )
}

data class InfoWidgetButtonModel(
    @SerializedName("name")
    val name: String = String.EMPTY,
    @SerializedName("applink")
    val appLink: String = String.EMPTY,
)