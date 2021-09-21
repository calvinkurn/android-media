package com.tokopedia.hotel.screenshot.hoteldetail

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelDetailActivityScreenshotLightTesting: BaseHotelDetailActivityScreenshotTesting() {
    override fun filePrefix(): String = "hotel-detail-light"

    override fun forceDarkMode(): Boolean = false
}