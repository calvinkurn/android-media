package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoPointStatusTierEntity(
    @Expose
    @SerializedName("id")
    var id: Int = 0,
    @Expose
    @SerializedName("eggImageURL")
    var eggImageUrl: String = "",
    @Expose
    @SerializedName("name")
    var name: String = "",
    @Expose
    @SerializedName("nameDesc")
    var nameDesc: String = "",
    @SerializedName("eggImageHomepageURL")
    var eggImageHomepageURL: String = "",
    @SerializedName("backgroundImgURLMobile")
    var backgroundImgURLMobile: String = ""
)
