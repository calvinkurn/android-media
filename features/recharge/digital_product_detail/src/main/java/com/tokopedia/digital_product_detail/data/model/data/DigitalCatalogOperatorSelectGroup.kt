package com.tokopedia.digital_product_detail.data.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.product.CatalogOperator

data class DigitalCatalogOperatorSelectGroup(
    @SerializedName("rechargeCatalogOperatorSelectGroup")
    @Expose
    val response: DigitalOperatorCluster = DigitalOperatorCluster()
)

data class DigitalOperatorCluster(

    @SerializedName("text")
    @Expose
    val text: String = "",
    @SerializedName("style")
    @Expose
    val style: String = "",
    @SerializedName("help")
    @Expose
    val help: String = "",
    @SerializedName("operatorGroup")
    @Expose
    var operatorGroups: List<DigitalOperatorGroup>? = listOf(),
    @SerializedName("validations")
    @Expose
    var validations: List<Validation>? = listOf()

)

data class DigitalOperatorGroup(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("operators")
    @Expose
    val operators: List<CatalogOperator> = listOf()
)