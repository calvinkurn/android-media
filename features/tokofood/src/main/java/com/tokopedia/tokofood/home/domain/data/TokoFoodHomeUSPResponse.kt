package com.tokopedia.tokofood.home.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoFoodHomeUSPResponse (
    @SerializedName("tokofoodGetUSP")
    @Expose
    val response: USPResponse
)

data class USPResponse(
    @SerializedName("list")
    val list:List<USP>,
    @SerializedName("footer")
    val footer: String = "",
    @SerializedName("imageURL")
    val imageUrl: String = ""

)

data class USP(
    @SerializedName("iconURL")
    @Expose
    val iconUrl: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("formatted")
    @Expose
    val formatted: String = ""
)
