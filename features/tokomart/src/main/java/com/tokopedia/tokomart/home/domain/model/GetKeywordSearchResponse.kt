package com.tokopedia.tokomart.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KeywordSearchData (
        @SerializedName("universe_placeholder")
        @Expose
        val searchData: SearchPlaceholder = SearchPlaceholder()
)

data class SearchPlaceholder(
        @SerializedName("data")
        @Expose
        var data: Data? = Data()
)

data class Data(
        @SerializedName("placeholder_list")
        @Expose
        var placeholders: ArrayList<PlaceHolder>? = arrayListOf(),
        @SerializedName("placeholder")
        @Expose
        var placeholder: String? = "",
        @SerializedName("keyword")
        @Expose
        var keyword: String? = ""
)

data class PlaceHolder(
        @Expose
        var placeholder: String? = "",
        @SerializedName("keyword")
        @Expose
        var keyword: String? = ""
)
