package com.tokopedia.discovery.catalogrevamp.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class CatalogDetailPageAnalytics {

    companion object {
        private const val EVENT_CATALOG = "clickCatalog"
        private const val CATALOG_PAGE = "catalog page"
        private const val CLICK_SPECIFICATIONS = "click spesifikasi"
        private const val CLICK_SOCIAL_SHARE = "click social share"
        private const val CLICK_INDEX_PICTURE = "click index picture"
        private const val SWIPE_INDEX_PICTURE = "swipe index picture"
        private const val CLICK_CATALOG_PICTURE = "click catalog picture"
        private const val SWIPE_CATALOG_PICTURE = "swipe catalog picture"

        @JvmStatic
        fun trackEventClickSpecification() {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_SPECIFICATIONS,
                    ""
            ))
        }

        @JvmStatic
        fun trackEventClickSocialShare() {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_SOCIAL_SHARE,
                    ""
            ))
        }

        @JvmStatic
        fun trackEventClickIndexPicture(position: Int) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_INDEX_PICTURE,
                    position.toString()
            ))
        }

        @JvmStatic
        fun trackEventSwipeIndexPicture(direction: String) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    SWIPE_INDEX_PICTURE,
                    direction
            ))
        }

        @JvmStatic
        fun trackEventClickCatalogPicture() {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_CATALOG_PICTURE,
                    ""
            ))
        }

        @JvmStatic
        fun trackEventSwipeCatalogPicture(direction: String) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    SWIPE_CATALOG_PICTURE,
                    direction
            ))
        }
    }
}