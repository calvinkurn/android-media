package com.tokopedia.ovop2p.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OvoP2pTransferSource() : Parcelable{
    @SerializedName("name")
    var name: String = ""
    @SerializedName("phone")
    var phone: String = ""

        constructor(parcel: Parcel) : this() {
                name = parcel.readString()
                phone = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(name)
                parcel.writeString(phone)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<OvoP2pTransferSource> {
                override fun createFromParcel(parcel: Parcel): OvoP2pTransferSource {
                        return OvoP2pTransferSource(parcel)
                }

                override fun newArray(size: Int): Array<OvoP2pTransferSource?> {
                        return arrayOfNulls(size)
                }
        }
}
