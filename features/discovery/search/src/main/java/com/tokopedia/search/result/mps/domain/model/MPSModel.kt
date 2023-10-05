package com.tokopedia.search.result.mps.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DynamicFilterModel

data class MPSModel(
    @SerializedName("ace_search_shop_mps")
    @Expose
    val searchShopMPS: SearchShopMPS = SearchShopMPS(),

    @SerializedName("filter_sort_product")
    @Expose
    val quickFilterModel: DynamicFilterModel = DynamicFilterModel(),
) {

    val totalData
        get() = searchShopMPS.header.totalData

    val shopList
        get() = searchShopMPS.shopList

    val quickFilterList
        get() = quickFilterModel.data.filter

    val responseCode
        get() = searchShopMPS.header.responseCode

    data class SearchShopMPS(
        @SerializedName("header")
        @Expose
        val header: Header = Header(),

        @SerializedName("data")
        @Expose
        val shopList: List<Shop> = listOf(),
    ) {

        data class Header(
            @SerializedName("total_data")
            @Expose
            val totalData: Long = 0,

            @SerializedName("treatment_code")
            @Expose
            val treatmentCode: String = "0",

            @SerializedName("response_code")
            @Expose
            val responseCode: String = "0",
        )

        data class Shop(
            @SerializedName("id")
            @Expose
            val id: String = "0",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("city")
            @Expose
            val city: String = "",

            @SerializedName("location")
            @Expose
            val location: String = "",

            @SerializedName("subtitle")
            @Expose
            val subtitle: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("image_url")
            @Expose
            val imageUrl: String = "",

            @SerializedName("component_id")
            @Expose
            val componentId: String = "",

            @SerializedName("tracking_option")
            @Expose
            val trackingOption: Int = 0,

            @SerializedName("ticker")
            @Expose
            val ticker: Ticker = Ticker(),

            @SerializedName("badges")
            @Expose
            val badgeList: List<Badge> = listOf(),

            @SerializedName("free_ongkir")
            @Expose
            val freeOngkir: FreeOngkir = FreeOngkir(),

            @SerializedName("products")
            @Expose
            val productList: List<Product> = listOf(),

            @SerializedName("buttons")
            @Expose
            val buttonList: List<Button> = listOf(),
        ) {

            val productCardList
                get() = productList.filter { it.id != "0" }

            val viewAllCard: Button?
                get() = productList
                    .firstOrNull { it.id == "0" }
                    ?.buttonList
                    ?.firstOrNull()

            data class Ticker(
                @SerializedName("type")
                @Expose
                val type: String = "",

                @SerializedName("message")
                @Expose
                val message: String = "",
            )

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

            data class Button(
                @SerializedName("name")
                @Expose
                val name: String = "",

                @SerializedName("applink")
                @Expose
                val applink: String = "",

                @SerializedName("text")
                @Expose
                val text: String = "",

                @SerializedName("is_cta")
                @Expose
                val isCTA: Boolean = false,

                @SerializedName("component_id")
                @Expose
                val componentId: String = "",

                @SerializedName("tracking_option")
                @Expose
                val trackingOption: Int = 0,
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

                @SerializedName("image_url")
                @Expose
                val imageUrl: String = "",

                @SuppressLint("Invalid Data Type")
                @SerializedName("price")
                @Expose
                val price: Int = 0,

                @SerializedName("price_format")
                @Expose
                val priceFormat: String = "",

                @SerializedName("original_price")
                @Expose
                val originalPrice: String = "",

                @SerializedName("discount_percentage")
                @Expose
                val discountPercentage: Int = 0,

                @SerializedName("rating_average")
                @Expose
                val ratingAverage: String = "",

                @SerializedName("parent_id")
                @Expose
                val parentId: String = "",

                @SerializedName("stock")
                @Expose
                val stock: Int = 0,

                @SerializedName("min_order")
                @Expose
                val minOrder: Int = 0,

                @SerializedName("component_id")
                @Expose
                val componentId: String = "",

                @SerializedName("tracking_option")
                @Expose
                val trackingOption: Int = 0,

                @SerializedName("label_groups")
                @Expose
                val labelGroupList: List<LabelGroup> = listOf(),

                @SerializedName("buttons")
                @Expose
                val buttonList: List<Button> = listOf(),
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
