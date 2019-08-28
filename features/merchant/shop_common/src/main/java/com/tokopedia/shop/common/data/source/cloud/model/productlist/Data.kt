package com.tokopedia.shop.common.data.source.cloud.model.productlist


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("condition")
        val condition: Int = 0,
        @SerializedName("flag")
        val flags: Flags = Flags(),
        @SerializedName("cashback")
        @Expose
        val cashBack: CashBackResponse = CashBackResponse(),
        @SerializedName("minimum_order")
        val minimumOrder: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("name_encoded")
        val nameEncoded: String = "",
        @SerializedName("position")
        val position: Int = 0,
        @SerializedName("price")
        val price: Price = Price(),
        @SerializedName("primary_image")
        val primaryImage: PrimaryImage = PrimaryImage(),
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("product_url")
        val productUrl: String = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("wholesale")
        val wholesaleResponse: ArrayList<WholeSaleResponse> = arrayListOf()
)