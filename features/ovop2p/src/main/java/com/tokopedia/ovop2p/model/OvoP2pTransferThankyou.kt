package com.tokopedia.ovop2p.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OvoP2pTransferThankyou() : Parcelable{
    @SerializedName("transfer_id")
    var trnsfrId: Int = 0
    @SerializedName("transaction_id")
    var txnId: Int = 0
    @SerializedName("status")
    var status: String = ""
    @SerializedName("amount")
    var amt: Int = 0
    @SerializedName("source_of_fund")
    var srcFnd: String = ""
    @SerializedName("transfer_date")
    var trnsfrDate: String = ""
    @SerializedName("source")
    lateinit var source: OvoP2pTransferSource
    @SerializedName("message")
    var msg: String = ""
    @SerializedName("reference_number")
    var refNum: Int = 0
    @SerializedName("errors")
    var errors: List<Map<String, String>>? = null

    constructor(parcel: Parcel) : this() {
        trnsfrId = parcel.readInt()
        txnId = parcel.readInt()
        status = parcel.readString()
        amt = parcel.readInt()
        srcFnd = parcel.readString()
        trnsfrDate = parcel.readString()
        source = parcel.readParcelable(OvoP2pTransferSource::class.java.classLoader)
        msg = parcel.readString()
        refNum = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(trnsfrId)
        parcel.writeInt(txnId)
        parcel.writeString(status)
        parcel.writeInt(amt)
        parcel.writeString(srcFnd)
        parcel.writeString(trnsfrDate)
        parcel.writeParcelable(source, flags)
        parcel.writeString(msg)
        parcel.writeInt(refNum)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OvoP2pTransferThankyou> {
        override fun createFromParcel(parcel: Parcel): OvoP2pTransferThankyou {
            return OvoP2pTransferThankyou(parcel)
        }

        override fun newArray(size: Int): Array<OvoP2pTransferThankyou?> {
            return arrayOfNulls(size)
        }
    }
}
