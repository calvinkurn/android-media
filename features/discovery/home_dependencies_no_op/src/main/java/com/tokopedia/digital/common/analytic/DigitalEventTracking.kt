package com.tokopedia.digital.common.analytic

interface DigitalEventTracking {

    interface Event {
        companion object {
            const val HOMEPAGE_INTERACTION = "userInteractionHomePage"
        }
    }

    interface Category {
        companion object {
            const val DIGITAL_HOMEPAGE = "homepage digital"
        }
    }

    interface Action {
        companion object {
            const val CLICK_SEE_ALL_PRODUCTS = "click lihat semua produk"
        }
    }
}
