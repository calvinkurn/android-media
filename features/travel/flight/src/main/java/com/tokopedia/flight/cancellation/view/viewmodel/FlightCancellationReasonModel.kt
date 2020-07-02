package com.tokopedia.flight.cancellation.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationReasonAdapterTypeFactory

/**
 * @author by furqan on 15/06/2020
 */
data class FlightCancellationReasonModel(val id: String,
                                         val detail: String,
                                         val requiredDocs: List<FlightCancellationReasonRequiredDocsModel>?)
    : Parcelable, Visitable<FlightCancellationReasonAdapterTypeFactory> {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(FlightCancellationReasonRequiredDocsModel)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(detail)
        parcel.writeTypedList(requiredDocs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightCancellationReasonModel> {
        override fun createFromParcel(parcel: Parcel): FlightCancellationReasonModel {
            return FlightCancellationReasonModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightCancellationReasonModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun type(typeFactory: FlightCancellationReasonAdapterTypeFactory): Int =
            typeFactory.type(this)

}