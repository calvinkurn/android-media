package com.tokopedia.hotel.screenshot.srp_map

/**
 * @author by astidhiyaa on 18/05/21
 */
class HotelSearchMapLightScreenShotTesting: BaseHotelSearchMapScreenshotTesting() {
    override fun forceDarkMode(): Boolean = false

    override fun filePrefix(): String = "hotel-srp-map-light"
}