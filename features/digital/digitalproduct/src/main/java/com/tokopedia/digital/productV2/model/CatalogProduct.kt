package com.tokopedia.digital.productV2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory

/**
 * Created by resakemal on 26/11/19.
 */
class CatalogProduct(

        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("dataCollections")
        @Expose
        var dataCollections: List<DataCollection> = listOf()

) {
        class DataCollection(
                @SerializedName("name")
                @Expose
                val name: String = "",
                @SerializedName("productData")
                @Expose
                var productData: List<CatalogProductData> = listOf()
        ): Visitable<VoucherGameDetailAdapterFactory> {
                override fun type(typeFactory: VoucherGameDetailAdapterFactory) = typeFactory.type(this)
        }
}