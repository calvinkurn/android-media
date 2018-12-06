package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseBannerOffer {
    @SerializedName("banner")
    @Expose
    var banners: List<BannerEntity>? = null

    override fun toString(): String {
        return "ResponseBannerOffer{" +
                "banners=" + banners +
                '}'.toString()
    }
}
