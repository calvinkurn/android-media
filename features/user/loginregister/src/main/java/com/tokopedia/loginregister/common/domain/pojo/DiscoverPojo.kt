package com.tokopedia.loginregister.common.domain.pojo

import com.google.gson.annotations.SerializedName

data class DiscoverPojo(
    @SerializedName("discover")
    var data: DiscoverData = DiscoverData()
)

data class DiscoverData(
    @SerializedName("providers")
    var providers: ArrayList<ProviderData> = arrayListOf(),
    @SerializedName("url_background_seller")
    var urlBackgroundSeller: String = "",
)

data class ProviderData(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("image")
    var image: String = "",
    @SerializedName("color")
    var color: String = "",
    @SerializedName("scope")
    var scope: String = ""
)
