package com.tokopedia.discovery2.data.cpmtopads

import com.google.gson.annotations.SerializedName

data class ProductItem(

        @SerializedName("price_format")
        val priceFormat: String? = null,

        @SerializedName("applinks")
        var applinks: String? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("image_product")
        var imageProduct: ImageProduct? = null,

        @SerializedName("id")
        val id: String? = null,

        @SerializedName("button_text")
        var buttonText: String? = "",

        // Specifically for Shop TopAds Click URL
        var shopAdsClickUrl: String? = "",

        var shopAdsViewUrl: String? = ""

)