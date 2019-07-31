package com.tokopedia.transactiondata.entity.response.cartlist.shopgroup

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ProductInvenageTotal(

        @field:SerializedName("by_product")
        val byProduct: ByProduct = ByProduct(),

        @field:SerializedName("is_counted_by_user")
        val isCountedByUser: Boolean = false,

        @field:SerializedName("is_counted_by_product")
        val isCountedByProduct: Boolean = false,

        @field:SerializedName("by_user")
        val byUser: ByUser = ByUser(),

        @field:SerializedName("by_user_text")
        val byUserText: ByUserText = ByUserText(),

        @field:SerializedName("by_product_text")
        val byProductText: ByProductText = ByProductText()
)