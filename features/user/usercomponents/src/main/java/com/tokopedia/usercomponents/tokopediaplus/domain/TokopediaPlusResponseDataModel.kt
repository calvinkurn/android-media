package com.tokopedia.usercomponents.tokopediaplus.domain

import com.google.gson.annotations.SerializedName

data class TokopediaPlusResponseDataModel(
    @SerializedName("tokopediaPlusWidget")
    var tokopediaPlus: TokopediaPlusDataModel = TokopediaPlusDataModel()
)

data class TokopediaPlusDataModel(
    @SerializedName("isShown")
    var isShown: Boolean = false,
    @SerializedName("iconImageURL")
    var iconImageURL: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("subtitle")
    var subtitle: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("applink")
    var applink: String = "",
    @SerializedName("isSubscriber")
    var isSubscriber: Boolean = false
)