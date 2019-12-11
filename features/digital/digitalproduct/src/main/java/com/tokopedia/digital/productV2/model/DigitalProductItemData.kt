package com.tokopedia.digital.productV2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapterFactory

/**
 * Created by resakemal on 26/11/19.
 */
class DigitalProductItemData(

        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("dataCollections")
        @Expose
        var dataCollections: List<CatalogProductData.DataCollection> = listOf(),
        var value: String = ""

): Visitable<DigitalProductAdapterFactory> {
        override fun type(typeFactory: DigitalProductAdapterFactory) = typeFactory.type(this)
}