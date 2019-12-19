package com.tokopedia.shop.common.data.source.cloud.model.productlist


import com.google.gson.annotations.SerializedName

data class Flags(
        @SerializedName("is_featured")
        val isFeatured: Boolean = false,
        @SerializedName("is_freereturn")
        val isFreereturn: Boolean = false,
        @SerializedName("is_preorder")
        val isPreorder: Boolean = false,
        @SerializedName("is_variant")
        val isVariant: Boolean = false,
        @SerializedName("with_stock")
        val withStock: Boolean = false
)