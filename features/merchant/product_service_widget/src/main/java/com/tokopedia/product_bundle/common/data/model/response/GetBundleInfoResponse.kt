package com.tokopedia.product_bundle.common.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.parcelize.Parcelize

data class GetBundleInfoResponse(
        @SerializedName("GetBundleInfo")
        @Expose val getBundleInfo: GetBundleInfo? = GetBundleInfo()
)

data class GetBundleInfo(
        @SerializedName("error")
        @Expose val error: Error = Error(),
        @SerializedName("bundleInfo")
        @Expose val bundleInfo: List<BundleInfo> = emptyList()
)

@Parcelize
data class BundleInfo(
        @SerializedName("bundleID")
        @Expose val bundleID: Long = 0L,
        @SerializedName("groupID")
        @Expose val groupID: Long = 0L,
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("type")
        @Expose val type: String = "",
        @SerializedName("status")
        @Expose val status: String = "",
        @SerializedName("shopID")
        @Expose val shopID: String = "0",
        @SerializedName("startTimeUnix")
        @Expose val startTimeUnix: Long = 0L,
        @SerializedName("stopTimeUnix")
        @Expose val stopTimeUnix: Long = 0L,
        @SerializedName("warehouseID")
        @Expose val warehouseID: Long = 0L,
        @SerializedName("quota")
        @Expose val quota: Int = 0,
        @SerializedName("originalQuota")
        @Expose val originalQuota: Int = 0,
        @SerializedName("maxOrder")
        @Expose val maxOrder: Int = 0,
        @SerializedName("bundleStats")
        @Expose val bundleStats: BundleStats = BundleStats(),
        @SerializedName("preorder")
        @Expose var preorder: Preorder = Preorder(),
        @SerializedName("bundleItem")
        @Expose val bundleItems: List<BundleItem> = listOf(),
        @SerializedName("shopInformation")
        @Expose val shopInformation: ShopInformation = ShopInformation(),
): Parcelable

@Parcelize
data class BundleItem(
        @SerializedName("productID")
        @Expose val productID: Long = 0L,
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("picURL")
        @Expose val picURL: String = "",
        @SerializedName("status")
        @Expose val status: String = "",
        @SerializedName("quantity")
        @Expose val quantity: Int = 0,
        @SerializedName("bundlePrice")
        @Expose val bundlePrice: Double = 0.0,
        @SerializedName("stock")
        @Expose val stock: Long = 0,
        @SerializedName("minOrder")
        @Expose val minOrder: Int = 0,
        @SerializedName("selection")
        @Expose val selections: List<Selection> = listOf(),
        @SerializedName("children")
        @Expose val children: List<Child> = listOf(),
        @SerializedName("originalPrice")
        @Expose val originalPrice: Double = 0.0,
        @SerializedName("productStatus")
        @Expose val productStatus: String = ""
): Parcelable {

    fun getPreviewOriginalPrice() = if (originalPrice > 0) originalPrice else
        children.minByOrNull { it.bundlePrice }?.originalPrice.orZero()

    fun getPreviewBundlePrice() = if (bundlePrice > 0) bundlePrice else
        children.minByOrNull { it.bundlePrice }?.bundlePrice.orZero()

    fun getMultipliedOriginalPrice() = getPreviewOriginalPrice() * getPreviewMinOrder()

    fun getMultipliedBundlePrice() = getPreviewBundlePrice() * getPreviewMinOrder()

    fun getPreviewMinOrder() = if (minOrder > 0) minOrder else
        children.minByOrNull { it.minOrder }?.minOrder.orZero()
}

@Parcelize
data class Selection(
        @SerializedName("productVariantID")
        @Expose val productVariantID: Long = 0L,
        @SerializedName("variantID")
        @Expose val variantID: Long = 0L,
        @SerializedName("variantUnitID")
        @Expose val variantUnitID: Long = 0L,
        @SerializedName("position")
        @Expose val position: Long = 0L,
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("identifier")
        @Expose val identifier: String = "",
        @SerializedName("option")
        @Expose val options: List<VariantOption> = listOf()
): Parcelable

@Parcelize
data class VariantOption(
        @SerializedName("productVariantOptionID")
        @Expose val productVariantOptionID: Long = 0L,
        @SerializedName("unitValueID")
        @Expose val unitValueID: Long = 0L,
        @SerializedName("value")
        @Expose val value: String = "",
        @SerializedName("hex")
        @Expose val hex: String = "",
        @SerializedName("picUrl")
        @Expose val picUrl: String = "",
        @SerializedName("picUrl100")
        @Expose val picUrl100: String = "",
        @SerializedName("picUrl200")
        @Expose val picUrl200: String = ""
): Parcelable

@Parcelize
data class Child(
        @SerializedName("productID")
        @Expose val productID: Long = 0L,
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("picURL")
        @Expose val picURL: String = "",
        @SerializedName("minOrder")
        @Expose val minOrder: Int = 0,
        @SerializedName("bundlePrice")
        @Expose val bundlePrice: Double = 0.0,
        @SerializedName("originalPrice")
        @Expose val originalPrice: Double = 0.0,
        @SerializedName("stock")
        @Expose val stock: Int = 0,
        @SerializedName("isBuyable")
        @Expose val isBuyable: Boolean = false,
        @SerializedName("optionID")
        @Expose val optionIds: List<Long> = listOf()
): Parcelable

@Parcelize
data class Preorder(
        @SerializedName("status")
        @Expose val status: String = "",
        @SerializedName("statusNum")
        @Expose val statusNum: Long = 0L,
        @SerializedName("processType")
        @Expose val processType: String = "",
        @SerializedName("processTypeNum")
        @Expose val processTypeNum: Int = 0,
        @SerializedName("startTime")
        @Expose val startTime: String = "",
        @SerializedName("endTime")
        @Expose val endTime: String = "",
        @SerializedName("orderLimit")
        @Expose val orderLimit: Long = 0L,
        @SerializedName("maxOrder")
        @Expose val maxOrder: Long = 0L,
        @SerializedName("processDay")
        @Expose val processDay: Long = 0L,
        @SerializedName("processTime")
        @Expose val processTime: String = ""
): Parcelable

@Parcelize
data class ShopInformation (
    @SerializedName("ShopName")
    @Expose val shopName: String = "",
    @SerializedName("ShopType")
    @Expose val shopType: String = "",
    @SerializedName("ShopBadge")
    @Expose val shopBadge: String = "",
    @SerializedName("ShopID")
    @Expose val shopId: Long = 0L,
): Parcelable

@Parcelize
data class BundleStats (
    @SerializedName("SoldItem")
    @Expose val totalSold: String = ""
): Parcelable
