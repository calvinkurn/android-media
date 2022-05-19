package com.tokopedia.play.broadcaster.domain.model.socket

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by kenny.hadisaputra on 11/02/22
 */
@SuppressLint("ResponseFieldAnnotation")
data class SectionedProductTagSocketResponse(
    @SerializedName("sections")
    val sections: List<Section> = emptyList(),
) {

    data class Section(
        @SerializedName("type")
        val type: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("products")
        val products: List<Product> = emptyList(),
    )

    data class Product(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("image_url")
        val imageUrl: String = "",

        @SerializedName("original_price")
        val originalPrice: Double = 0.0,

        @SerializedName("original_price_formatted")
        val originalPriceFmt: String = "",

        @SerializedName("discount")
        val discount: Double = 0.0,

        @SerializedName("price")
        val price: Double = 0.0,

        @SerializedName("price_formatted")
        val priceFmt: String = "",

        @SerializedName("quantity")
        val quantity: Long = 0,
    )
}