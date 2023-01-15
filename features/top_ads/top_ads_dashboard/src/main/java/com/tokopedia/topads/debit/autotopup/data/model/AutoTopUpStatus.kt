package com.tokopedia.topads.debit.autotopup.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AutoTopUpStatus(
        @SerializedName("status")
        @Expose
        val status: String = "",

        @SerializedName("status_desc")
        @Expose
        val statusDesc: String = "",

        @SerializedName("extra_credit_percent")
        @Expose
        val statusBonus :Double = 1.0,

        @SerializedName("tkpd_product_id")
        @Expose
        val id: Int = -1,

        @SerializedName("available_nominal")
        @Expose
        val availableNominals: MutableList<AutoTopUpItem> = mutableListOf(),

        @SerializedName("frequency")
        @Expose
        val frequency :Int = 6,
)
