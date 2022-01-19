package com.tokopedia.product_ar.model


import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.common.data.model.variant.VariantCampaign

data class ProductAr(
        @SerializedName("button")
        @Expose
        val button: ProductArButton = ProductArButton(),
        @SerializedName("campaignInfo")
        @Expose
        val campaignInfo: VariantCampaign = VariantCampaign(),
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
        val type: String = "",
        @SerializedName("unavailableCopy")
        @Expose
        val unavailableCopy: String = "",
        @SerializedName("minOrder")
        @Expose
        val minOrder: Int = 0,
        @Transient
        val providerDataCompiled: ModifaceProvider? = null
) {
    fun getFinalMinOrder(): Int {
        return if (campaignInfo.isActive == true) campaignInfo.minOrder ?: 0 else minOrder
    }

    fun getFinalPrice(): Double {
        return if (campaignInfo.isActive == true) campaignInfo.discountedPrice ?: 0.0 else price
    }

    fun getFinalStock(): Int {
        return if (campaignInfo.isActive == true) campaignInfo.stock ?: 0 else stock.toIntOrZero()
    }
}

data class ProductArButton(
        @SerializedName("text")
        @Expose
        val text: String = "",

        @SerializedName("cart_type")
        @Expose
        val cartType: String = "",

        @SerializedName("color")
        @Expose
        val color: String = ""
)