package com.tokopedia.hotel.screenshot.evoucher

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelEVoucherScreenshotDarkTesting : BaseHotelEVoucherScreenshotTesting(){
    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "hotel-evoucher-dark"
}