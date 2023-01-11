package com.tokopedia.feedplus.view.constants

/**
 * @author by yfsx on 13/11/18.
 */


interface Constants {

    object FeedConstants {
        const val KEY_FEED = "KEY_FEED"
        const val KEY_FEED_FIRST_PAGE_LAST_CURSOR = "KEY_FEED_FIRSTPAGE_LAST_CURSOR"
        const val KEY_FEED_FIRST_PAGE_CURSOR = "KEY_FEED_FIRSTPAGE_CURSOR"
    }

    object FeedDetailConstants {

        object Event {
            const val CATEGORY_PAGE = "clickKategori"
            const val CLICK_APP_SHARE_WHEN_REFERRAL_OFF = "clickAppShare"
            const val CLICK_APP_SHARE_REFERRAL = "clickReferral"
            const val PRODUCT_DETAIL_PAGE = "clickPDP"
        }

        object EventLabel {
            const val SHARE_TO = "Share - "
        }

        object Category {
            const val CATEGORY_PAGE = "Category Page"
            const val REFERRAL = "Referral"
            const val APPSHARE = "App share"
            const val PRODUCT_DETAIL = "Product Detail Page"
        }

        object Action {
            const val CATEGORY_SHARE = "Bottom Navigation - Share"
            const val SELECT_CHANNEL = "select channel"
            const val CLICK = "Click"
        }

        object MoEngage {
            const val CHANNEL = "channel"
        }

        object EventMoEngage {
            const val REFERRAL_SHARE_EVENT = "Share_Event"
        }
    }
}
