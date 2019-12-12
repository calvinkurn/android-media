package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.product.CatalogOperator

/**
 * Created by resakemal on 28/11/19.
 */
class RechargeGeneralOperatorCluster(

        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("style")
        @Expose
        val style: String = "",
        @SerializedName("operatorGroup")
        @Expose
        var operatorGroups: List<CatalogOperatorGroup> = listOf()

) {

        class Response(
                @SerializedName("rechargeCatalogOperatorSelectGroup")
                @Expose
                val response: RechargeGeneralOperatorCluster = RechargeGeneralOperatorCluster()
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