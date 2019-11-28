package com.tokopedia.digital.productV2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.product.CatalogOperator

/**
 * Created by resakemal on 28/11/19.
 */
class DigitalProductOperatorCluster(

        @SerializedName("componentID")
        @Expose
        val componentID: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("paramName")
        @Expose
        val paramName: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("style")
        @Expose
        val style: String = "",
        @SerializedName("operatorGroup")
        @Expose
        var operators: List<CatalogOperatorGroup> = listOf()

) {

        class Response(
                @SerializedName("rechargeCatalogOperatorSelectGroup")
                @Expose
                val response: DigitalProductOperatorCluster = DigitalProductOperatorCluster()
        )

        class CatalogOperatorGroup(
                @SerializedName("name")
                @Expose
                val name: String = "",
                @SerializedName("operators")
                @Expose
                val operators: List<CatalogOperator> = listOf()
        )
}