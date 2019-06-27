package com.tokopedia.topads.credit.history.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.credit.history.view.adapter.TopAdsCreditHistoryTypeFactory

data class CreditHistory(
        @SerializedName("amount")
        @Expose
        val amount: Float = 0f,

        @SerializedName("amount_fmt")
        @Expose
        val amountFmt: String = "",

        @SerializedName("date")
        @Expose
        val date: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("is_reduction")
        @Expose
        val isReduction: Boolean = false,

        @SerializedName("show_timestamp")
        @Expose
        val showTimestamp: Boolean = false,

        @SerializedName("transaction_type")
        @Expose
        val transactionType: Int = 0
): Visitable<TopAdsCreditHistoryTypeFactory>{

    override fun type(typeFactory: TopAdsCreditHistoryTypeFactory) = typeFactory.type(this)

}