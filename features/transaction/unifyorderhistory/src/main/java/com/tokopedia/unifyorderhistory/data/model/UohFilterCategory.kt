package com.tokopedia.unifyorderhistory.data.model

import com.google.gson.annotations.SerializedName

data class UohFilterCategory(
        @SerializedName("data")
        val data: Data = Data()
) {
    data class Data(
            @SerializedName("uohFilterCategory")
            val uohFilterCategoryData: UohFilterCategoryData = UohFilterCategoryData()
    ) {
        data class UohFilterCategoryData(
                @SerializedName("filtersV2")
                val v2Filters: List<FilterV2> = listOf(),
                @SerializedName("categories")
                val categories: List<Category> = listOf()
        ) {
            data class FilterV2(
                    @SerializedName("isPrimary")
                    val isPrimary: Boolean = false,
                    @SerializedName("label")
                    val label: String = "",
                    @SerializedName("value")
                    val value: String = ""
            )

            data class Category(
                    @SerializedName("value")
                    val value: String = "",
                    @SerializedName("label")
                    val label: String = "",
                    @SerializedName("description")
                    val description: String = "",
                    @SerializedName("category_group")
                    val categoryGroup: String = ""
            )
        }
    }
}
