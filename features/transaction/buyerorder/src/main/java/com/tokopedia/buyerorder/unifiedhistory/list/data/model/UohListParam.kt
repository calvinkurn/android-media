package com.tokopedia.buyerorder.unifiedhistory.list.data.model


import com.google.gson.annotations.SerializedName

data class UohListParam(
        @SerializedName("UUID")
        var uUID: String = "",

        @SerializedName("VerticalID")
        var verticalID: String = "",

        @SerializedName("VerticalCategory")
        var verticalCategory: String = "",

        @SerializedName("Status")
        var status: String = "",

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
        var limit: Int = 20,

        @SerializedName("SortBy")
        var sortBy: String = "",

        @SerializedName("IsSortAsc")
        var isSortAsc: Boolean = false
    )