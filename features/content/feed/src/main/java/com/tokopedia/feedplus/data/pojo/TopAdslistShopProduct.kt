package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class TopAdslistShopProduct(
    @SerializedName("product_id")
    @Expose val productId: String = "",

    @SerializedName("product_name")
    @Expose val productName: String = "",

    @SerializedName("image_url")
    @Expose val imageUrl: String = ""
)
