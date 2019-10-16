package com.tokopedia.transactiondata.insurance.entity.response

import com.google.gson.annotations.SerializedName

data class UpdateInsuranceDataGqlResponse(
        @SerializedName("update_cart_transactional")
        var data: UpdateCartTransactional
)

data class UpdateCartTransactional(
        @SerializedName("update_transactional")
        var updateTransactional: InsuranceUpdateTransactional,

        @SerializedName("update_cart")
        var updateCart: InsuranceUpdateCart
)

data class InsuranceUpdateTransactional(
        @SerializedName("status")
        var status: Boolean,

        @SerializedName("error_message")
        var errorMessage: String
)

data class InsuranceUpdateCart(
        @SerializedName("status")
        var status: String
)