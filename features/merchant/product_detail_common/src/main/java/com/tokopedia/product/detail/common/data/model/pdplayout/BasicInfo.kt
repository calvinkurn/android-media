package com.tokopedia.product.detail.common.data.model.pdplayout

import android.content.Context
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.DEFAULT_PRICE_MINIMUM_SHIPPING
import com.tokopedia.product.detail.common.R
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.constant.WeightTypeDef
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.Etalase
import com.tokopedia.product.detail.common.data.model.product.Stats
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp

data class BasicInfo(
    @SerializedName("shopName")
    @Expose
    val shopName: String = "",
    @SerializedName("blacklistMessage")
    @Expose
    val blacklistMessage: BlacklistMessage = BlacklistMessage(),
    @SerializedName("catalogID")
    @Expose
    val catalogID: String = "",
    @SerializedName("category")
    @Expose
    val category: Category = Category(),
    @SerializedName("isBlacklisted")
    @Expose
    val isBlacklisted: Boolean = false,
    @SerializedName("isLeasing")
    @Expose
    val isLeasing: Boolean = false,
    @SerializedName("maxOrder")
    @Expose
    val maxOrder: Int = 0,
    @SerializedName("menu")
    @Expose
    val menu: Etalase = Etalase(),
    @SerializedName("minOrder")
    @Expose
    val minOrder: Int = 0,
    @SerializedName("needPrescription")
    @Expose
    val needPrescription: Boolean = false,
    @SerializedName("productID")
    @Expose
    val productID: String = "",
    @SerializedName("shopID")
    @Expose
    val shopID: String = "",
    @SerializedName("sku")
    @Expose
    val sku: String = "",
    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("url")
    @Expose
    val url: String = "",
    @SerializedName("weightUnit")
    @Expose
    val weightUnit: String = WeightTypeDef.UNKNOWN,
    @SerializedName("weight")
    @Expose
    val weight: Int = 0,
    @SerializedName("stats")
    @Expose
    val stats: Stats = Stats(),
    @SerializedName("txStats")
    @Expose
    val txStats: TxStatsDynamicPdp = TxStatsDynamicPdp(),
    @SerializedName("defaultOngkirEstimation")
    @Expose
    val defaultOngkirEstimation: String = "30000",
    @SerializedName("isTokoNow")
    @Expose
    val isTokoNow: Boolean = false,
    @SerializedName("totalStockFmt")
    @Expose
    val totalStockFmt: String = "",
    @SerializedName("isGiftable")
    @Expose
    val isGiftable: Boolean = false,
    @SerializedName("defaultMediaURL")
    @Expose
    val defaultMediaUrl: String = "",
    @SerializedName("shopMultilocation")
    @Expose
    val productMultilocation: ProductMultilocation = ProductMultilocation()
) {
    fun getDefaultOngkirDouble(): Double = defaultOngkirEstimation.toDoubleOrNull()
        ?: DEFAULT_PRICE_MINIMUM_SHIPPING

    fun getShopId(): Int = shopID.toIntOrNull() ?: 0
    fun isActive(): Boolean {
        return status == ProductStatusTypeDef.ACTIVE
    }

    fun isWarehouse(): Boolean {
        return status == ProductStatusTypeDef.WAREHOUSE
    }

    fun statusMessage(context: Context): String {
        return when (status) {
            ProductStatusTypeDef.DELETED -> context.getString(R.string.product_status_deleted)
            ProductStatusTypeDef.ACTIVE -> context.getString(R.string.product_status_active)
            ProductStatusTypeDef.WAREHOUSE -> context.getString(R.string.product_status_warehouse)
            ProductStatusTypeDef.HIDDEN -> context.getString(R.string.product_status_hidden)
            ProductStatusTypeDef.PENDING -> context.getString(R.string.product_status_pending)
            ProductStatusTypeDef.BANNED -> context.getString(R.string.product_status_banned)
            else -> context.getString(R.string.product_status_active)
        }
    }
}
