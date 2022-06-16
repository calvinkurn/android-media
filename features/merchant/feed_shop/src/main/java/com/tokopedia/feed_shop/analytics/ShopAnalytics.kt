package com.tokopedia.feed_shop.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 2019-08-07.
 */
class ShopAnalytics @Inject constructor(
        private val userSession: UserSessionInterface
) {

    companion object Constant {
        /**
         * Event
         */
        private const val EVENT_CLICK_FEED = "clickFeed"

        /**
         * Category
         */
        private const val CATEGORY_SHOP_PAGE = "content feed - shop page"

        /**
         * Action - General
         */
        private const val ACTION_GENERAL_CLICK = "click %s"

        /**
         * Action - Specific
         */
        private const val ACTION_CREATE_POST = "buat post"
    }

    fun eventClickCreatePost() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_FEED,
                CATEGORY_SHOP_PAGE,
                String.format(ACTION_GENERAL_CLICK, ACTION_CREATE_POST),
                userSession.userId
        )
    }
}