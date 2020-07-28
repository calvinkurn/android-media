package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.rechargegeneral.presentation.adapter.RechargeGeneralAdapterFactory

/**
 * Created by resakemal on 26/11/19.
 */
open class RechargeGeneralProductInput (

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
        var dataCollections: List<CatalogProductInput.DataCollection> = listOf(),
        @SerializedName("validations")
        @Expose
        var validations: List<CatalogProductInput.Validation> = listOf(),
        var value: String = "",
        var isFavoriteNumber: Boolean = false

) : Visitable<RechargeGeneralAdapterFactory> {
        override fun type(typeFactory: RechargeGeneralAdapterFactory) = typeFactory.type(this)
}