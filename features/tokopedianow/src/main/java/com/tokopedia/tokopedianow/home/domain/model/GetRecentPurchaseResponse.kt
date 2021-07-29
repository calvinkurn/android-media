package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.network.exception.Header
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero

data class GetRecentPurchaseResponse(
    @SerializedName("TokonowRepurchaseWidget")
    val response: RecentPurchaseResponse?
) {

    data class RecentPurchaseResponse(
        @SerializedName("header")
        val header: Header,
        @SerializedName("data")
        val data: RecentPurchaseData
    )

    data class RecentPurchaseData(
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("listProduct")
        val products: List<Product> = emptyList()
    )

    data class Product(
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
        val minOrder: String = "",
        @Expose
        @SerializedName("stock")
        val stock: String = "",
        @Expose
        @SerializedName("category")
        val category: String = "",
        @Expose
        @SerializedName("campaignCode")
        val campaignCode: String = "",
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
            return parentProductId.toIntOrZero() != 0
        }

        fun getDiscount(): String {
            val discount = discountPercentage.toFloatOrZero()
            return if(discount > 0f) "$discountPercentage$PERCENTAGE" else ""
        }
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