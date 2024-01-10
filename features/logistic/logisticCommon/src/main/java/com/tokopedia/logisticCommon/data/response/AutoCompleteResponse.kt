package com.tokopedia.logisticCommon.data.response

import com.google.gson.annotations.SerializedName

data class AutoCompleteResponse(
    @SerializedName("kero_maps_autocomplete")
    var keroMapsAutocomplete: KeroMapsAutocomplete = KeroMapsAutocomplete()
)

data class KeroMapsAutocomplete(
    @SerializedName("data")
    var AData: AData = AData(),
    @SerializedName("error_code")
    var errorCode: Int = 0
)

data class AData(
    @SerializedName("predictions")
    var predictions: List<Prediction> = listOf()
)

data class Prediction(
    @SerializedName("description")
    var description: String = "",
    @SerializedName("matched_substrings")
    var matchedSubstrings: List<MatchedSubstring> = listOf(),
    @SerializedName("place_id")
    var placeId: String = "",
    @SerializedName("structured_formatting")
    var structuredFormatting: StructuredFormatting = StructuredFormatting(),
    @SerializedName("terms")
    var terms: List<Term> = listOf(),
    @SerializedName("types")
    var types: List<String> = listOf(),
    @SerializedName("district_id")
    var districtId: Long = 0L,
    @SerializedName("city_id")
    var cityId: Long = 0L,
    @SerializedName("province_id")
    var provinceId: Long = 0L,
    @SerializedName("district_name")
    var districtName: String = "",
    @SerializedName("city_name")
    var cityName: String = "",
    @SerializedName("province_name")
    var provinceName: String = "",
    @SerializedName("postal_code")
    var postalCode: String = "",
    @SerializedName("latitude")
    var latitude: String = "0.0",
    @SerializedName("longitude")
    var longitude: String = "0.0",
    @SerializedName("title")
    var title: String = ""
)

data class MatchedSubstring(
    @SerializedName("length")
    var length: Int = 0,
    @SerializedName("offset")
    var offset: Int = 0
)

data class StructuredFormatting(
    @SerializedName("main_text")
    var mainText: String = "",
    @SerializedName("main_text_matched_substrings")
    var mainTextMatchedSubstrings: List<MainTextMatchedSubstring> = listOf(),
    @SerializedName("secondary_text")
    var secondaryText: String = ""
)

data class Term(
    @SerializedName("offset")
    var offset: Int = 0,
    @SerializedName("value")
    var value: String = ""
)

data class MainTextMatchedSubstring(
    @SerializedName("length")
    var length: Int = 0,
    @SerializedName("offset")
    var offset: Int = 0
)
