package com.tokopedia.hotel.screenshot.srp

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelSearchResultScreenshotDarkTesting: BaseHotelSearchResultScreenshotTesting() {
    override fun filePrefix(): String = "hotel-old-srp-dark"

    override fun forceDarkMode(): Boolean = true
}