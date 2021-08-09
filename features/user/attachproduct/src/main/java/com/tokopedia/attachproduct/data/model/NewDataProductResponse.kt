package com.tokopedia.attachproduct.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 08/03/18.
 */
data class NewDataProductResponse (
    @SerializedName("url")
    var productUrl: String? = null,

    @SerializedName("name")
    var productName: String? = null,

    @SerializedName("id")
    var productId: String? = null,

    @SerializedName("imageURL")
    @Expose
    var productImage: String? = null,

    @SuppressLint("Invalid Data Type") @SerializedName("price")
    var productPrice: String? = null,

    @SerializedName("priceInt")
    var priceInt: Int? = 0,

    @SerializedName("shop")
    val shop: DataShopResponse? = null
)