package com.tokopedia.wishlistcollection.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetWishlistCollectionItemsResponse(
	@SerializedName("get_wishlist_collection_items")
	val getWishlistCollectionItems: GetWishlistCollectionItems
) {
	data class GetWishlistCollectionItems(

		@SerializedName("show_empty_state_on_bottomsheet")
		val showEmptyStateOnBottomsheet: Boolean = false,

		@SerializedName("error_message")
		val errorMessage: String = "",

		@SerializedName("ticker")
		val ticker: Ticker = Ticker(),

		@SerializedName("has_collection")
		val hasCollection: Boolean = false,

		@SerializedName("offset")
		val offset: Int = 0,

		@SerializedName("header_title")
		val headerTitle: String = "",

		@SerializedName("has_next_page")
		val hasNextPage: Boolean = false,

		@SerializedName("query")
		val query: String = "",

		@SerializedName("total_data")
		val totalData: Int = 0,

		@SerializedName("count_removable_items")
		val countRemovableItems: Int = 0,

        @SerializedName("description")
        val description: String = "",

		@SerializedName("empty_state")
		val emptyState: EmptyState = EmptyState(),

        @SerializedName("collection_type")
        val collectionType: Int = 0,

		@SerializedName("setting")
		val setting: Setting = Setting(),

		@SerializedName("storage_cleaner_bottomsheet")
		val storageCleanerBottomsheet: StorageCleanerBottomsheet = StorageCleanerBottomsheet(),

		@SerializedName("show_delete_progress")
		val showDeleteProgress: Boolean = false,

		@SerializedName("sort_filters")
		val sortFilters: List<SortFiltersItem> = emptyList(),

		@SerializedName("limit")
		val limit: Int = 0,

		@SerializedName("page")
		val page: Int = 0,

		@SerializedName("items")
		val items: List<ItemsItem> = emptyList(),

        @SerializedName("add_wishlist_bulk_config")
        val addWishlistBulkConfig: AddWishlistBulkConfig = AddWishlistBulkConfig()
	) {
		data class Ticker(

			@SerializedName("button")
			val button: Button = Button(),

			@SerializedName("message")
			val message: String = "",

			@SerializedName("type")
			val type: String = ""
		) {
			data class Button(

				@SerializedName("text")
				val text: String = "",

				@SerializedName("color")
				val color: String = "",

				@SerializedName("action")
				val action: String = ""
			)
		}

		data class EmptyState(

            @SerializedName("buttons")
			val buttons: List<Setting.Button> = emptyList(),

            @SerializedName("messages")
			val messages: List<MessageEmptyState> = emptyList(),

            @SerializedName("type")
			val type: String = ""
		) {


			data class MessageEmptyState(
				@SerializedName("title")
				val title: String = "",

				@SerializedName("description")
				val desc: String = "",

				@SerializedName("image_url")
				val imageUrl: String = ""
			)
		}

		data class Setting(
			@SerializedName("buttons")
			val buttons: List<Button> = emptyList()
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

		data class StorageCleanerBottomsheet(

			@SerializedName("button")
			val button: ButtonStorageCleaner = ButtonStorageCleaner(),

			@SerializedName("options")
			val options: List<OptionsItem> = emptyList(),

			@SerializedName("description")
			val description: String = "",

			@SerializedName("title")
			val title: String = ""
		) {
			data class OptionsItem(

				@SerializedName("name")
				val name: String = "",

				@SerializedName("description")
				val description: String = "",

				@SerializedName("action")
				val action: String = "",

				@SerializedName("is_selected")
				val isSelected: Boolean = false,

				@SerializedName("option_id")
				val optionId: String = "",

				@SerializedName("text")
				val text: String = ""
			)

			data class ButtonStorageCleaner(
				@SerializedName("text")
				val text: String = ""
			)
		}

		data class SortFiltersItem(

			@SerializedName("selection_type")
			val selectionType: Int = 0,

			@SerializedName("is_active")
			val isActive: Boolean = false,

			@SerializedName("name")
			val name: String = "",

			@SerializedName("options")
			val options: List<OptionsItem> = emptyList(),

			@SuppressLint("Invalid Data Type")
			@SerializedName("id")
			val id: Int = 0,

			@SerializedName("text")
			val text: String = "",

			@SerializedName("title")
			val title: String = ""
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

		data class ItemsItem(

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
			val discountPercentage: Int = 0,

			@SerializedName("default_child_id")
			val defaultChildId: String = "",

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
				data class PrimaryButton(

					@SerializedName("action")
					val action: String = "",

					@SerializedName("text")
					val text: String = "",

					@SerializedName("url")
					val url: String = ""
				)

				data class AdditionalButtonsItem(

					@SerializedName("action")
					val action: String = "",

					@SerializedName("text")
					val text: String = "",

					@SerializedName("url")
					val url: String = ""
				)
			}

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
				val type: Int = 0,

				@SerializedName("title")
				val title: String = ""
			)

			data class CategoryItem(

				@SerializedName("category_name")
				val categoryName: String = "",

				@SuppressLint("Invalid Data Type")
				@SerializedName("category_id")
				val categoryId: Int = 0
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
		}

        data class AddWishlistBulkConfig(
            @SerializedName("max_bulk")
            val maxBulk: Long = 0L,

            @SerializedName("toaster")
            val addWishlistBulkToaster: AddWishlistBulkToaster = AddWishlistBulkToaster()
        ) {
            data class AddWishlistBulkToaster(
                @SerializedName("message")
                val message: String = ""
            )
        }
	}
}
