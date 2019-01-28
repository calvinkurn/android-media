package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class ResponseBannerOffer(
        @SerializedName("banner")
        var banners: List<BannerEntity>? = null)
