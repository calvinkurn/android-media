package com.tokopedia.hotel.screenshot.srp_map

/**
 * @author by astidhiyaa on 07/05/21
 */
class HotelSearchMapDarkScreenshotTesting: BaseHotelSearchMapScreenshotTesting() {
    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "hotel-srp-map-dark"
}