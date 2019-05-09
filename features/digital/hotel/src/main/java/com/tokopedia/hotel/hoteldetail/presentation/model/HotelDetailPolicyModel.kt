package com.tokopedia.hotel.hoteldetail.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyPolicyData

/**
 * @author by furqan on 08/05/19
 */
class HotelDetailPolicyModel (
        var checkInFrom: String = "",
        var checkInTo: String = "",
        var checkOutFrom: String = "",
        var checkOutTo: String = "",
        var propertyPolicy: List<PropertyPolicyData> = listOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(PropertyPolicyData)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(checkInFrom)
        parcel.writeString(checkInTo)
        parcel.writeString(checkOutFrom)
        parcel.writeString(checkOutTo)
        parcel.writeTypedList(propertyPolicy)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotelDetailPolicyModel> {
        override fun createFromParcel(parcel: Parcel): HotelDetailPolicyModel {
            return HotelDetailPolicyModel(parcel)
        }

        override fun newArray(size: Int): Array<HotelDetailPolicyModel?> {
            return arrayOfNulls(size)
        }
    }
}