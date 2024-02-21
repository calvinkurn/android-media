package com.tokopedia.tokopedianow.shoppinglist.domain.model

import com.google.gson.annotations.SerializedName

data class GetShoppingListDataResponse(
    @SerializedName("TokonowGetShoppingList")
    val tokonowGetShoppingList: TokonowGetShoppingList = TokonowGetShoppingList()
) {
    companion object {
        private const val WEIGHT_POSITION = "weight"
    }

    data class TokonowGetShoppingList(
        @SerializedName("data")
        val data: Data = Data(),
        @SerializedName("header")
        val header: Header = Header()
    )

    data class Data(
        @SerializedName("listAvailableItem")
        val listAvailableItem: List<AvailableItem> = emptyList(),
        @SerializedName("listUnavailableItem")
        val listUnavailableItem: List<UnavailableItem> = emptyList(),
        @SerializedName("metadata")
        val metadata: Metadata = Metadata()
    )

    data class AvailableItem(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("discountPercentage")
        val discountPercentage: Int = 0,
        @SerializedName("id")
        val id: String = "",
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("isSelected")
        val isSelected: Boolean = false,
        @SerializedName("labelGroup")
        val labelGroup: List<LabelGroup> = emptyList(),
        @SerializedName("maxOrder")
        val maxOrder: Int = 0,
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("originalPrice")
        val originalPrice: String = "",
        @SerializedName("parentProductID")
        val parentProductID: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("priceInt")
        val priceInt: Double = 0.0,
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("url")
        val url: String = "",
        @SerializedName("warehouseID")
        val warehouseID: String = ""
    ) {
        fun getWeight() = labelGroup.firstOrNull { it.isWeightPosition() }?.title.orEmpty()
    }

    data class UnavailableItem(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("discountPercentage")
        val discountPercentage: Int = 0,
        @SerializedName("id")
        val id: String = "",
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("isSelected")
        val isSelected: Boolean = false,
        @SerializedName("labelGroup")
        val labelGroup: List<LabelGroup> = emptyList(),
        @SerializedName("maxOrder")
        val maxOrder: Int = 0,
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("originalPrice")
        val originalPrice: String = "",
        @SerializedName("parentProductID")
        val parentProductID: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("priceInt")
        val priceInt: Double = 0.0,
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("url")
        val url: String = "",
        @SerializedName("warehouseID")
        val warehouseID: String = ""
    ) {
        fun getWeight() = labelGroup.firstOrNull { it.isWeightPosition() }?.title.orEmpty()
    }

    data class Metadata(
        @SerializedName("inStockSelectedTotalData")
        val inStockSelectedTotalData: Int = 0,
        @SerializedName("inStockSelectedTotalPrice")
        val inStockSelectedTotalPrice: Int = 0,
        @SerializedName("inStockSelectedTotalPriceFmt")
        val inStockSelectedTotalPriceFmt: String = "",
        @SerializedName("inStockTotalData")
        val inStockTotalData: Int = 0,
        @SerializedName("oosTotalData")
        val oosTotalData: Int = 0,
        @SerializedName("queryParam")
        val queryParam: String = ""
    )

    data class Shop(
        @SerializedName("id")
        val id: String = ""
    )

    data class LabelGroup(
        @SerializedName("position")
        val position: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("url")
        val url: String = ""
    ) {
        fun isWeightPosition() = position == WEIGHT_POSITION
    }

    data class Header(
        @SerializedName("processTime")
        val processTime: Double = 0.0,
        @SerializedName("error_code")
        val errorCode: String = "",
        @SerializedName("messages")
        val messages: List<String> = emptyList()
    )
}
