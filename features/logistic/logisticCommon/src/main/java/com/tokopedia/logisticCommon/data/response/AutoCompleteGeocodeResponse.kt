package com.tokopedia.logisticCommon.data.response

import com.google.gson.annotations.SerializedName

data class AutoCompleteGeocodeResponse (
        @SerializedName("keroAutocompleteGeocode")
        val keroAutocompleteGeocode: KeroAutocompleteGeocode = KeroAutocompleteGeocode()
)

data class KeroAutocompleteGeocode(
        @SerializedName("server_process_time")
        val serverProcessTime: String = "",
        @SerializedName("data")
        val data: GeoData = GeoData(),
        @SerializedName("config")
        val config: String = "",
        @SerializedName("status")
        val status: String = ""
)

data class GeoData(
        @SerializedName("Results")
        val results: List<ResultsItem> = emptyList(),
        @SerializedName("NextPageToken")
        val nextPageToken: String = "",
        @SerializedName("HTMLAttributions")
        val hTMLAttributions: List<Any> = emptyList()
)

data class ResultsItem(
        @SerializedName("formatted_address")
        val formattedAddress: String = "",
        @SerializedName("types")
        val types: List<String> = emptyList(),
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("rating")
        val rating: Double = 0.0,
        @SerializedName("photos")
        val photos: List<PhotosItem> = emptyList(),
        @SerializedName("permanently_closed")
        val permanentlyClosed: Boolean = false,
        @SerializedName("alt_ids")
        val altIds: List<Any> = emptyList(),
        @SerializedName("price_level")
        val priceLevel: Int = 0,
        @SerializedName("scope")
        val scope: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("opening_hours")
        val openingHours: OpeningHours = OpeningHours(),
        @SerializedName("geometry")
        val geometry: Geometry = Geometry(),
        @SerializedName("vicinity")
        val vicinity: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("place_id")
        val placeId: String = ""
)

data class PhotosItem(
        @SerializedName("photo_reference")
        val photoReference: String = "",
        @SerializedName("width")
        val width: Int = 0,
        @SerializedName("html_attributions")
        val htmlAttributions: List<String> = emptyList(),
        @SerializedName("height")
        val height: Int = 0
)

data class OpeningHours(
        @SerializedName("open_now")
        val openNow: Boolean = false,
        @SerializedName("permanently_closed")
        val permanentlyClosed: Any = Any(),
        @SerializedName("periods")
        val periods: List<Any> = emptyList(),
        @SerializedName("weekday_text")
        val weekdayText: List<Any> = emptyList()
)

data class Geometry(
        @SerializedName("types")
        val types: List<Any> = emptyList(),
        @SerializedName("viewport")
        val viewport: Viewport = Viewport(),
        @SerializedName("bounds")
        val bounds: Bounds = Bounds(),
        @SerializedName("location")
        val location: Location = Location(),
        @SerializedName("location_type")
        val locationType: String = ""
)

data class Viewport(
        @SerializedName("southwest")
        val southwest: Southwest = Southwest(),
        @SerializedName("northeast")
        val northeast: Northeast = Northeast()
)

data class Bounds(
        @SerializedName("southwest")
        val southwest: Southwest = Southwest(),
        @SerializedName("northeast")
        val northeast: Northeast = Northeast()
)

data class Southwest(
        @SerializedName("lng")
        val lng: Double = 0.0,
        @SerializedName("lat")
        val lat: Double = 0.0
)

data class Northeast(
        @SerializedName("lng")
        val lng: Double = 0.0,
        @SerializedName("lat")
        val lat: Double = 0.0
)

data class Location(
        @SerializedName("lng")
        val lng: Double = 0.0,
        @SerializedName("lat")
        val lat: Double = 0.0
)