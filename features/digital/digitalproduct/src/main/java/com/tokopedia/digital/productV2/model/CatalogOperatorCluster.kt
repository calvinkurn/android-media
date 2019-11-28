package com.tokopedia.digital.productV2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapterFactory

/**
 * Created by resakemal on 12/08/19.
 */
class CatalogOperatorCluster(

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
        fun type(typeFactory: DigitalProductAdapterFactory): Int {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        class Response(
                @SerializedName("rechargeCatalogOperatorSelectGroup")
                @Expose
                val response: CatalogOperatorCluster = CatalogOperatorCluster()
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