package com.tokopedia.transactionanalytics.data.expresscheckout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 07/02/19.
 */

data class Product(
        @SerializedName("name")
        var name: String? = "",

        @SerializedName("id")
        var id: Int? = 0,

        @SerializedName("price")
        var price: Int? = 0,

        @SerializedName("brand")
        var brand: String? = "",

        @SerializedName("category")
        var category: String? = "",

        @SerializedName("variant")
        var variant: String? = "",

        @SerializedName("quantity")
        var quantity: Int? = 0,

        @SerializedName("shop_id")
        var shopId: Int? = 0,

        @SerializedName("shop_type")
        var shopType: String? = "",

        @SerializedName("shop_name")
        var shopName: String? = ""
) {
    companion object {
        const val SHOP_TYPE_REGULER = "reguler"
        const val SHOP_TYPE_OFFICIAL_STORE = "official_store"
        const val SHOP_TYPE_GOLD_MERCHANT = "gold_merchant"
    }
}