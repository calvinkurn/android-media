package com.tokopedia.hotel.screenshot.roomlist

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelRoomListScreenshotDarkTesting : BaseHotelRoomListScreenshotTesting(){
    override fun filePrefix(): String = "hotel-room-list-dark"

    override fun forceDarkMode(): Boolean = true
}