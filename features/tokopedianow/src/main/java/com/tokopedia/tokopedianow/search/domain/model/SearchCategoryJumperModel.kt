package com.tokopedia.tokopedianow.search.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchCategoryJumperModel(
        @SerializedName("searchJumper")
        @Expose
        val jumperData: SearchCategoryJumperData = SearchCategoryJumperData(),
) {

    data class SearchCategoryJumperData(
            @SerializedName("data")
            @Expose
            val data: SearchCategoryJumper = SearchCategoryJumper(),
    ) {

        fun getTitle() = data.title

        fun getJumperItemList() = data.data
    }

    data class SearchCategoryJumper(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("data")
            @Expose
            val data: List<JumperData> = listOf(),
    )

    data class JumperData(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",
    )

}