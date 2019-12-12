package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.rechargegeneral.presentation.adapter.RechargeGeneralAdapterFactory

/**
 * Created by resakemal on 26/11/19.
 */
class RechargeGeneralItemData(

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

): Visitable<RechargeGeneralAdapterFactory> {
        override fun type(typeFactory: RechargeGeneralAdapterFactory) = typeFactory.type(this)
}