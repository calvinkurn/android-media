package com.tokopedia.hotel.booking.data.model

import android.os.Parcelable
import com.tokopedia.travel.passenger.presentation.model.TravelContactData
import kotlinx.android.parcel.Parcelize

/**
 * @author by resakemal on 28/05/19
 */
@Parcelize
data class HotelBookingPageModel(
        var cartId: String = "",
        var roomRequest: String = "",
        var contactData: TravelContactData = TravelContactData(),
        var guestName: String = "",
        var promoCode: String = "",
        var isForOtherGuest: Int = 0
) : Parcelable