package com.tokopedia.ovop2p.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OvoP2pTransferThankyou(@SerializedName("transfer_id")
                             var trnsfrId: Int = 0,
                             @SerializedName("transaction_id")
                             var txnId: Int = 0,
                             @SerializedName("status")
                             var status: String = "",
                             @SerializedName("amount")
                             var amt: Int = 0,
                             @SerializedName("source_of_fund")
                             var srcFnd: String = "",
                             @SerializedName("transfer_date")
                             var trnsfrDate: String = "",
                             @SerializedName("source")
                             var source: OvoP2pTransferSource = OvoP2pTransferSource(),
                             @SerializedName("destination")
                             var destination: OvoP2pTransferSource = OvoP2pTransferSource(),
                             @SerializedName("message")
                             var msg: String = "",
                             @SerializedName("reference_number")
                             var refNum: Int = 0,
                             @SerializedName("errors")
                             var errors: List<Map<String, String>>? = null) : Parcelable
