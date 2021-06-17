package com.tokopedia.hotel.screenshot.srp

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelSearchResultScreenshotLightTesting : BaseHotelSearchResultScreenshotTesting(){
    override fun filePrefix(): String = "hotel-old-srp-light"

    override fun forceDarkMode(): Boolean = false
}