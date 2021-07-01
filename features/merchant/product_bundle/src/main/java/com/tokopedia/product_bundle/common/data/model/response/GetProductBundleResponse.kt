package com.tokopedia.product_bundle.common.data.model.response

import com.google.gson.annotations.SerializedName

data class GetProductBundleResponse(
        @SerializedName("BundleInfo")
        val bundleInfo: BundleInfo = BundleInfo(),
        @SerializedName("Header")
        val header: Header = Header()
)

data class BundleInfo(
        @SerializedName("BundleID")
        val bundleID: Long = 0L,
        @SerializedName("GroupID")
        val groupID: Long = 0L,
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("Type")
        val type: String = "",
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("ShopID")
        val shopID: Int = 0,
        @SerializedName("StartTimeUnix")
        val startTimeUnix: Long = 0L,
        @SerializedName("StopTimeUnix")
        val stopTimeUnix: Long = 0L,
        @SerializedName("BundleItem")
        val bundleItems: List<BundleItem> = listOf(),
        @SerializedName("WarehouseID")
        val warehouseID: Long = 0L,
        @SerializedName("Quota")
        val quota: Int = 0,
        @SerializedName("OriginalQuota")
        val originalQuota: Int = 0,
        @SerializedName("MaxOrder")
        val maxOrder: Int = 0,
)

data class BundleItem(
        @SerializedName("ProductID")
        val productID: Long = 0L,
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("PicURL")
        val picURL: String = "",
        @SerializedName("Status")
        val status: String = "",
        @SerializedName("Quantity")
        val quantity: Int = 0,
        @SerializedName("Selection")
        val selections: List<Selection> = listOf(),
        @SerializedName("Children")
        val children: List<Child> = listOf(),
        @SerializedName("BundlePrice")
        val bundlePrice: Long = 0L,
        @SerializedName("Stock")
        val stock: Int = 0,
        @SerializedName("MinOrder")
        val minOrder: Int = 0
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