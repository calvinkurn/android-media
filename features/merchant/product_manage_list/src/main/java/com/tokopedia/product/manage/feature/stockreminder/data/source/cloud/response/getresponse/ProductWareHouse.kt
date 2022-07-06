package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductWareHouse(
        @SerializedName("product_id")
        @Expose
        val productId: String,
        @SerializedName("warehouse_id")
        @Expose
        val wareHouseId: String,
        @SerializedName("stock")
        @Expose
        val stock: Int,
        @SerializedName("price")
        @Expose
        val price: String,
        @SerializedName("threshold")
        @Expose
        val threshold: Int,
        @SerializedName("shop_id")
        @Expose
        val shopId: String
)