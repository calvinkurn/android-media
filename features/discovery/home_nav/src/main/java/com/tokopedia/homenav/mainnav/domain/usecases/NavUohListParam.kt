package com.tokopedia.homenav.mainnav.domain.usecases


import com.google.gson.annotations.SerializedName

data class NavUohListParam(
        @SerializedName("UUID")
        var uUID: String = "",

        @SerializedName("VerticalID")
        var verticalID: String = "",

        @SerializedName("VerticalCategory")
        var verticalCategory: String = "",

        @SerializedName("Status")
        var status: String = "Dalam Proses",

        @SerializedName("SearchableText")
        var searchableText: String = "",

        @SerializedName("CreateTime")
        var createTime: String = "",

        @SerializedName("CreateTimeStart")
        var createTimeStart: String = "",

        @SerializedName("CreateTimeEnd")
        var createTimeEnd: String = "",

        @SerializedName("Page")
        var page: Int = 0,

        @SerializedName("Limit")
        var limit: Int = 7,

        @SerializedName("SortBy")
        var sortBy: String = "",

        @SerializedName("IsSortAsc")
        var isSortAsc: Boolean = false
    )