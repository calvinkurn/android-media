package com.tokopedia.shop_showcase.shop_showcase_product_add.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    @SerializedName("ProductList")
    var productList: ProductList = ProductList()
) {
    data class ProductList(
        @SerializedName("header")
        var header: Header = Header(),
        @SerializedName("meta")
        var meta: Meta = Meta(),
        @SerializedName("data")
        var data: List<Data> = listOf()
    ) {
        data class Header(
            @SerializedName("processTime")
            var processTime: Double = 0.0,
            @SerializedName("messages")
            var messages: List<String> = listOf(),
            @SerializedName("reason")
            var reason: String = "",
            @SerializedName("errorCode")
            var errorCode: String = ""
        )

        data class Meta(
            @SerializedName("totalHits")
            var totalHits: Int = 0
        )

        data class Data(
            @SerializedName("id")
            var id: String = "",
            @SerializedName("name")
            var name: String = "",
            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            var price: Price = Price(),
            @SerializedName("stock")
            var stock: Int = 0,
            @SerializedName("stockReserved")
            var stockReserved: Int = 0,
            @SerializedName("status")
            var status: String = "",
            @SerializedName("minOrder")
            var minOrder: Int = 0,
            @SerializedName("maxOrder")
            var maxOrder: Int = 0,
            @SerializedName("weight")
            var weight: Int = 0,
            @SerializedName("weightUnit")
            var weightUnit: String = "",
            @SerializedName("condition")
            var condition: String = "",
            @SerializedName("isMustInsurance")
            var isMustInsurance: Boolean = false,
            @SerializedName("isCOD")
            var isCOD: Boolean = false,
            @SerializedName("isVariant")
            var isVariant: Boolean = false,
            @SerializedName("isEmptyStock")
            var isEmptyStock: Boolean = false,
            @SerializedName("hasStockReserved")
            var hasStockReserved: Boolean = false,
            @SerializedName("hasInbound")
            var hasInbound: Boolean = false,
            @SerializedName("url")
            var url: String = "",
            @SerializedName("sku")
            var sku: String = "",
            @SerializedName("featured")
            var featured: Int = 0,
            @SerializedName("score")
            var score: Score = Score(),
            @SerializedName("category")
            var category: List<Category> = listOf(),
            @SerializedName("menu")
            var menu: List<Menu> = listOf(),
            @SerializedName("pictures")
            var pictures: List<Pictures> = listOf(),
            @SerializedName("shop")
            var shop: Shop = Shop(),
            @SerializedName("wholesale")
            var wholesale: List<Wholesale> = listOf(),
            @SerializedName("stats")
            var stats: Stats = Stats(),
            @SerializedName("txStats")
            var txStats: TxStats = TxStats(),
            @SerializedName("lock")
            var lock: String = "",
            @SerializedName("tax")
            var tax: String = "",
            @SerializedName("topads")
            var topads: String = "",
            @SerializedName("warehouseCount")
            var warehouseCount: Int = 0,
            @SerializedName("priceSuggestion")
            var priceSuggestion: String = "",
            @SerializedName("campaignType")
            var campaignType: List<CampaignType> = listOf(),
            @SerializedName("suspendLevel")
            var suspendLevel: Int = 0,
            @SerializedName("hasAddon")
            var hasAddon: Boolean = false,
            @SuppressLint("Invalid Data Type")
            @SerializedName("addonID")
            var addonID: List<String> = listOf(),
            @SerializedName("hasStockAlert")
            var hasStockAlert: Boolean = false,
            @SerializedName("stockAlertCount")
            var stockAlertCount: Int = 0,
            @SerializedName("stockAlertActive")
            var stockAlertActive: Boolean = false,
            @SerializedName("isMultipleWarehouse")
            var isMultipleWarehouse: Boolean = false,
            @SerializedName("haveNotifyMeOOS")
            var haveNotifyMeOOS: Boolean = false,
            @SerializedName("notifyMeOOSCount")
            var notifyMeOOSCount: Int = 0,
            @SerializedName("notifyMeOOSWording")
            var notifyMeOOSWording: String = "",
            @SerializedName("manageProductData")
            var manageProductData: ManageProductData = ManageProductData(),
            @SerializedName("createTime")
            var createTime: String = ""
        ) {
            data class Price(
                @SerializedName("min")
                var min: Int = 0,
                @SerializedName("max")
                var max: Int = 0
            )

            data class Score(
                @SerializedName("total")
                var total: Int = 0
            )

            data class Category(
                @SerializedName("id")
                var id: String = ""
            )

            data class Menu(
                @SerializedName("id")
                var id: String = ""
            )

            data class Pictures(
                @SerializedName("urlThumbnail")
                var urlThumbnail: String = ""
            )

            data class Shop(
                @SerializedName("id")
                var id: String = ""
            )

            data class Wholesale(
                @SerializedName("minQty")
                var minQty: Int = 0,
                @SerializedName("price")
                var price: Double = 0.0
            )

            data class Stats(
                @SerializedName("countView")
                var countView: Int = 0,
                @SerializedName("countReview")
                var countReview: Int = 0,
                @SerializedName("countTalk")
                var countTalk: Int = 0
            )

            data class TxStats(
                @SerializedName("sold")
                var sold: Int = 0
            )

            data class CampaignType(
                @SerializedName("id")
                var id: String = "",
                @SerializedName("name")
                var name: String = "",
                @SerializedName("iconURL")
                var iconURL: String = ""
            )

            data class ManageProductData(
                @SerializedName("isPriceUncompetitive")
                var isPriceUncompetitive: Boolean = false,
                @SerializedName("isStockGuaranteed")
                var isStockGuaranteed: Boolean = false,
                @SerializedName("scoreV3")
                var scoreV3: Int = 0,
                @SerializedName("isTobacco")
                var isTobacco: Boolean = false,
                @SerializedName("isDTInbound")
                var isDTInbound: Boolean = false,
                @SerializedName("isInGracePeriod")
                var isInGracePeriod: Boolean = false,
                @SerializedName("isArchived")
                var isArchived: Boolean = false
            )
        }
    }
}

