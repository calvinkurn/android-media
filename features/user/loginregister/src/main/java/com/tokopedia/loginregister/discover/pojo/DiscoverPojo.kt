package com.tokopedia.loginregister.discover.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscoverPojo(
    @SerializedName("discover")
    @Expose
    var data: DiscoverData = DiscoverData()
)

data class DiscoverData(
    @SerializedName("providers")
    @Expose
    var providers: ArrayList<ProviderData> = arrayListOf(),
    @SerializedName("url_background_seller")
    @Expose
    var urlBackgroundSeller: String = "",
)

data class ProviderData(
    @SerializedName("id")
    @Expose
    var id: String = "",
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("url")
    @Expose
    var url: String = "",
    @SerializedName("image")
    @Expose
    var image: String = "",
    @SerializedName("color")
    @Expose
    var color: String = "",
    @SerializedName("scope")
    @Expose
    var scope: String = ""
)