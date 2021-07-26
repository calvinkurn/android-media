package com.tokopedia.hotel.screenshot.booking

/**
 * @author by astidhiyaa on 30/04/21
 */

class HotelBookingScreenshotLightTesting: BaseHotelBookingScreenshotTesting() {
    override fun forceDarkMode(): Boolean = false

    override fun filePrefix(): String = "hotel-booking-light"
}