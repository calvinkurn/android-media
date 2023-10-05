package com.tokopedia.entertainment.search.data

import com.google.gson.annotations.SerializedName

data class EventSearchFullLocationResponse(

	@field:SerializedName("data")
	val data: Data = Data()
){
	data class Data(

			@field:SerializedName("event_location_search")
			val eventLocationSearch: EventLocationSearch = EventLocationSearch()
	){
		data class EventLocationSearch(

				@field:SerializedName("count")
				val count: String = "",

				@field:SerializedName("locations")
				val locations: List<LocationsItem> = listOf()
		){
			data class LocationsItem(

					@field:SerializedName("image_app")
					val imageApp: String = "",

					@field:SerializedName("city_name")
					val cityName: String = "",

					@field:SerializedName("icon_app")
					val iconApp: String = "",

					@field:SerializedName("name")
					val name: String = "",

					@field:SerializedName("id")
					val id: String = "",

					@field:SerializedName("search_name")
					val searchName: String = "",

					@field:SerializedName("city_id")
					val cityId: String = ""
			)
		}
	}
}
