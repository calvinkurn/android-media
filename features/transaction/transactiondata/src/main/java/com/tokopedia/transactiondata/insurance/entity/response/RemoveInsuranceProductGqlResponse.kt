package com.tokopedia.transactiondata.insurance.entity.response

import com.google.gson.annotations.SerializedName

data class RemoveInsuranceProductGqlResponse(
        @SerializedName("remove_from_cart_transactional")
        var response: RemoveFromCart
)

data class RemoveFromCart(
        @SerializedName("remove_transactional")
        val removeTransactional: RemoveTransactional,

        @SerializedName("remove_cart")
        var removeCart: RemoveCart
)

data class RemoveTransactional(
        @SerializedName("status")
        var status: Boolean,

        @SerializedName("error_message")
        val errorMessage: String
)

data class RemoveCart(
        @SerializedName("error_message")
        var errorMessage: String,

        @SerializedName("status")
        val status: String
)