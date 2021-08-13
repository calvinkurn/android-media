package com.tokopedia.hotel.screenshot.hoteldetail

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelDetailActivityScreenshotDarkTesting: BaseHotelDetailActivityScreenshotTesting() {
    override fun filePrefix(): String = "hotel-detail-dark"

    override fun forceDarkMode(): Boolean = true
}