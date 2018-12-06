package com.tokopedia.instantloan.common.analytics

class InstantLoanEventConstants {

    interface Screen {
        companion object {
            val DANA_INSTAN_SCREEN_NAME = "dana instan"
            val TANPA_AGUNAN_SCREEN_NAME = "tanpa agunan"
            val AGUNAN_SCREEN_NAME = "agunan"
        }
    }

    internal interface Event {
        companion object {
            val EVENT_CLICK_FINTECH_MICROSITE = "clickFintechMicrosite"
        }
    }

    internal interface Category {
        companion object {
            val FINTECH_HOMEPAGE = "fin - android homepage"
        }
    }

    internal interface Action {
        companion object {
            val PL_BANNER_IMPRESSION = "pl - banner impressions - {partner name}"
            val PL_BANNER_CLICK = "pl - banner impressions click - {partner name}"
            val PL_CARI_PINJAMAN_CLICK = "pl - cari pinjaman click"
            val PL_POP_UP_CLICK = "pl - dana instan popup click"
        }
    }

    interface EventLabel {
        companion object {
            val PL_POPUP_LEARN_MORE = "pelajari selengkapnya"
            val PL_POPUP_TNC = "third - syarat dan ketentuan"
            val PL_POPUP_CONNECT_DEVICE = "third - hubungkan perangkat"
            val PL_INTRO_SLIDER_FIRST_NEXT = "first - next"
            val PL_INTRO_SLIDER_SECOND_NEXT = "second - next"
            val PL_INTRO_SLIDER_SECOND_PREVIOUS = "second - previous"
            val PL_INTRO_SLIDER_THIRD_PREVIOUS = "third - previous"
            val PL_PERMISSION_DENIED = "forth - permission denied - "
        }
    }

}
