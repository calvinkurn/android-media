package com.tokopedia.salam.umrah.common.analytics

import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_BACA_ITINERARY_SELENGKAPNYA
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_BELI
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_FOTO_HOTEL
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_FOTO_PRODUK
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_LIHAT_SEMUA_FAQ
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_LIHAT_SEMUA_FASILITAS
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_PLIH_KAMAR
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.SCROLL_HOTEL
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.SCROLL_PENERBANGAN

enum class UmrahPdpTrackingUserAction {
    CLICK_PACKAGE_PHOTOS {
        override fun getEventAction() = CLICK_FOTO_PRODUK
    },
    CHOOSE_ROOM_TYPE_UP {
        override fun getEventAction(): String = CLICK_PLIH_KAMAR
    },
    CHOOSE_ROOM_TYPE_DOWN{
        override fun getEventAction(): String = CLICK_BELI
    },
    SCROLLING_HOTEL {
        override fun getEventAction(): String = SCROLL_HOTEL
    },
    CLICK_HOTEL_PHOTOS{
        override fun getEventAction(): String = CLICK_FOTO_HOTEL
    },
    SCROLLING_PENERBANGAN{
        override fun getEventAction(): String = SCROLL_PENERBANGAN
    },
    LIHAT_ITINERARY_SELENGKAPNYA{
        override fun getEventAction(): String = CLICK_BACA_ITINERARY_SELENGKAPNYA
    },
    LIHAT_SEMUA_FASILITAS{
        override fun getEventAction(): String = CLICK_LIHAT_SEMUA_FASILITAS
    },
    SCROLL_FAQ{
        override fun getEventAction(): String = UmrahTrackingConstant.SCROLL_FAQ
    },
    LIHAT_SEMUA_FAQ{
        override fun getEventAction(): String = CLICK_LIHAT_SEMUA_FAQ
    },
    CLICK_FAQ{
        override fun getEventAction(): String = UmrahTrackingConstant.CLICK_FAQ
    },
    CLICK_BACK {
        override fun getEventAction(): String = UmrahTrackingConstant.CLICK_BACK
    };

    abstract fun getEventAction(): String
}