package com.tokopedia.vouchergame.list.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
        )
}