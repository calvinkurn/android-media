package com.tokopedia.common.topupbills.data.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
open class CatalogProductData(

        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("text")
        @Expose
        var text: String = "",
        @SerializedName("dataCollections")
        @Expose
        var dataCollections: List<DataCollection> = listOf()

) {
        //data collection product
        class DataCollection(
                @SerializedName("name")
                @Expose
                var name: String = "",
                @SerializedName("value")
                @Expose
                var value: String = "",
                @SerializedName("cluster_type")
                @Expose
                var clusterType: String = "",
                @SerializedName("products")
                @Expose
                var products: List<CatalogProduct> = listOf()
        )
}