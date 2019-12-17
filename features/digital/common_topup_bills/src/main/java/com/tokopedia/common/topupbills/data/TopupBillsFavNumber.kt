package com.tokopedia.common.topupbills.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TopupBillsFavNumber(
        @SerializedName("list")
        @Expose
        val favNumberList: List<TopupBillsFavNumberItem>) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(TopupBillsFavNumberItem))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(favNumberList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopupBillsFavNumber> {
        override fun createFromParcel(parcel: Parcel): TopupBillsFavNumber {
            return TopupBillsFavNumber(parcel)
        }

        override fun newArray(size: Int): Array<TopupBillsFavNumber?> {
            return arrayOfNulls(size)
        }
    }
}