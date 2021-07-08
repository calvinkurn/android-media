package com.tokopedia.product_bundle.common.data.model.response

import com.google.gson.annotations.SerializedName

data class GetProductBundleResponse(
        @SerializedName("BundleInfo")
        val bundleInfo: BundleInfo = BundleInfo(),
        @SerializedName("Header")
        val header: Header = Header()
)

data class BundleInfo(
        @SerializedName("bundleID")
        val bundleID: Long = 0L,
        @SerializedName("groupID")
        val groupID: Long = 0L,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("shopID")
        val shopID: Int = 0,
        @SerializedName("startTimeUnix")
        val startTimeUnix: Long = 0L,
        @SerializedName("stopTimeUnix")
        val stopTimeUnix: Long = 0L,
        @SerializedName("warehouseID")
        val warehouseID: Long = 0L,
        @SerializedName("quota")
        val quota: Int = 0,
        @SerializedName("originalQuota")
        val originalQuota: Int = 0,
        @SerializedName("maxOrder")
        val maxOrder: Int = 0,
        @SerializedName("bundleItem")
        val bundleItems: List<BundleItem> = listOf()
)

data class BundleItem(
        @SerializedName("productID")
        val productID: Long = 0L,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("picURL")
        val picURL: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("quantity")
        val quantity: Int = 0,
        @SerializedName("bundlePrice")
        val bundlePrice: Long = 0L,
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("selection")
        val selections: List<Selection> = listOf(),
        @SerializedName("Children")
        val children: List<Child> = listOf(),
        @SerializedName("originalPrice")
        val originalPrice: Long = 0L
)

data class Selection(
        @SerializedName("ProductVariantID")
        val productVariantID: Long = 0L,
        @SerializedName("VariantID")
        val variantID: Long = 0L,
        @SerializedName("VariantUnitID")
        val variantUnitID: Long = 0L,
        @SerializedName("Position")
        val position: Int = 0,
        @SerializedName("Option")
        val options: List<VariantOption> = listOf(),
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("Identifier")
        val Identifier: String = ""
)

data class VariantOption(
        @SerializedName("ProductVariantOptionID")
        val productVariantOptionID: Long = 0L,
        @SerializedName("UnitValueID")
        val unitValueID: Int = 0,
        @SerializedName("Value")
        val value: String = "",
        @SerializedName("Hex")
        val hex: String = ""
)

data class Child(
        @SerializedName("ProductID")
        val productID: Long = 0L,
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("PicURL")
        val picURL: String = "",
        @SerializedName("MinOrder")
        val minOrder: Int = 0,
        @SerializedName("BundlePrice")
        val bundlePrice: Long = 0L,
        @SerializedName("Stock")
        val stock: Int = 0,
        @SerializedName("OptionID")
        val optionIds: List<Int> = listOf()
)