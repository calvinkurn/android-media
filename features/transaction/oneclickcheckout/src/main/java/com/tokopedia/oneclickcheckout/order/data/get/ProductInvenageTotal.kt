package com.tokopedia.oneclickcheckout.order.data.get

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

data class ByProduct(
        @SerializedName("in_cart")
        val inCart: Int = 0,
        @SerializedName("last_stock_less_than")
        val lastStockLessThan: Int = 0
)

data class ByProductText(
        @SerializedName("in_cart")
        val inCart: String = "",
        @SerializedName("last_stock_less_than")
        val lastStockLessThan: String = "",
        @SerializedName("complete")
        val complete: String = ""
)

data class ByUser(
        @SerializedName("in_cart")
        val inCart: Int = 0,
        @SerializedName("last_stock_less_than")
        val lastStockLessThan: Int = 0
)

data class ByUserText(
        @SerializedName("in_cart")
        val inCart: String = "",
        @SerializedName("last_stock_less_than")
        val lastStockLessThan: String = "",
        @SerializedName("complete")
        val complete: String = ""
)