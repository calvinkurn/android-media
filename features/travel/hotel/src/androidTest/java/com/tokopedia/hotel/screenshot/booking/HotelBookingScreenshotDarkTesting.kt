package com.tokopedia.hotel.screenshot.booking

/**
 * @author by astidhiyaa on 30/04/21
 */

class HotelBookingScreenshotDarkTesting: BaseHotelBookingScreenshotTesting() {
    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "hotel-booking-dark"
}