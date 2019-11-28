package com.tokopedia.common.topupbills.data.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
open class CatalogProduct(

        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("dataCollections")
        @Expose
        open val dataCollections: List<DataCollection> = listOf()

) {
        open class DataCollection(
                @SerializedName("name")
                @Expose
                val name: String = "",
                @SerializedName("products")
                @Expose
                open val products: List<CatalogProductData> = listOf()
        )
}