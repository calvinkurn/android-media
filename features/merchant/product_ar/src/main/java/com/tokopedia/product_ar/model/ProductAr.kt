package com.tokopedia.product_ar.model


import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular

data class ProductAr(
        @SerializedName("button")
        @Expose
        val button: ProductArButton = ProductArButton(),
        @SerializedName("campaignInfo")
        @Expose
        val campaignInfo: CampaignModular = CampaignModular(),
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        @Expose
        val price: Double = 0.0,
        @SerializedName("productID")
        @Expose
        val productID: String = "",
        @SerializedName("providerData")
        @Expose
        val providerData: String = "",
        @SerializedName("psku")
        @Expose
        val psku: String = "",
        @SerializedName("stock")
        @Expose
        val stock: String = "",
        @SerializedName("stockCopy")
        @Expose
        val stockCopy: String = "",
        @SerializedName("type")
        @Expose
        val type: String = ""
)

data class ProductArButton(
        @SerializedName("text")
        @Expose
        val text: String = ""
)