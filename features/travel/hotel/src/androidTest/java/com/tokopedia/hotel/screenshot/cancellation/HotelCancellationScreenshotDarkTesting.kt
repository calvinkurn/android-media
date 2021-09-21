package com.tokopedia.hotel.screenshot.cancellation

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelCancellationScreenshotDarkTesting: BaseHotelCancellationScreenshotTesting() {
    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "hotel-cancellation-dark"
}