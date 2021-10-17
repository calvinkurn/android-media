package com.tokopedia.wishlist.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class WishlistV2Response(
	@SerializedName("data")
	val data: Data
) {
	data class Data(
		@SerializedName("wishlist_v2")
		val wishlistV2: WishlistV2
	) {
		data class WishlistV2(
				@SerializedName("error_message")
				val errorMessage: String,

				@SerializedName("offset")
				val offset: Int,

				@SerializedName("has_next_page")
				val hasNextPage: Boolean,

				@SerializedName("query")
				val query: String,

				@SerializedName("sort_filters")
				val sortFilters: List<SortFiltersItem>,

				@SerializedName("limit")
				val limit: Int,

				@SerializedName("total_data")
				val totalData: Int,

				@SerializedName("next_page_url")
				val nextPageUrl: String,

				@SerializedName("page")
				val page: Int,

				@SerializedName("items")
				val items: List<ItemsItem>,

				@SerializedName("empty_state")
				val emptyState: EmptyState
		) {
			data class ItemsItem(

					@SerializedName("original_price")
					val originalPrice: String,

					@SerializedName("label_group")
					val labelGroup: List<LabelGroupItem>,

					@SerializedName("shop")
					val shop: Shop,

					@SerializedName("price_fmt")
					val priceFmt: String,

					@SerializedName("available")
					val available: Boolean,

					@SerializedName("rating")
					val rating: String,

					@SerializedName("original_price_fmt")
					val originalPriceFmt: String,

					@SerializedName("discount_percentage")
					val discountPercentage: Int,

					@SerializedName("default_child_id")
					val defaultChildId: String,

					@SuppressLint("Invalid Data Type")
					@SerializedName("price")
					val price: String,

					@SerializedName("wholesale_price")
					val wholesalePrice: List<WholesalePriceItem>,

					@SerializedName("id")
					val id: String,

					@SerializedName("buttons")
					val buttons: Buttons,

					@SerializedName("image_url")
					val imageUrl: String,

					@SerializedName("discount_percentage_fmt")
					val discountPercentageFmt: String,

					@SerializedName("wishlist_id")
					val wishlistId: String,

					@SerializedName("variant_name")
					val variantName: String,

					@SerializedName("label_stock")
					val labelStock: String,

					@SerializedName("url")
					val url: String,

					@SerializedName("label_status")
					val labelStatus: String,

					@SerializedName("labels")
					val labels: List<String>,

					@SerializedName("badges")
					val badges: List<BadgesItem>,

					@SerializedName("name")
					val name: String,

					@SerializedName("min_order")
					val minOrder: String,

					@SerializedName("bebas_ongkir")
					val bebasOngkir: BebasOngkir,

					@SerializedName("category")
					val category: List<CategoryItem>,

					@SerializedName("preorder")
					val preorder: Boolean,

					@SerializedName("sold_count")
					val soldCount: String
			) {
				data class LabelGroupItem(
						@SerializedName("position")
						val position: String,

						@SerializedName("title")
						val title: String,

						@SerializedName("type")
						val type: String,

						@SerializedName("url")
						val url: String
				)

				data class WholesalePriceItem(
						@SuppressLint("Invalid Data Type")
						@SerializedName("price")
						val price: String,

						@SerializedName("maximum")
						val maximum: String,

						@SerializedName("minimum")
						val minimum: String
				)

				data class BadgesItem(
						@SerializedName("image_url")
						val imageUrl: String,

						@SerializedName("title")
						val title: String
				)

				data class BebasOngkir(

						@SerializedName("image_url")
						val imageUrl: String,

						@SerializedName("type")
						val type: Int,

						@SerializedName("title")
						val title: String
				)

				data class CategoryItem(

						@SerializedName("category_name")
						val categoryName: String,

						@SuppressLint("Invalid Data Type")
						@SerializedName("category_id")
						val categoryId: Int
				)

				data class Shop(

						@SerializedName("is_tokonow")
						val isTokonow: Boolean,

						@SerializedName("name")
						val name: String,

						@SerializedName("location")
						val location: String,

						@SerializedName("id")
						val id: String,

						@SerializedName("fulfillment")
						val fulfillment: Fulfillment,

						@SerializedName("url")
						val url: String
				) {
					data class Fulfillment(

							@SerializedName("text")
							val text: String,

							@SerializedName("is_fulfillment")
							val isFulfillment: Boolean
					)
				}

				data class Buttons(

						@SerializedName("additional_buttons")
						val additionalButtons: List<AdditionalButtonsItem>,

						@SerializedName("primary_button")
						val primaryButton: PrimaryButton
				) {
					data class AdditionalButtonsItem(

							@SerializedName("action")
							val action: String,

							@SerializedName("text")
							val text: String,

							@SerializedName("url")
							val url: String
					)

					data class PrimaryButton(

							@SerializedName("action")
							val action: String,

							@SerializedName("text")
							val text: String,

							@SerializedName("url")
							val url: String
					)
				}
			}

			data class EmptyState(

					@SerializedName("button")
					val button: Button,

					@SerializedName("messages")
					val messages: List<Any>,

					@SerializedName("type")
					val type: String
			) {
				data class Button(

						@SerializedName("action")
						val action: String,

						@SerializedName("text")
						val text: String,

						@SerializedName("url")
						val url: String
				)
			}

			data class SortFiltersItem(

					@SerializedName("selection_type")
					val selectionType: Int,

					@SerializedName("is_active")
					val isActive: Boolean,

					@SerializedName("name")
					val name: String,

					@SerializedName("options")
					val options: List<OptionsItem>,

					@SuppressLint("Invalid Data Type")
					@SerializedName("id")
					val id: Int,

					@SerializedName("text")
					val text: String
			) {
				data class OptionsItem(

						@SerializedName("is_selected")
						val isSelected: Boolean,

						@SerializedName("description")
						val description: String,

						@SerializedName("option_id")
						val optionId: String,

						@SerializedName("text")
						val text: String
				)
			}
		}
	}
}
