package com.tokopedia.entertainment.search.data

import com.google.gson.annotations.SerializedName

data class EventSearchHistoryResponse(

	@field:SerializedName("data")
	val data: Data = Data()
){
	data class Data(

			@field:SerializedName("TravelCollectiveRecentSearches")
			val travelCollectiveRecentSearches: TravelCollectiveRecentSearches = TravelCollectiveRecentSearches()
	){
		data class TravelCollectiveRecentSearches(

				@field:SerializedName("meta")
				val meta: Meta = Meta(),

				@field:SerializedName("items")
				val items: List<ItemsItem> = listOf()
		){
			data class Meta(

					@field:SerializedName("webURL")
					val webURL: String = "",

					@field:SerializedName("appURL")
					val appURL: String = "",

					@field:SerializedName("title")
					val title: String = "",

					@field:SerializedName("type")
					val type: String = ""
			)

			data class ItemsItem(

					@field:SerializedName("prefixStyling")
					val prefixStyling: String = "",

					@field:SerializedName("product")
					val product: String = "",

					@field:SerializedName("prefix")
					val prefix: String = "",

					@field:SerializedName("subtitle")
					val subtitle: String = "",

					@field:SerializedName("imageURL")
					val imageURL: String = "",

					@field:SerializedName("appURL")
					val appURL: String = "",

					@field:SerializedName("title")
					val title: String = "",

					@field:SerializedName("value")
					val value: String = ""
			)
		}
	}
}