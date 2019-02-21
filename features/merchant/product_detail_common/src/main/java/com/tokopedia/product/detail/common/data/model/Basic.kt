package com.tokopedia.product.detail.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.constant.PriceCurrencyTypeDef
import com.tokopedia.product.detail.common.data.model.constant.ProductConditionTypeDef
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.constant.WeightTypeDef

data class Basic(
        @SerializedName("alias")
        @Expose
        val alias: String = "",

        @SerializedName("catalogID")
        @Expose
        val catalogID: Int = 0,

        @SerializedName("condition")
        @Expose
        val condition: String = ProductConditionTypeDef.UNKNOWN,

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("gtin")
        @Expose
        val gtin: String = "",

        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("isEligibleCOD")
        @Expose
        val isEligibleCod: Boolean = false,

        @SerializedName("isKreasiLokal")
        @Expose
        val isKreasiLokal: Boolean = false,

        @SerializedName("isMustInsurance")
        @Expose
        val isMustInsurance: Boolean = false,

        @SerializedName("lastUpdatePrice")
        @Expose
        val lastUpdatePrice: String = "",

        @SerializedName("maxOrder")
        @Expose
        val maxOrder: Int = 0,

        @SerializedName("minOrder")
        @Expose
        val minOrder: Int = 0,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("price")
        @Expose
        val price: Float = 0f,

        @SerializedName("priceCurrency")
        @Expose
        val priceCurrency: String = PriceCurrencyTypeDef.IDR,

        @SerializedName("shopID")
        @Expose
        val shopID: Int = 0,

        @SerializedName("sku")
        @Expose
        val sku: String = "",

        @SerializedName("status")
        @Expose
        val status: String = ProductStatusTypeDef.ACTIVE,

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("weight")
        @Expose
        val weight: Float = 0f,

        @SerializedName("weightUnit")
        @Expose
        val weightUnit: String = WeightTypeDef.UNKNOWN
)