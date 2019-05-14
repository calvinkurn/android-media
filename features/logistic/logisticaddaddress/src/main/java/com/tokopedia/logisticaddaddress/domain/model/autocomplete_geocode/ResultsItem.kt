package com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ResultsItem(

	@field:SerializedName("formatted_address")
	val formattedAddress: String = "",

	@field:SerializedName("types")
	val types: List<String> = emptyList(),

	@field:SerializedName("icon")
	val icon: String = "",

	@field:SerializedName("rating")
	val rating: Double = 0.0,

	@field:SerializedName("photos")
	val photos: List<PhotosItem> = emptyList(),

	@field:SerializedName("permanently_closed")
	val permanentlyClosed: Boolean = false,

	@field:SerializedName("alt_ids")
	val altIds: List<Any> = emptyList(),

	@field:SerializedName("price_level")
	val priceLevel: Int = 0,

	@field:SerializedName("scope")
	val scope: String = "",

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("opening_hours")
	val openingHours: OpeningHours = OpeningHours(),

	@field:SerializedName("geometry")
	val geometry: Geometry = Geometry(),

	@field:SerializedName("vicinity")
	val vicinity: String = "",

	@field:SerializedName("id")
	val id: String = "",

	@field:SerializedName("place_id")
	val placeId: String = ""
)