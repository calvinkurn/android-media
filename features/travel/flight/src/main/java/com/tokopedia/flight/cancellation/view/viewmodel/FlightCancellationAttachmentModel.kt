package com.tokopedia.flight.cancellation.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentTypeFactory

/**
 * Created by alvarisi on 3/26/18.
 */
data class FlightCancellationAttachmentModel(var filename: String = "",
                                             var filepath: String = "",
                                             var passengerId: String = "",
                                             var passengerName: String = "",
                                             var relationId: String = "",
                                             var journeyId: String = "",
                                             var docType: Int = 0,
                                             var percentageUpload: Long = 0,
                                             var isUploaded: Boolean = false)
    : Visitable<FlightCancellationAttachmentTypeFactory?>, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun type(typeFactory: FlightCancellationAttachmentTypeFactory?): Int =
            typeFactory?.type(this) ?: 0

    override fun equals(obj: Any?): Boolean {
        var isEqual = false

        if (obj is FlightCancellationAttachmentModel) {
            isEqual = this.passengerId == obj.passengerId &&
                    this.passengerName == obj.passengerName
        }

        return isEqual
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(filename)
        parcel.writeString(filepath)
        parcel.writeString(passengerId)
        parcel.writeString(passengerName)
        parcel.writeString(relationId)
        parcel.writeString(journeyId)
        parcel.writeInt(docType)
        parcel.writeLong(percentageUpload)
        parcel.writeByte(if (isUploaded) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightCancellationAttachmentModel> {
        override fun createFromParcel(parcel: Parcel): FlightCancellationAttachmentModel {
            return FlightCancellationAttachmentModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightCancellationAttachmentModel?> {
            return arrayOfNulls(size)
        }
    }

}