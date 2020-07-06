package com.tokopedia.buyerorder.unifiedhistory.list.data.model


import com.google.gson.annotations.SerializedName

data class UohListParam(
    @SerializedName("input")
    val input: Input = Input()
) {
    data class Input(
        @SerializedName("UUID")
        val uUID: String = "",

        @SerializedName("VerticalID")
        val verticalID: String = "",

        @SerializedName("VerticalCategory")
        val verticalCategory: String = "",

        @SerializedName("Status")
        val status: String = "",

        @SerializedName("SearchableText")
        val searchableText: String = "",

        @SerializedName("CreateTime")
        val createTime: String = "",

        @SerializedName("CreateTimeStart")
        val createTimeStart: String = "",

        @SerializedName("CreateTimeEnd")
        val createTimeEnd: String = "",

        @SerializedName("Page")
        val page: Int = 0,

        @SerializedName("Limit")
        val limit: Int = 0,

        @SerializedName("SortBy")
        val sortBy: String = "",

        @SerializedName("IsSortAsc")
        val isSortAsc: Boolean = false
    )
}