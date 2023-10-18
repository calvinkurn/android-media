package com.tokopedia.wishlistcommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetWishlistV2Response(
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("wishlist_v2")
        val wishlistV2: WishlistV2 = WishlistV2()
    ) {
        data class WishlistV2(
            @SerializedName("error_message")
            val errorMessage: String = "",

            @SerializedName("offset")
            val offset: Int = -1,

            @SerializedName("has_next_page")
            val hasNextPage: Boolean = false,

            @SerializedName("query")
            val query: String = "",

            @SerializedName("sort_filters")
            val sortFilters: List<SortFiltersItem> = emptyList(),

            @SerializedName("limit")
            val limit: Int = -1,

            @SerializedName("total_data")
            val totalData: Int = -1,

            @SerializedName("next_page_url")
            val nextPageUrl: String = "",

            @SerializedName("page")
            val page: Int = -1,

            @SerializedName("items")
            val items: List<Item> = emptyList(),

            @SerializedName("empty_state")
            val emptyState: EmptyState = EmptyState()
        ) {
            data class Item(

                @SerializedName("original_price")
                val originalPrice: String = "",

                @SerializedName("label_group")
                val labelGroup: List<LabelGroupItem> = emptyList(),

                @SerializedName("shop")
                val shop: Shop = Shop(),

                @SerializedName("price_fmt")
                val priceFmt: String = "",

                @SerializedName("available")
                val available: Boolean = false,

                @SerializedName("rating")
                val rating: String = "",

                @SerializedName("original_price_fmt")
                val originalPriceFmt: String = "",

                @SerializedName("discount_percentage")
                val discountPercentage: Int = -1,

                @SerializedName("default_child_id")
                val defaultChildId: String = "",

                @SuppressLint("Invalid Data Type")
                @SerializedName("price")
                val price: String = "",

                @SuppressLint("Invalid Data Type")
                @SerializedName("wholesale_price")
                val wholesalePrice: List<WholesalePriceItem> = emptyList(),

                @SerializedName("id")
                val id: String = "",

                @SerializedName("buttons")
                val buttons: Buttons = Buttons(),

                @SerializedName("image_url")
                val imageUrl: String = "",

                @SerializedName("discount_percentage_fmt")
                val discountPercentageFmt: String = "",

                @SerializedName("wishlist_id")
                val wishlistId: String = "",

                @SerializedName("variant_name")
                val variantName: String = "",

                @SerializedName("label_stock")
                val labelStock: String = "",

                @SerializedName("url")
                val url: String = "",

                @SerializedName("label_status")
                val labelStatus: String = "",

                @SerializedName("labels")
                val labels: List<String> = emptyList(),

                @SerializedName("badges")
                val badges: List<BadgesItem> = emptyList(),

                @SerializedName("name")
                val name: String = "",

                @SerializedName("min_order")
                val minOrder: String = "",

                @SerializedName("bebas_ongkir")
                val bebasOngkir: BebasOngkir = BebasOngkir(),

                @SerializedName("category")
                val category: List<CategoryItem> = emptyList(),

                @SerializedName("preorder")
                val preorder: Boolean = false,

                @SerializedName("sold_count")
                val soldCount: String = ""
            ) {
                data class LabelGroupItem(
                    @SerializedName("position")
                    val position: String = "",

                    @SerializedName("title")
                    val title: String = "",

                    @SerializedName("type")
                    val type: String = "",

                    @SerializedName("url")
                    val url: String = ""
                )

                data class WholesalePriceItem(
                    @SuppressLint("Invalid Data Type")
                    @SerializedName("price")
                    val price: String = "",

                    @SerializedName("maximum")
                    val maximum: String = "",

                    @SerializedName("minimum")
                    val minimum: String = ""
                )

                data class BadgesItem(
                    @SerializedName("image_url")
                    val imageUrl: String = "",

                    @SerializedName("title")
                    val title: String = ""
                )

                data class BebasOngkir(

                    @SerializedName("image_url")
                    val imageUrl: String = "",

                    @SerializedName("type")
                    val type: Int = -1,

                    @SerializedName("title")
                    val title: String = ""
                )

                data class CategoryItem(

                    @SerializedName("category_name")
                    val categoryName: String = "",

                    @SuppressLint("Invalid Data Type")
                    @SerializedName("category_id")
                    val categoryId: Int = -1
                )

                data class Shop(

                    @SerializedName("is_tokonow")
                    val isTokonow: Boolean = false,

                    @SerializedName("name")
                    val name: String = "",

                    @SerializedName("location")
                    val location: String = "",

                    @SerializedName("id")
                    val id: String = "",

                    @SerializedName("fulfillment")
                    val fulfillment: Fulfillment = Fulfillment(),

                    @SerializedName("url")
                    val url: String = ""
                ) {
                    data class Fulfillment(

                        @SerializedName("text")
                        val text: String = "",

                        @SerializedName("is_fulfillment")
                        val isFulfillment: Boolean = false
                    )
                }

                data class Buttons(

                    @SerializedName("additional_buttons")
                    val additionalButtons: List<AdditionalButtonsItem> = emptyList(),

                    @SerializedName("primary_button")
                    val primaryButton: PrimaryButton = PrimaryButton()
                ) {
                    data class AdditionalButtonsItem(

                        @SerializedName("action")
                        val action: String = "",

                        @SerializedName("text")
                        val text: String = "",

                        @SerializedName("url")
                        val url: String = ""
                    )

                    data class PrimaryButton(

                        @SerializedName("action")
                        val action: String = "",

                        @SerializedName("text")
                        val text: String = "",

                        @SerializedName("url")
                        val url: String = ""
                    )
                }
            }

            data class EmptyState(

                @SerializedName("button")
                val button: Button = Button(),

                @SerializedName("messages")
                val messages: List<Any> = emptyList(),

                @SerializedName("type")
                val type: String = ""
            ) {
                data class Button(

                    @SerializedName("action")
                    val action: String = "",

                    @SerializedName("text")
                    val text: String = "",

                    @SerializedName("url")
                    val url: String = ""
                )
            }

            data class SortFiltersItem(

                @SerializedName("selection_type")
                val selectionType: Int = -1,

                @SerializedName("is_active")
                val isActive: Boolean = false,

                @SerializedName("name")
                val name: String = "",

                @SerializedName("options")
                val options: List<OptionsItem> = emptyList(),

                @SuppressLint("Invalid Data Type")
                @SerializedName("id")
                val id: Int = -1,

                @SerializedName("text")
                val text: String = ""
            ) {
                data class OptionsItem(

                    @SerializedName("is_selected")
                    val isSelected: Boolean = false,

                    @SerializedName("description")
                    val description: String = "",

                    @SerializedName("option_id")
                    val optionId: String = "",

                    @SerializedName("text")
                    val text: String = ""
                )
            }
        }
    }
}
