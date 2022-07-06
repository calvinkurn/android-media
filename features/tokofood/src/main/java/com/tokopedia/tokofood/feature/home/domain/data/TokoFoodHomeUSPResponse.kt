package com.tokopedia.tokofood.feature.home.domain.data

import com.google.gson.annotations.SerializedName

data class TokoFoodHomeUSPResponse (
    @SerializedName("tokofoodGetUSP")
    val response: USPResponse
)

data class USPResponse(
    @SerializedName("list")
    val list:List<USP> = emptyList(),
    @SerializedName("footer")
    val footer: String = "",
    @SerializedName("imageURL")
    val imageUrl: String = ""

)

data class USP(
    @SerializedName("iconURL")
    val iconUrl: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("formatted")
    val formatted: String = ""
)
