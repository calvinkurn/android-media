package com.tokopedia.topads.debit.autotopup.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AutoTopUpItem(
    @SerializedName("min_credit_fmt")
    @Expose
    val minCreditFmt: String = "",

    @SerializedName("price_fmt")
    @Expose
    val priceFmt: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    @Expose
    val price: Long = 0L,

    @SerializedName("additional_fee")
    @Expose
    val additionalFee: List<AdditionalFee>? = emptyList(),

    @SerializedName("total_amount")
    val totalAmount: Float = 0.0f,

    @SerializedName("tkpd_product_id")
    @Expose
    val id: Int = -1
) {

    fun getTopAdsCreditFmt(): String {
        return StringBuilder("Rp").append(priceFmt.replace("Rp ", "")).toString()
    }
    data class AdditionalFee(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("amount")
        val amount: Float = 0.0f,
        @SerializedName("percent")
        val percent: Float = 0.0f,
    )
}
