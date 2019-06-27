package com.tokopedia.product.detail.data.model.installment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InstallmentBank(
        @SerializedName("bankID")
        @Expose
        val id: Int = 0,

        @SerializedName("bankCode")
        @Expose
        val code: String = "",

        @SerializedName("bankName")
        @Expose
        val name: String = "",

        @SerializedName("bankIcon")
        @Expose
        val icon: String = "",

        @SerializedName("installmentList")
        @Expose
        val installmentList: List<Installment> = listOf()
) {

    data class Installment(
            @SerializedName("term")
            @Expose
            val term: Int = 1,

            @SerializedName("feeValue")
            @Expose
            val feeValue: Float = 0f,

            @SerializedName("feeType")
            @Expose
            val feeType: String = "",

            @SerializedName("interest")
            @Expose
            val interest: Float = 0f,

            @SerializedName("minimumAmount")
            @Expose
            val minAmount: Float = 0f,

            @SerializedName("monthlyPrice")
            @Expose
            val monthlyPrice: Float = 0f,

            @SerializedName("osMonthlyPrice")
            @Expose
            val osMonthlyPrice: Float = 0f
    )
}