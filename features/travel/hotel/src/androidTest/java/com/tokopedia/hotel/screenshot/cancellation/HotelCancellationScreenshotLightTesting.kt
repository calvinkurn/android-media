package com.tokopedia.hotel.screenshot.cancellation

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelCancellationScreenshotLightTesting: BaseHotelCancellationScreenshotTesting() {
    override fun forceDarkMode(): Boolean = false

    override fun filePrefix(): String = "hotel-cancellation-light"
}