package com.tokopedia.instantloan.data.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GqlLendingPartnerData(
        @SerializedName("ID")
        var partnerId: Int,

        @SerializedName("Name")
        var partnerName: String? = "",

        @SerializedName("NameSlug")
        var partnerNameSlug: String? = "",

        @SerializedName("Logo")
        var partnerIconUrl: String? = ""
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(partnerId)
                parcel.writeString(partnerName)
                parcel.writeString(partnerNameSlug)
                parcel.writeString(partnerIconUrl)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<GqlLendingPartnerData> {
                override fun createFromParcel(parcel: Parcel): GqlLendingPartnerData {
                        return GqlLendingPartnerData(parcel)
                }

                override fun newArray(size: Int): Array<GqlLendingPartnerData?> {
                        return arrayOfNulls(size)
                }
        }


}