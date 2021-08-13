package com.tokopedia.hotel.screenshot.roomlist

/**
 * @author by astidhiyaa on 30/04/21
 */
class HotelRoomListScreenshotLightTesting : BaseHotelRoomListScreenshotTesting(){
    override fun filePrefix(): String = "hotel-room-list-light"

    override fun forceDarkMode(): Boolean = false
}