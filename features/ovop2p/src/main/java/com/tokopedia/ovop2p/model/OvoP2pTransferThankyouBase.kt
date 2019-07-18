package com.tokopedia.ovop2p.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OvoP2pTransferThankyouBase() : Parcelable{
    @SerializedName("goalP2PThanks")
    lateinit var ovoP2pTransferThankyou: OvoP2pTransferThankyou

    constructor(parcel: Parcel) : this() {
        ovoP2pTransferThankyou = parcel.readParcelable(OvoP2pTransferThankyou::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(ovoP2pTransferThankyou, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OvoP2pTransferThankyouBase> {
        override fun createFromParcel(parcel: Parcel): OvoP2pTransferThankyouBase {
            return OvoP2pTransferThankyouBase(parcel)
        }

        override fun newArray(size: Int): Array<OvoP2pTransferThankyouBase?> {
            return arrayOfNulls(size)
        }
    }
}
