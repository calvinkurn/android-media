package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ProductInvenageTotal(
    @SerializedName("by_product")
    val byProduct: ByProduct = ByProduct(),
    @SerializedName("is_counted_by_user")
    val isCountedByUser: Boolean = false,
    @SerializedName("is_counted_by_product")
    val isCountedByProduct: Boolean = false,
    @SerializedName("by_user")
    val byUser: ByUser = ByUser(),
    @SerializedName("by_user_text")
    val byUserText: ByUserText = ByUserText(),
    @SerializedName("by_product_text")
    val byProductText: ByProductText = ByProductText()
)
