package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TelcoFilterTagComponent(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("param_name")
        @Expose
        val paramName: String = "",
        @SerializedName("data_collections")
        @Expose
        val dataCollections: List<DataCollections> = mutableListOf()
)

data class DataCollections(
        @SerializedName("key")
        @Expose
        val key: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
)