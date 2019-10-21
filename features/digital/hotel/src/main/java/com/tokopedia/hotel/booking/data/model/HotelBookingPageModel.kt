package com.tokopedia.hotel.booking.data.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.common.travel.presentation.model.TravelContactData

/**
 * @author by resakemal on 28/05/19
 */

data class HotelBookingPageModel(
        var cartId: String = "",
        var roomRequest: String = "",
        var contactData: TravelContactData = TravelContactData(),
        var guestName: String = "",
        var promoCode: String = "",
        var isForOtherGuest: Int = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(TravelContactData::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cartId)
        parcel.writeString(roomRequest)
        parcel.writeParcelable(contactData, flags)
        parcel.writeString(guestName)
        parcel.writeString(promoCode)
        parcel.writeInt(isForOtherGuest)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotelBookingPageModel> {
        override fun createFromParcel(parcel: Parcel): HotelBookingPageModel {
            return HotelBookingPageModel(parcel)
        }

        override fun newArray(size: Int): Array<HotelBookingPageModel?> {
            return arrayOfNulls(size)
        }
    }

}