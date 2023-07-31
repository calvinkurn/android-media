package com.tokopedia.entertainment.search.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class EventDetailResponse(

	@field:SerializedName("data")
	val data: Data = Data()
){
	data class Data(

			@field:SerializedName("event_child_category")
			val eventChildCategory: EventChildCategory = EventChildCategory(),

			@field:SerializedName("event_search")
			val eventSearch: EventSearch = EventSearch()
	){
		data class EventChildCategory(

				@field:SerializedName("categories")
                var categories: List<CategoriesItem> = listOf()
		){
			data class CategoriesItem(

					@field:SerializedName("app_url")
					val appUrl: String = "",

					@field:SerializedName("name")
					val name: String = "",

					@field:SerializedName("id")
					val id: String = "",

					@field:SerializedName("title")
					val title: String = "",

					@field:SerializedName("priority")
					val priority: String = "",

					@field:SerializedName("media_url")
					val mediaUrl: String = ""
			)
		}

		data class EventSearch(

				@field:SerializedName("count")
				val count: String = "",

				@field:SerializedName("products")
				val products: List<ProductsItem> = listOf()
		){
			data class ProductsItem(

					@field:SerializedName("no_promo")
					val noPromo: Boolean = false,

					@field:SerializedName("seo_url")
					val seoUrl: String = "",

					@field:SerializedName("use_pdf")
					val usePdf: Int = 0,

					@field:SerializedName("rating")
					val rating: String = "",

					@field:SerializedName("search_tags")
					val searchTags: String = "",

					@field:SerializedName("min_start_time")
					val minStartTime: String = "",

					@field:SerializedName("image_app")
					val imageApp: String = "",

					@field:SerializedName("offer_text")
					val offerText: String = "",

					@field:SerializedName("has_seat_layout")
					val hasSeatLayout: String = "",

					@field:SerializedName("sale_start_time")
					val saleStartTime: String = "",

					@SuppressLint("Invalid Data Type")
					@SerializedName("price")
					val price: String = "",

					@field:SerializedName("remaining_sale_time")
					val remainingSaleTime: String = "",

					@field:SerializedName("id")
					val id: String = "",

					@field:SerializedName("buy_enabled")
					val buyEnabled: Boolean = false,

					@field:SerializedName("saving_percentage")
					val savingPercentage: String = "",

					@field:SerializedName("likes")
					val likes: Int = 0,

					@field:SerializedName("is_searchable")
					val isSearchable: Int = 0,

					@field:SerializedName("custom_labels")
					val customLabels: String = "",

					@field:SerializedName("thumbs_down")
					val thumbsDown: String = "",

					@field:SerializedName("meta_title")
					val metaTitle: String = "",

					@field:SerializedName("tnc")
					val tnc: String = "",

					@field:SerializedName("long_rich_desc")
					val longRichDesc: String = "",

					@field:SerializedName("display_name")
					val displayName: String = "",

					@field:SerializedName("city_ids")
					val cityIds: String = "",

					@field:SerializedName("display_tags")
					val displayTags: String = "",

					@field:SerializedName("meta_description")
					val metaDescription: String = "",

					@field:SerializedName("saving")
					val saving: String = "",

					@field:SerializedName("child_category_ids")
					val childCategoryIds: String = "",

					@field:SerializedName("short_desc")
					val shortDesc: String = "",

					@field:SerializedName("recommendation_url")
					val recommendationUrl: String = "",

					@field:SerializedName("promotion_text")
					val promotionText: String = "",

					@field:SerializedName("status")
					val status: Int = 0,

					@field:SerializedName("convenience_fee")
					val convenienceFee: String = "",

					@field:SerializedName("has_popup")
					val hasPopup: Boolean = false,

					@field:SerializedName("title")
					val title: String = "",

					@field:SerializedName("meta_keywords")
					val metaKeywords: String = "",

					@field:SerializedName("sold_quantity")
					val soldQuantity: String = "",

					@field:SerializedName("duration")
					val duration: String = "",

					@field:SerializedName("sale_end_date")
					val saleEndDate: String = "",

					@field:SerializedName("city_name")
					val cityName: String = "",

					@field:SerializedName("category_id")
					val categoryId: String = "",

					@field:SerializedName("autocode")
					val autocode: String = "",

					@field:SerializedName("genre")
					val genre: String = "",

					@field:SerializedName("max_end_time")
					val maxEndTime: String = "",

					@field:SerializedName("sell_rate")
					val sellRate: String = "",

					@field:SerializedName("is_liked")
					val isLiked: Boolean = false,

					@field:SerializedName("redirect")
					val redirect: Int = 0,

					@field:SerializedName("date_range")
					val dateRange: Boolean = false,

					@field:SerializedName("max_end_date")
					val maxEndDate: String= "",

					@field:SerializedName("quantity")
					val quantity: String = "",

					@field:SerializedName("app_url")
					val appUrl: String = "",

					@field:SerializedName("mrp")
					val mrp: String= "",

					@field:SerializedName("sale_start_date")
					val saleStartDate: String = "",

					@field:SerializedName("min_start_date")
					val minStartDate: String= "",

					@field:SerializedName("message")
					val message: String = "",

					@field:SerializedName("thumbs_up")
					val thumbsUp: String= "",

					@field:SerializedName("url")
					val url: String = "",

					@field:SerializedName("is_top")
					val isTop: Int = 0,

					@field:SerializedName("salient_features")
					val salientFeatures: String = "",

					@field:SerializedName("is_food_available")
					val isFoodAvailable: Int = 0,

					@field:SerializedName("schedule")
					val schedule: String = "",

					@field:SerializedName("action_text")
					val actionText: String = "",

					@field:SerializedName("web_url")
					val webUrl: String = "",

					@field:SerializedName("seat_chart_type")
					val seatChartType: String = "",

					@field:SerializedName("sale_end_time")
					val saleEndTime: String = "",

					@field:SerializedName("sales_price")
					val salesPrice: String= "",

					@field:SerializedName("location")
					val location: String = "",

					@field:SerializedName("is_featured")
					val isFeatured: Int = 0,

					@field:SerializedName("is_promo")
					val isPromo: Int = 0,

					@field:SerializedName("is_free")
					val isFree: Int = 0
			)
		}
	}
}
