package com.tokopedia.hotel.globalsearch.presentation.model

/**
 * @author by furqan on 21/11/2019
 */
class HotelGlobalSearchModel(
        var checkInDate: String = "",
        var checkInDateFmt: String = "",
        var checkOutDate: String = "",
        var checkOutDateFmt: String = "",
        var numOfGuests: Int = 0,
        var numOfRooms: Int = 0,
        var nightCount: Long = 0,
        var destinationId: Long = 0,
        var destinationType: String = "",
        var destinationName: String = "",
        var locationLong: Double = 0.0,
        var locationLat: Double = 0.0,
        var searchId: String = "",
        var searchType: String = "")