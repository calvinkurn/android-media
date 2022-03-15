package com.tokopedia.people

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

class UserProfileAnalytics {
    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    private object ScreenName {
        const val FEED_USER_PROFILE = "Feed User Profile"
        const val FOLLOWER_FOLLOWING_LIST = "Feed User Profile - Follower/Following List"
        const val SHARE_BUTTON_UFP = "Share button on User Feed page"
        const val SCREENSHOT_SHARE_BUTTON_UFP = "Screenshot share bottom sheet on User Feed page"
        const val SHARE_BOTTOM_SHEET = "Share bottom sheet on User Feed page"
        const val SHARE_BOTTOM_SHEET_POPUP = "Pop-up for image access authorization User Feed page"
    }

    private object Action {
        const val CLICK_BACK = "click - back"
        const val CLICK_SHARE = "click - share"
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
        const val CLICK_FOLLOW = "click - follow"
        const val CLICK_UNFOLLOW = "click - unfollow"
        const val CLICK_PROFILE_COMPLETION_PROMPT = "click - profile completion prompt"
        const val CLICK_USER = "click - user"

    }

    private object Event {

    }

    private object Category {
    }
}