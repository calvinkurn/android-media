package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.saldodetails.commom.ParcelableViewModel
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter.SaldoDetailTransactionFactory

data class DepositHistoryList(
    @SerializedName("note")
    var note: String? = null,
    @SerializedName("amount")
    var amount: Double = 0.0,
    @SerializedName("create_time")
    var createTime: String,
    @SerializedName("type_description")
    var typeDescription: String? = null,
    @SerializedName("withdrawal_status_string")
    var withdrawalStatusString: String? = null,
    @SerializedName("withdrawal_status_color")
    var withdrawalStatusColor: Int,
    @SerializedName("have_detail")
    var haveDetail: Boolean,
    @SerializedName("deposit_id")
    var depositId: Long,
    @SerializedName("detail_type")
    var detailType: Int,
    @SerializedName("withdrawal_id")
    var withdrawalId: Long)
: ParcelableViewModel<SaldoDetailTransactionFactory> {
    override fun type(typeFactory: SaldoDetailTransactionFactory): Int {
        return typeFactory.type(this)
    }
}