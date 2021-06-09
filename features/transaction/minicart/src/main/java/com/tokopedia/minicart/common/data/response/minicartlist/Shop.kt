package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Shop(
        @SerializedName("maximum_weight_wording")
        val maximumWeightWording: String = "",
        @SerializedName("maximum_shipping_weight")
        val maximumShippingWeight: Int = 0,
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("shop_type_info")
        val shopTypeInfo: ShopTypeInfo = ShopTypeInfo()
)