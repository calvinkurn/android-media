package com.tokopedia.hotel.screenshot.evoucher

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelEVoucherScreenshotLightTesting : BaseHotelEVoucherScreenshotTesting(){
    override fun forceDarkMode(): Boolean = false

    override fun filePrefix(): String = "hotel-evoucher-light"
}