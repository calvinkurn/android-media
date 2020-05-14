package com.tokopedia.common.topupbills.data.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
class CatalogProductInput (

        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("param_name")
        @Expose
        val paramName: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("style")
        @Expose
        val style: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("placeholder")
        @Expose
        val placeholder: String = "",
        @SerializedName("help")
        @Expose
        val help: String = "",
        @SerializedName("data_collections")
        @Expose
        val dataCollections: List<DataCollection> = listOf(),
        @SerializedName("validations")
        @Expose
        val validations: List<Validation> = listOf()

) {
        class Validation(
                @SerializedName("id")
                @Expose
                val id: Int = 0,
                @SerializedName("title")
                @Expose
                val title: String = "",
                @SerializedName("rule")
                @Expose
                val rule: String = "",
                @SerializedName("message")
                @Expose
                val message: String = ""
        )

        class DataCollection(
                @SerializedName("value")
                @Expose
                val value: String = ""
        )
}