package com.tokopedia.digital.productV2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapterFactory

/**
 * Created by resakemal on 26/11/19.
 */
class DigitalProductInput (

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
        val dataCollections: List<CatalogProductInput.DataCollection> = listOf(),
        @SerializedName("validations")
        @Expose
        val validations: List<CatalogProductInput.Validation> = listOf(),
        var value: String = ""

): Visitable<DigitalProductAdapterFactory> {
        override fun type(typeFactory: DigitalProductAdapterFactory) = typeFactory.type(this)
}