package com.tokopedia.product.detail.data.model.installment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InstallmentResponse(
        @SerializedName("installmentCalculation")
        @Expose
        val result: Result? = null
){

    data class Result(
            @SerializedName("response")
            @Expose
            val response: String = "",

            @SerializedName("message")
            @Expose
            val message: String = "",

            @SerializedName("bank")
            @Expose
            val bank: List<InstallmentBank> = listOf()
    )
}