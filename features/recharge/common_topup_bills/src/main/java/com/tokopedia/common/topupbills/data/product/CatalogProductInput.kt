package com.tokopedia.common.topupbills.data.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
class CatalogProductInput (

        @SerializedName("id")
        @Expose
        var id: String = "",
        @SerializedName("param_name")
        @Expose
        var paramName: String = "",
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("style")
        @Expose
        var style: String = "",
        @SerializedName("text")
        @Expose
        var text: String = "",
        @SerializedName("placeholder")
        @Expose
        var placeholder: String = "",
        @SerializedName("help")
        @Expose
        var help: String = "",
        @SerializedName("data_collections")
        @Expose
        var dataCollections: List<DataCollection> = listOf(),
        @SerializedName("validations")
        @Expose
        var validations: List<Validation> = listOf()

) {
        class Validation(
                @SerializedName("id")
                @Expose
                var id: Int = 0,
                @SerializedName("title")
                @Expose
                var title: String = "",
                @SerializedName("rule")
                @Expose
                var rule: String = "",
                @SerializedName("message")
                @Expose
                var message: String = ""
        )

        class DataCollection(
                @SerializedName("name")
                @Expose
                var name: String = "",
                @SerializedName("value")
                @Expose
                var value: String = "",
                @SerializedName("products")
                @Expose
                var products: List<CatalogProduct> = listOf()
        )
}