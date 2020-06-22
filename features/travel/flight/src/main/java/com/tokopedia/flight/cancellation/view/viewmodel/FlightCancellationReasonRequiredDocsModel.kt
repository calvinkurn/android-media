package com.tokopedia.flight.cancellation.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by furqan on 15/06/2020
 */
data class FlightCancellationReasonRequiredDocsModel(val docId: Int,
                                                     val docTitle: String)
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(docId)
        parcel.writeString(docTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightCancellationReasonRequiredDocsModel> {
        override fun createFromParcel(parcel: Parcel): FlightCancellationReasonRequiredDocsModel {
            return FlightCancellationReasonRequiredDocsModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightCancellationReasonRequiredDocsModel?> {
            return arrayOfNulls(size)
        }
    }
}