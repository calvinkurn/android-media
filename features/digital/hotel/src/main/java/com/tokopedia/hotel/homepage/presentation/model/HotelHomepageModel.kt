package com.tokopedia.hotel.homepage.presentation.model

/**
 * @author by furqan on 05/04/19
 */
class HotelHomepageModel(var checkInDate: String = "",
                         var checkInDateFmt: String = "",
                         var checkOutDate: String = "",
                         var checkOutDateFmt: String = "",
                         var nightCounter: Long = 0,
                         var roomCount: Int = 1,
                         var adultCount: Int = 1,
                         var childCount: Int = 0,
                         var locLat: Double = 0.0,
                         var locLong: Double = 0.0,
                         var locName: String = "Bali",
                         var locId: Int = 0,
                         var locType: String = "")