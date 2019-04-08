package com.tokopedia.hotel.homepage.presentation.model

/**
 * @author by furqan on 05/04/19
 */
class HotelHomepageModel(var checkInDateString: String = "",
                         var checkOutDateString: String = "",
                         var nightCounter: Long = 0,
                         var roomCount: Int = 1,
                         var adultCount: Int = 1,
                         var childCount: Int = 0)