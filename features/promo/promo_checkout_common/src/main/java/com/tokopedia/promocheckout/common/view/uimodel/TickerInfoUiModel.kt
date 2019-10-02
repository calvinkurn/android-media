package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-10-01.
 */

data class TickerInfoUiModel(
        var tickerMessage: String = "",
        var errorCode: String = "",
        var errorMessage: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tickerMessage)
        parcel.writeString(errorCode)
        parcel.writeString(errorMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TickerInfoUiModel> {
        override fun createFromParcel(parcel: Parcel): TickerInfoUiModel {
            return TickerInfoUiModel(parcel)
        }

        override fun newArray(size: Int): Array<TickerInfoUiModel?> {
            return arrayOfNulls(size)
        }
    }
}