package com.tokopedia.recommendation_widget_common.widget.carousel.global

object RecommendationCarouselTrackingConst {

    object Category {
        const val INBOX_PAGE = "inbox page"
        const val PDP = "product detail page"
    }

    object Action {
        const val IMPRESSION_ON_PRODUCT_RECOMMENDATION_INBOX = "impression on product recommendation inbox"
        const val CLICK_ON_PRODUCT_RECOMMENDATION_INBOX = "click on product recommendation inbox"
        const val IMPRESSION_ON_PRODUCT_RECOMMENDATION_PDP = "impression on product recommendation product detail"
        const val CLICK_ON_PRODUCT_RECOMMENDATION_PDP = "click on product recommendation product detail"
        const val IMPRESSION_PDP_RECOM_WITH_ATC = "impression pdp recom with atc"
        const val CLICK_PDP_RECOM_WITH_ATC = "click pdp recom with atc"
        const val ATC_PDP_RECOM_WITH_ATC = "atc pdp recom with atc"
        const val ADJUST_QTY_PDP_RECOM_WITH_ATC = "click pdp recom with atc - adjust quantity"
        const val DELETE_PDP_RECOM_WITH_ATC = "click pdp recom with atc - delete"
        const val CLICK_PDP_RECOM_SEE_ALL = "click pdp recom with atc - lihat semua"
    }

    object List {
        const val REKOMENDASI_UNTUK_ANDA = "rekomendasi untuk anda"
        const val INBOX = "inbox"
        const val PDP = "product detail"
        const val PRODUCT = "product"
    }

    object TrackerId {
        const val IMPRESSION_RECOMMENDATION_ITEM_PDP_ATC = "43018"
        const val CLICK_RECOMMENDATION_ITEM_PDP_ATC = "43019"
        const val ATC_RECOMMENDATION_ITEM_PDP_ATC = "43020"
        const val SEE_ALL_RECOMMENDATION_PDP_ATC = "43021"
        const val DELETE_RECOMMENDATION_ITEM_PDP_ATC = "43022"
        const val ADJUST_QTY_ITEM_PDP_ATC = "43024"
    }
}
