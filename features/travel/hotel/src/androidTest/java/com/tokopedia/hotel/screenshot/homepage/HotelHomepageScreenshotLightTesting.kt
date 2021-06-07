package com.tokopedia.hotel.screenshot.homepage

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelHomepageScreenshotLightTesting : BaseHotelHomepageScreenshotTesting(){
    override fun filePrefix(): String = "hotel-homepage-light"

    override fun forceDarkMode(): Boolean = false
}