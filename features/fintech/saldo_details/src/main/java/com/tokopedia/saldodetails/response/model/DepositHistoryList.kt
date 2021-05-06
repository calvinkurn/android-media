package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.viewmodel.ParcelableViewModel

class DepositHistoryList : ParcelableViewModel<SaldoDetailTransactionFactory> {

    @SerializedName("note")
    var note: String? = null

    @SerializedName("amount")
    var amount: Double = 0.0

    @SerializedName("image")
    var imageURL: String? = null

    @SerializedName("create_time")
    var createTime: String? = null

    @SerializedName("type_description")
    var typeDescription: String? = null

    override fun type(typeFactory: SaldoDetailTransactionFactory): Int {
        return typeFactory.type(this)
    }
}