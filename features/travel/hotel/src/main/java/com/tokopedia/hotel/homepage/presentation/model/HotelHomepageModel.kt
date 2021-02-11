package com.tokopedia.hotel.homepage.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 05/04/19
 */
@Parcelize
class HotelHomepageModel(var checkInDate: String = "",
                         var checkInDateFmt: String = "",
                         var checkOutDate: String = "",
                         var checkOutDateFmt: String = "",
                         var nightCounter: Long = 0,
                         var roomCount: Int = 1,
                         var adultCount: Int = 1,
                         var locLat: Double = 0.0,
                         var locLong: Double = 0.0,
                         var locName: String = "",
                         var locId: Long = 0,
                         var locType: String = "",
                         var searchType: String = "",
                         var searchId: String = "") : Parcelable