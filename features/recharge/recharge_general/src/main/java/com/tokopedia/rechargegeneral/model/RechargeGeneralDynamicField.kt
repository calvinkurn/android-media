package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.product.CatalogProduct

/**
 * Created by resakemal on 26/11/19.
 */
data class RechargeGeneralDynamicField(

        @SerializedName("param_name")
        @Expose
        val paramName: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("style")
        @Expose
        var style: String = "",
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

    class DataCollection(
            @SerializedName("name")
            @Expose
            val name: String = "",
            @SerializedName("products")
            @Expose
            var products: List<CatalogProduct> = listOf())

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
}