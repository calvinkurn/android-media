package com.tokopedia.vouchergame.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory

/**
 * Created by resakemal on 13/08/19.
 */
class VoucherGameProductData(

        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("dataCollections")
        @Expose
        val dataCollections: List<DataCollection> = listOf()

) {
        class DataCollection(
                @SerializedName("name")
                @Expose
                val name: String = "",
                @SerializedName("products")
                @Expose
                val products: List<VoucherGameProduct> = listOf()
        ): Visitable<VoucherGameDetailAdapterFactory> {
                override fun type(typeFactory: VoucherGameDetailAdapterFactory) = typeFactory.type(this)
        }
}