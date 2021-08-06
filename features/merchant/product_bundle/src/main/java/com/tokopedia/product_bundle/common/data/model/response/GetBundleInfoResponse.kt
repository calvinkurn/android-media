package com.tokopedia.product_bundle.common.data.model.response

import com.google.gson.annotations.SerializedName

data class GetBundleInfoResponse(
        @SerializedName("error")
        val error: Error = Error(),
        @SerializedName("data")
        val bundleInfo: BundleInfo = BundleInfo()
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
        val bundlePrice: Double = 0.0,
        @SerializedName("stock")
        val stock: Long = 0,
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("selection")
        val selections: List<Selection> = listOf(),
        @SerializedName("children")
        val children: List<Child> = listOf(),
        @SerializedName("originalPrice")
        val originalPrice: Double = 0.0,
        @SerializedName("productStatus")
        val productStatus: String = "",
        @SerializedName("preorder")
        val preorder: Preorder = Preorder()
)

data class Selection(
        @SerializedName("productVariantID")
        val productVariantID: Long = 0L,
        @SerializedName("variantID")
        val variantID: Long = 0L,
        @SerializedName("variantUnitID")
        val variantUnitID: Long = 0L,
        @SerializedName("position")
        val position: Long = 0L,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("identifier")
        val identifier: String = "",
        @SerializedName("option")
        val options: List<VariantOption> = listOf()
)

data class VariantOption(
        @SerializedName("productVariantOptionID")
        val productVariantOptionID: Long = 0L,
        @SerializedName("unitValueID")
        val unitValueID: Long = 0L,
        @SerializedName("value")
        val value: String = "",
        @SerializedName("hex")
        val hex: String = "",
        @SerializedName("picUrl")
        val picUrl: String = "",
        @SerializedName("picUrl100")
        val picUrl100: String = "",
        @SerializedName("picUrl200")
        val picUrl200: String = ""
)

data class Child(
        @SerializedName("productID")
        val productID: Long = 0L,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("picURL")
        val picURL: String = "",
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("bundlePrice")
        val bundlePrice: Double = 0.0,
        @SerializedName("originalPrice")
        val originalPrice: Double = 0.0,
        @SerializedName("stock")
        val stock: Long = 0,
        @SerializedName("optionID")
        val optionIds: List<Long> = listOf()
)

data class Preorder(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("statusNum")
        val statusNum: Long = 0L,
        @SerializedName("processType")
        val processType: String = "",
        @SerializedName("processTypeNum")
        val processTypeNum: Long = 0L,
        @SerializedName("startTime")
        val startTime: String = "",
        @SerializedName("endTime")
        val endTime: String = "",
        @SerializedName("orderLimit")
        val orderLimit: Long = 0L,
        @SerializedName("maxOrder")
        val maxOrder: Long = 0L,
        @SerializedName("processDay")
        val processDay: Long = 0L,
        @SerializedName("processTime")
        val processTime: Long = 0L
)