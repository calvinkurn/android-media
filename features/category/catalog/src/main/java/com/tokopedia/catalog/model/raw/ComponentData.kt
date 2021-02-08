package com.tokopedia.catalog.model.raw

import com.google.gson.annotations.SerializedName

data class ComponentData(
        @SerializedName("name")
        val name : String,
        @SerializedName("row")
        val row : List<Row> = listOf()

){
    data class Row (
            val icon: String,
            val key: String,
            val value: List<String>
    )
}