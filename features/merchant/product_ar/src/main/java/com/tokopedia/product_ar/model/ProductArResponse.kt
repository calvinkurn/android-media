package com.tokopedia.product_ar.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.product.Category

data class ProductArResponse(
        @SerializedName("pdpGetARData")
        @Expose
        val data: PdpGetARData = PdpGetARData()
)

data class PdpGetARData(
        @SerializedName("optionBgImage")
        @Expose
        val optionBgImage: String = "",
        @SerializedName("options")
        @Expose
        val productArs: List<ProductAr> = listOf(),
        @SerializedName("provider")
        @Expose
        val provider: String = "",
        @SerializedName("metadata")
        @Expose
        val metaData: ArMetaData = ArMetaData()
)

data class ArMetaData(
        @SerializedName("shopName")
        @Expose
        val shopName: String = "",
        @SerializedName("categoryID")
        @Expose
        val categoryID: String = "",
        @SerializedName("shopType")
        @Expose
        val shopType: String = "",
        @SerializedName("categoryName")
        @Expose
        val categoryName: String = "",
        @SerializedName("categoryDetail")
        @Expose
        val categoryDetail: List<Category> = listOf()
)