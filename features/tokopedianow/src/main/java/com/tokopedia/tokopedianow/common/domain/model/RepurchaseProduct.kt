package com.tokopedia.tokopedianow.common.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toFloatOrZero

data class RepurchaseProduct(
    @Expose
    @SerializedName("id")
    val id: String = "",
    @Expose
    @SerializedName("name")
    val name: String = "",
    @Expose
    @SerializedName("url")
    val url: String = "",
    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @Expose
    @SerializedName("price")
    @SuppressLint("Invalid Data Type")
    val price: String = "",
    @Expose
    @SerializedName("slashedPrice")
    val slashedPrice: String = "",
    @Expose
    @SerializedName("discountPercentage")
    val discountPercentage: String = "",
    @Expose
    @SerializedName("parentProductId")
    val parentProductId: String = "",
    @Expose
    @SerializedName("rating")
    val rating: String = "",
    @Expose
    @SerializedName("ratingAverage")
    val ratingAverage: String = "",
    @Expose
    @SerializedName("countReview")
    val countReview: String = "",
    @SerializedName("minOrder")
    val minOrder: Int = 0,
    @SerializedName("maxOrder")
    val maxOrder: Int = 0,
    @Expose
    @SerializedName("stock")
    val stock: Int = 0,
    @Expose
    @SerializedName("category")
    val category: String = "",
    @Expose
    @SerializedName("categoryID")
    val categoryId: String = "",
    @Expose
    @SerializedName("campaignCode")
    val campaignCode: String = "",
    @Expose
    @SerializedName("wishlist")
    val wishlist: Boolean = false,
    @Expose
    @SerializedName("labelGroup")
    val labelGroup: List<LabelGroup> = emptyList(),
    @Expose
    @SerializedName("labelGroupVariant")
    val labelGroupVariant: List<LabelGroupVariant> = emptyList(),
    @Expose
    @SerializedName("shop")
    val shop: Shop = Shop()
) {
    companion object {
        private const val PERCENTAGE = "%"
    }

    fun isVariant(): Boolean {
        return parentProductId != "0" &&
            parentProductId.isNotBlank()
    }

    fun getDiscount(): String {
        val discount = discountPercentage.toFloatOrZero()
        return if(discount > 0f) "$discountPercentage$PERCENTAGE" else ""
    }

    data class LabelGroup(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("position")
        @Expose
        val position: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("url")
        @Expose
        val url: String = ""
    )

    data class LabelGroupVariant(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("typeVariant")
        @Expose
        val typeVariant: String = "",
        @SerializedName("hexColor")
        @Expose
        val hexColor: String = ""
    )

    data class Shop(
        @SerializedName("id")
        @Expose
        val id: String = ""
    )
}
