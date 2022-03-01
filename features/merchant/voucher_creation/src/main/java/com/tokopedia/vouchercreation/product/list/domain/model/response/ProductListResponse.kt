package com.tokopedia.vouchercreation.product.list.domain.model.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductListResponse(
        @SerializedName("ProductList")
        @Expose val productList: ProductList
)

data class ProductList(
        @SerializedName("header")
        @Expose val header: Header,
        @SerializedName("data")
        @Expose val data: List<ProductData>
)

data class GoodsPriceRange(
        @SerializedName("min")
        @Expose val min: Double,
        @SerializedName("max")
        @Expose val max: Double
)

data class GoodsScore(
        @SerializedName("total")
        @Expose val total: Double
)

@SuppressLint("Invalid Data Type")
data class GoodsCategory(
        @SerializedName("id")
        @Expose val id: Int
)

@SuppressLint("Invalid Data Type")
data class GoodsMenu(
        @SerializedName("id")
        @Expose val id: Int
)

data class GoodsPicture(
        @SerializedName("urlThumbnail")
        @Expose val urlThumbnail: String
)

data class GoodsPosition(
        @SerializedName("position")
        @Expose val position: Int
)

data class GoodsTxStats(
        @SerializedName("sold")
        @Expose val sold: Int
)

data class GoodsWarehouse(
        @SerializedName("id")
        @Expose val id: String
)

@SuppressLint("Invalid Data Type")
data class ProductData(
        @SerializedName("id")
        @Expose val id: String,
        @SerializedName("name")
        @Expose val name: String,
        @SerializedName("price")
        @Expose val price: GoodsPriceRange,
        @SerializedName("stock")
        @Expose val stock: Int,
        @SerializedName("stockReserved")
        @Expose val stockReserved: Int,
        @SerializedName("hasStockReserved")
        @Expose val hasStockReserved: Boolean,
        @SerializedName("hasInbound")
        @Expose val hasInbound: Boolean,
        @SerializedName("status")
        @Expose val status: String,
        @SerializedName("minOrder")
        @Expose val minOrder: Int,
        @SerializedName("maxOrder")
        @Expose val maxOrder: Int,
        @SerializedName("weight")
        @Expose val weight: Int,
        @SerializedName("weightUnit")
        @Expose val weightUnit: String,
        @SerializedName("condition")
        @Expose val condition: String,
        @SerializedName("isMustInsurance")
        @Expose val isMustInsurance: Boolean,
        @SerializedName("isKreasiLokal")
        @Expose val isKreasiLokal: Boolean,
        @SerializedName("isCOD")
        @Expose val isCOD: Boolean,
        @SerializedName("isVariant")
        @Expose val isVariant: Boolean,
        @SerializedName("isCampaign")
        @Expose val isCampaign: Boolean,
        @SerializedName("featured")
        @Expose val featured: Int,
        @SerializedName("cashback")
        @Expose val cashback: Int,
        @SerializedName("url")
        @Expose val url: String,
        @SerializedName("sku")
        @Expose val sku: String,
        @SerializedName("score")
        @Expose val score: GoodsScore,
        @SerializedName("category")
        @Expose val category: List<GoodsCategory>,
        @SerializedName("menu")
        @Expose val menu: List<GoodsMenu>,
        @SerializedName("pictures")
        @Expose val pictures: List<GoodsPicture>,
        @SerializedName("position")
        @Expose val position: GoodsPosition,
        @SerializedName("txStats")
        @Expose val txStats: GoodsTxStats,
        @SerializedName("warehouseCount")
        @Expose val warehouseCount: Int,
        @SerializedName("isEmptyStock")
        @Expose val isEmptyStock: Boolean,
        @SerializedName("warehouse")
        @Expose val warehouse: List<GoodsWarehouse>,
        @SerializedName("bundleCount")
        @Expose val bundleCount: Int,
        @SerializedName("suspendLevel")
        @Expose val suspendLevel: Int
)

