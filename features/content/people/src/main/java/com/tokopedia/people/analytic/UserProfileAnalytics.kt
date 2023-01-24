package com.tokopedia.people.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.LIVE
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.NOT_LIVE
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.SELF
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.VISITOR
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.VOD
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

class UserProfileAnalytics {

    object Variable {
        val analyticTracker: ContextAnalytics
            get() = TrackApp.getInstance().gtm

        val currentSite: String
            get() = if (GlobalConfig.isSellerApp()) {
                Constants.TOKOPEDIA_SELLER
            } else {
                Constants.TOKOPEDIA_MARKETPLACE
            }
    }

    object Function {
        fun isLiveOrNotLive(isLive: Boolean) = if (isLive) LIVE else NOT_LIVE
        fun isSelfOrVisitor(isSelf: Boolean) = if (isSelf) SELF else VISITOR
        fun isLiveOrVod(isLive: Boolean) = if (isLive) LIVE else VOD
    }

    object Constants {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val BUSINESS_UNIT = "businessUnit"
        const val KEY_TRACKER_ID = "trackerId"
        const val CURRENT_SITE = "currentSite"
        const val SESSION_IRIS = "sessionIris"
        const val CREATIVE = "creative"
        const val POSITION = "position"
        const val ID = "id"
        const val NAME = "name"
        const val IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        const val SCREEN_NAME = "screenName"
        const val USER_ID = "userId"
        const val CONTENT = "content"
        const val PROMOTIONS = "promotions"
        const val ECOMMERCE = "ecommerce"
        const val TOKOPEDIA_SELLER = "tokopediaseller"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
        const val ALLOW = "Allow"
        const val DENY = "Deny"
        const val IZINKAN_AKSES = "Izinkan Akses"
        const val SELF = "self"
        const val VISITOR = "visitor"
        const val LIVE = "live"
        const val NOT_LIVE = "not live"
        const val VOD = "vod"
        const val LAIN_KALI = "Lain Kali"
    }

    object Event {
        const val EVENT_OPEN_SCREEN = "openScreen"
        const val EVENT_CLICK_FEED = "clickFeed"
        const val EVENT_VIEW_ITEM = "view_item"
        const val EVENT_SELECT_CONTENT = "select_content"
        const val EVENT_VIEW_HOME_PAGE = "viewHomepageIris"
        const val EVENT_CLICK_HOME_PAGE = "clickHomepage"
        const val EVENT_CLICK_CONTENT = "clickContent"
        const val EVENT_CLICK_COMMUNICATION = "clickCommunication"
        const val EVENT_VIEW_COMMUNICATION = "viewCommunicationIris"
    }

    object ScreenName {
        const val FEED_USER_PROFILE = "Feed User Profile"
        const val FEED_USER_PROFILE_PROFILE_RECOMMENDATION_CAROUSEL =
            "/feed user profile - profile recommendations carousel"
        const val FOLLOWER_FOLLOWING_LIST = "Feed User Profile - Follower/Following List"
        const val SHARE_BUTTON_UFP = "Share button on User Feed page"
        const val SCREENSHOT_SHARE_BUTTON_UFP = "Screenshot share bottom sheet on User Feed page"
        const val SHARE_BOTTOM_SHEET = "Share bottom sheet on User Feed page"
        const val SHARE_BOTTOM_SHEET_POPUP = "Pop-up for image access authorization User Feed page"
    }

    object Action {
        const val CLICK_BACK = "click - back"
        const val CLICK_SHARE = "click - share"
        const val CLICK_SHARE_BUTTON = "click - share button"
        const val CLICK_SHARE_CHANNEL = "click - sharing channel"
        const val VIEW_SHARE_SCREENSHOT_BOTTOMSHEET = "view - screenshot share bottom sheet"
        const val CLICK_CLOSE_SHARE_SCREENSHOT_BOTTOMSHEET =
            "click - close screenshot share bottom sheet"
        const val CLICK_CHANNEL_SHARE_SCREENSHOT_BOTTOMSHEET =
            "click - channel share bottom sheet - screenshot"
        const val CLICK_ACCESS_MEDIA = "click - access photo media and files"
        const val VIEW_SHARE_CHANNEL = "view on sharing channel"
        const val CLICK_CLOSE_SHARE_BUTTON = "click - close share bottom sheet"
        const val CLICK_BURGER_MENU = "click - burger menu"
        const val CLICK_PROFILE_PICTURE = "click - profile picture"
        const val CLICK_FOLLOWER = "click - follower"
        const val CLICK_FOLLOWING = "click - following"
        const val CLICK_SELENGKAPNYA = "click - selengkapnya"
        const val CLICK_VIDEO_TAB = "click - video tab"
        const val IMPRESSION_VIDEO = "impression - video"
        const val CLICK_VIDEO = "click - video"
        const val CLICK_FEED_TAB = "click - feed tab"
        const val IMPRESSION_POST = "impression - post"
        const val CLICK_POST = "click - post"
        const val CLICK_CREATE_POST = "click - create post button"
        const val CLICK_FOLLOW = "click - follow"
        const val CLICK_UNFOLLOW = "click - unfollow"
        const val CLICK_PROFILE_COMPLETION_PROMPT = "click - profile completion prompt"
        const val CLICK_PROFILE_RECOMMENDATION = "click - profile recommendations icon"
        const val CLICK_FOLLOW_PROFILE_RECOMMENDATION = "click - follow profile recommendations"
        const val IMPRESSION_PROFILE_RECOMMENDATION = "impression - profile recommendations icon"
        const val IMPRESSION_PROFILE_COMPLETION_PROMPT = "impression - profile completion prompt"
        const val IMPRESSION_ONBOARDING_BOTTOMSHEET_WITH_USERNAME =
            "impression - onboarding bottomsheet with username"
        const val CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITH_USERNAME =
            "click - lanjut on onboarding bottomsheet with username"
        const val IMPRESSION_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME =
            "impression - onboarding bottomsheet without username"
        const val IMPRESSION_PROFILE_RECOMMENDATIONS_CAROUSEL =
            "impression - profile recommendations carousel"
        const val CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME =
            "click - lanjut on onboarding bottomsheet without username"
        const val CLICK_EDIT_PROFILE_BUTTON_IN_OWN_PROFILE = "click - ubah profile"
        const val CLICK_USER = "click - user"
    }

    object Category {
        const val FEED_USER_PROFILE = "feed user profile"
        const val FEED_USER_PROFILE_POST = "feed user profile - post"
        const val FEED_USER_PROFILE_VIDEO = "feed user profile - video"
        const val FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET =
            "feed user profile - onboarding bottomsheet"
        const val FEED_USER_PROFILE_FOLLOWER_TAB = "feed user profile - follower tab"
        const val FEED_USER_PROFILE_FOLLOWING_TAB = "feed user profile - following tab"
    }
}
