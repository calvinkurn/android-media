package com.tokopedia.transactiondata.insurance.entity.response

import com.google.gson.annotations.SerializedName

data class AddInsuranceProductToCartGqlResponse(
        @SerializedName("add_to_cart_transactional")
        var addToCartTransactional: AddToCartTransactional
)

data class AddToCartTransactional(
        @SerializedName("add_transactional")
        var addTransactional: AddInsuranceProductTransactional,

        @SerializedName("add_cart")
        var addCart: AddInsuranceProductCart
)

data class AddInsuranceProductTransactional(
        @SerializedName("status")
        var status: Boolean,

        @SerializedName("error_message")
        var errorMessage: String
)

data class AddInsuranceProductCart(
        @SerializedName("status")
        var status: String,

        @SerializedName("error_message")
        var errorMessage: ArrayList<String>,

        @SerializedName("data")
        var successData: AddInsuranceProductCartSuccessData
)

data class AddInsuranceProductCartSuccessData(
        @SerializedName("message")
        var message: ArrayList<String>,

        @SerializedName("cart_id")
        var cartId: Long
)