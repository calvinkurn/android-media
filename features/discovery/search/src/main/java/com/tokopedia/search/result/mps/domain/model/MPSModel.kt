package com.tokopedia.search.result.mps.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MPSModel(
    @SerializedName("aceSearchShop")
    @Expose
    val aceSearchShop: AceSearchShop = AceSearchShop(),
) {

    val shopList
        get() = aceSearchShop.shopList

    data class AceSearchShop(
        @SerializedName("shops")
        @Expose
        val shopList: List<Shop> = listOf(),
    ) {

        data class Shop(
            @SerializedName("shop_id")
            @Expose
            val id: String = "0",

            @SerializedName("shop_name")
            @Expose
            val name: String = "",

            @SerializedName("shop_image")
            @Expose
            val image: String = "",

            @SerializedName("shop_location")
            @Expose
            val location: String = "",

            @SerializedName("shop_badge")
            @Expose
            val badge: Badge = Badge(),

            @SerializedName("button_title")
            @Expose
            val buttonTitle: String = "",

            @SerializedName("free_ongkir")
            @Expose
            val freeOngkir: FreeOngkir = FreeOngkir(),

            @SerializedName("products")
            @Expose
            val productList: List<Product> = listOf(),
        ) {

            data class Badge(
                @SerializedName("image_url")
                @Expose
                val imageUrl: String = "",

                @SerializedName("show")
                @Expose
                val isShow: Boolean = false,
            )

            data class FreeOngkir(
                @SerializedName("image_url")
                @Expose
                val imageUrl: String = "",

                @SerializedName("is_active")
                @Expose
                val isActive: Boolean = false,
            )

            data class Product(
                @SerializedName("id")
                @Expose
                val id: String = "0",

                @SerializedName("name")
                @Expose
                val name: String = "",

                @SerializedName("applink")
                @Expose
                val applink: String = "",

                @SerializedName("original_price")
                @Expose
                val originalPrice: String = "",

                @SerializedName("discount_percentage")
                @Expose
                val discountPercentage: String = "",

                @SuppressLint("Invalid Data Type")
                @SerializedName("price")
                @Expose
                val price: Int = 0,

                @SerializedName("price_format")
                @Expose
                val priceFormat: String = "",

                @SerializedName("image_url")
                @Expose
                val imageUrl: String = "",

                @SerializedName("rating_average")
                @Expose
                val ratingAverage: String = "",

                @SerializedName("label_groups")
                @Expose
                val labelGroupList: List<LabelGroup> = listOf(),
            ) {

                data class LabelGroup(
                    @SerializedName("position")
                    @Expose
                    val position: String = "",

                    @SerializedName("title")
                    @Expose
                    val title: String = "",

                    @SerializedName("type")
                    @Expose
                    val type: String = "",

                    @SerializedName("url")
                    @Expose
                    val url: String = "",
                )
            }
        }
    }
}
