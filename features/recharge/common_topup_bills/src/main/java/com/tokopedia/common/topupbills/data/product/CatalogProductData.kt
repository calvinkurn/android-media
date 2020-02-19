package com.tokopedia.common.topupbills.data.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
open class CatalogProductData(

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
                @SerializedName("products")
                @Expose
                var products: List<CatalogProduct> = listOf()
        )
}