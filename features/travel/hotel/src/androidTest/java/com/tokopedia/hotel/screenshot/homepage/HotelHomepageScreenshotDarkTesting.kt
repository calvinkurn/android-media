package com.tokopedia.hotel.screenshot.homepage

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelHomepageScreenshotDarkTesting : BaseHotelHomepageScreenshotTesting(){
    override fun filePrefix(): String = "hotel-homepage-dark"

    override fun forceDarkMode(): Boolean = true
}