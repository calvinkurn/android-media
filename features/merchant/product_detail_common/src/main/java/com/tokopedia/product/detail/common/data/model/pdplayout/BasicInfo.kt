package com.tokopedia.product.detail.common.data.model.pdplayout


import android.content.Context
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.R
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.constant.WeightTypeDef
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.Etalase
import com.tokopedia.product.detail.common.data.model.product.Stats
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp

data class BasicInfo(
        @SerializedName("shopName")
        val shopName: String = "",
        @SerializedName("blacklistMessage")
        val blacklistMessage: BlacklistMessage = BlacklistMessage(),
        @SerializedName("catalogID")
        val catalogID: String = "",
        @SerializedName("category")
        val category: Category = Category(),
        @SerializedName("isBlacklisted")
        val isBlacklisted: Boolean = false,
        @SerializedName("isLeasing")
        val isLeasing: Boolean = false,
        @SerializedName("maxOrder")
        val maxOrder: Int = 0,
        @SerializedName("menu")
        val menu: Etalase = Etalase(),
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("needPrescription")
        val needPrescription: Boolean = false,
        @SerializedName("productID")
        val productID: String = "",
        @SerializedName("shopID")
        val shopID: String = "",
        @SerializedName("sku")
        val sku: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("weightUnit")
        val weightUnit: String = WeightTypeDef.UNKNOWN,
        @SerializedName("weight")
        val weight: Int = 0,
        @SerializedName("stats")
        val stats: Stats = Stats(),
        @SerializedName("txStats")
        val txStats: TxStatsDynamicPdp = TxStatsDynamicPdp(),
        @SerializedName("defaultOngkirEstimation")
        val defaultOngkirEstimation: String = "30000"
) {

    companion object {
        const val KG = "kilogram"
        const val KILO = 1000
    }

    fun getDefaultOngkirInt(): Int = defaultOngkirEstimation.toIntOrNull() ?: 30000
    fun getWeightUnit(): Float = if (weightUnit.toLowerCase() == KG) weight.toFloat() else weight.toFloat() / KILO
    fun getProductId(): Int = productID.toIntOrNull() ?: 0
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