package com.tokopedia.people

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

class UserProfileAnalytics {
    val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm


    object Constants {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val SESSION_IRIS = "sessionIris"
        const val IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        const val SCREEN_NAME = "screenName"
        const val USER_ID = "userId"
        const val CONTENT = "content"
        const val PROMOTIONS = "promotions"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace "
    }

    object Event{
        const val EVENT_OPEN_SCREEN = "openScreen"
        const val EVENT_CLICK_FEED = "clickFeed"
        const val EVENT_VIEW_ITEM = "view_item"
        const val EVENT_SELECT_CONTENT = "select_content"
        const val EVENT_VIEW_HOME_PAGE = "viewHomepageIris"
        const val EVENT_CLICK_HOME_PAGE = "clickHomepage"
        const val EVENT_CLICK_COMMUNICATION = "clickCommunication"
        const val EVENT_VIEW_COMMUNICATION = "viewCommunicationIris"

    }

    object ScreenName {
        const val FEED_USER_PROFILE = "Feed User Profile"
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
        const val CLICK_CLOSE_SHARE_SCREENSHOT_BOTTOMSHEET = "click - close screenshot share bottom sheet"
        const val CLICK_CHANNEL_SHARE_SCREENSHOT_BOTTOMSHEET = "click - channel share bottom sheet - screenshot"
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
        const val IMPRESSION_ONBOARDING_BOTTOMSHEET_WITH_USERNAME = "impression - onboarding bottomsheet with username"
        const val CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITH_USERNAME = "click - lanjut on onboarding bottomsheet with username"
        const val IMPRESSION_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME = "impression - onboarding bottomsheet without username"
        const val CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME = "click - lanjut on onboarding bottomsheet without username"
        const val CLICK_USER = "click - user"

    }

    object Category {
        const val FEED_USER_PROFILE = "feed user profile"
        const val FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET = "feed user profile - onboarding bottomsheet"
        const val FEED_USER_PROFILE_FOLLOWER_TAB = "feed user profile - follower tab"
        const val FEED_USER_PROFILE_FOLLOWING_TAB = "feed user profile - following tab"
    }
}