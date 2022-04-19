package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StaticInfo {
    @SerializedName("InfoURL")
    @Expose
    var infoURL: String = ""

    @SerializedName("PromoText")
    @Expose
    var promoText: String = ""
}