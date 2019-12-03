package com.tokopedia.salam.umrah.common.analytics


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
        override fun getEventAction(): String = SCROLL_FAQ_CONST
    },
    LIHAT_SEMUA_FAQ{
        override fun getEventAction(): String = CLICK_LIHAT_SEMUA_FAQ
    },
    CLICK_FAQ{
        override fun getEventAction(): String = CLICK_FAQ_CONST
    },
    CLICK_BACK {
        override fun getEventAction(): String = CLICK_BACK_CONST
    };

    abstract fun getEventAction(): String
}