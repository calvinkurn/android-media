package com.tokopedia.shop.product.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Flags(
    @SerializedName("isFeatured")
    @Expose
    val isFeatured: Boolean = false,

    @SerializedName("isFreereturn")
    @Expose
    val isFreereturn: Boolean = false,

    @SerializedName("isPreorder")
    @Expose
    val isPreorder: Boolean = false,

    @SerializedName("isSold")
    @Expose
    val isSold: Boolean = false,

    @SerializedName("isVariant")
    @Expose
    val isVariant: Boolean = false,

    @SerializedName("isWholesale")
    @Expose
    val isWholesale: Boolean = false,

    @SerializedName("isWishlist")
    @Expose
    val isWishlist: Boolean = false,

    @SerializedName("mustInsurance")
    @Expose
    val mustInsurance: Boolean = false,

    @SerializedName("supportFreereturn")
    @Expose
    val supportFreereturn: Boolean = false,

    @SerializedName("withStock")
    @Expose
    val withStock: Boolean = false
)
