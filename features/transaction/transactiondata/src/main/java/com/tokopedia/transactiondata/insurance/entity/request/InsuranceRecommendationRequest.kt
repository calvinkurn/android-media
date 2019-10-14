package com.tokopedia.transactiondata.insurance.entity.request

import com.google.gson.annotations.SerializedName

data class InsuranceRecommendationRequest(
        @SerializedName("page")
        var page: String = "",

        @SerializedName("client_version")
        var clientVersion: String = "",

        @SerializedName("client_type")
        var clientType: String = "android",

        @SerializedName("shops")
        var shopsArrayList: List<InsuranceShopsData> = emptyList()
)


data class InsuranceShopsData(
        @SerializedName("shop_id")
        var shopId: Long = 0,

        @SerializedName("items")
        var shopItems: List<InsuranceShops> = emptyList()
)

data class InsuranceShops(
        @SerializedName("product_id")
        var productId: Long = 0,

        @SerializedName("quantity")
        var productQuantity: Int = 0,

        @SerializedName("product_title")
        var productTitle: String = "",

        @SerializedName("condition")
        var condition: String = "",

        @SerializedName("category_id")
        var categoryId: Long = 0,

        @SerializedName("product_price")
        var productPrice: Long = 0,

        @SerializedName("product_description")
        var prodDesc: String = "",

        @SerializedName("category")
        var shopCategory: InsuranceShopCategory = InsuranceShopCategory()

)

data class InsuranceShopCategory(
        @SerializedName("id")
        var categoryId: Long  = 0,

        @SerializedName("name")
        var categoryName: String = "",

        @SerializedName("bread_crumb_url")
        var breadCrumbUrl: String = "",

        @SerializedName("is_adult")
        var isAdult: Boolean = false,

        @SerializedName("detail")
        val categoryDetail: List<InsuranceCategoryDetail> = emptyList()
)

data class InsuranceCategoryDetail(
        @SerializedName("id")
        var id: Long = 0,

        @SerializedName("name")
        var categoryName: String = "",

        @SerializedName("bread_crumb_url")
        var breadCrumbUrl: String = "",

        @SerializedName("is_adult")
        var isAdult: Boolean = false

)