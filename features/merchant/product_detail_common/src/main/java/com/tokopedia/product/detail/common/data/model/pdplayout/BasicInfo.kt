package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class BasicInfo(
        @SerializedName("alias")
        val alias: String = "",
        @SerializedName("blacklistMessage")
        val blacklistMessage: BlacklistMessage = BlacklistMessage(),
        @SerializedName("catalogID")
        val catalogID: String = "",
        @SerializedName("category")
        val category: Category = Category(),
        @SerializedName("gtin")
        val gtin: String = "",
        @SerializedName("isBlacklisted")
        val isBlacklisted: Boolean = false,
        @SerializedName("isKreasiLokal")
        val isKreasiLokal: Boolean = false,
        @SerializedName("isLeasing")
        val isLeasing: Boolean = false,
        @SerializedName("isMustInsurance")
        val isMustInsurance: Boolean = false,
        @SerializedName("maxOrder")
        val maxOrder: Int = 0,
        @SerializedName("menu")
        val menu: Menu = Menu(),
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("name")
        val name: String = "",
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
        val url: String = ""
)