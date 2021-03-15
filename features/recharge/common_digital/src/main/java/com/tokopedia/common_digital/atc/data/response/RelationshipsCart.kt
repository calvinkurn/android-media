package com.tokopedia.common_digital.atc.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RelationshipsCart(
        @SerializedName("category")
        @Expose
        var category: Category? = null,

        @SerializedName("operator")
        @Expose
        var operator: Operator? = null,

        @SerializedName("product")
        @Expose
        var product: Product? = null
) {
    data class Category(
            @SerializedName("data")
            @Expose
            var data: RelationData? = null
    )

    data class Product(
            @SerializedName("data")
            @Expose
            var data: RelationData? = null
    )

    data class Operator(
            @SerializedName("data")
            @Expose
            var data: RelationData? = null
    )

    data class RelationData (
            @SerializedName("type")
            @Expose
            var type: String? = null,

            @SerializedName("id")
            @Expose
            var id: String? = null
    )

}