package com.tokopedia.orderhistory.data


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.orderhistory.view.adapter.OrderHistoryTypeFactory

data class Product(
        @SerializedName("categoryId")
        val categoryId: String = "",
        @SerializedName("discountedPercentage")
        val discountedPercentage: Int = 0,
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("isCampaignActive")
        val isCampaignActive: Boolean = false,
        @SerializedName("listImageUrl")
        val listImageUrl: List<String> = listOf(),
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("playstoreStatus")
        val playstoreStatus: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("priceBefore")
        val priceBefore: String = "",
        @SerializedName("priceBeforeInt")
        val priceBeforeInt: Int = 0,
        @SerializedName("priceInt")
        val priceInt: Double = 0.0,
        @SerializedName("productId")
        val productId: String = "",
        @SerializedName("productUrl")
        val productUrl: String = "",
        @SerializedName("shopId")
        val shopId: String = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("freeOngkir")
        val freeShipping: FreeShipping = FreeShipping()
) : Visitable<OrderHistoryTypeFactory> {

    val hasFreeShipping: Boolean get() = freeShipping.isActive && freeShipping.imageUrl.isNotEmpty()
    val buyEventAction: String = "click on buy again"
    val hasEmptyStock: Boolean get() = status == statusDeleted || status == statusWarehouse
    val hasDiscount: Boolean
        get() {
            return priceBefore.isNotEmpty() && discountedPercentage > 0
        }

    override fun type(typeFactory: OrderHistoryTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        private const val statusDeleted = 0
        private const val statusWarehouse = 3
    }
}