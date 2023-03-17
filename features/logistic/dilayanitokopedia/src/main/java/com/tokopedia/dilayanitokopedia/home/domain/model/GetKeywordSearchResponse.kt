package com.tokopedia.dilayanitokopedia.home.domain.model

import com.google.gson.annotations.SerializedName

class KeywordSearchData(
    @SerializedName("universe_placeholder")
    val searchData: SearchPlaceholder = SearchPlaceholder()
)

data class SearchPlaceholder(
    @SerializedName("data")
    var data: Data? = Data()
)

data class Data(
    @SerializedName("placeholder_list")
    var placeholders: ArrayList<PlaceHolder>? = arrayListOf(),
    @SerializedName("placeholder")
    var placeholder: String? = "",
    @SerializedName("keyword")
    var keyword: String? = ""
)

data class PlaceHolder(
    @SerializedName("placeholder")
    var placeholder: String? = "",
    @SerializedName("keyword")
    var keyword: String? = ""
)
