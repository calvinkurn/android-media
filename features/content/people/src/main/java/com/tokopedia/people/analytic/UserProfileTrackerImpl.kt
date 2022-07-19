package com.tokopedia.people.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_ACCESS_MEDIA
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_BACK
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_BURGER_MENU
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_CHANNEL_SHARE_SCREENSHOT_BOTTOMSHEET
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_CLOSE_SHARE_BUTTON
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_CLOSE_SHARE_SCREENSHOT_BOTTOMSHEET
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_CREATE_POST
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_EDIT_PROFILE_BUTTON_IN_OWN_PROFILE
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_FEED_TAB
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_FOLLOW
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_FOLLOWER
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_FOLLOWING
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_FOLLOW_PROFILE_RECOMMENDATION
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITH_USERNAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_POST
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_PROFILE_COMPLETION_PROMPT
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_PROFILE_PICTURE
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_PROFILE_RECOMMENDATION
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_SELENGKAPNYA
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_SHARE
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_SHARE_BUTTON
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_SHARE_CHANNEL
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_UNFOLLOW
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_USER
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_VIDEO
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.CLICK_VIDEO_TAB
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.IMPRESSION_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.IMPRESSION_ONBOARDING_BOTTOMSHEET_WITH_USERNAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.IMPRESSION_POST
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.IMPRESSION_PROFILE_COMPLETION_PROMPT
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.IMPRESSION_PROFILE_RECOMMENDATIONS_CAROUSEL
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.IMPRESSION_VIDEO
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.VIEW_SHARE_CHANNEL
import com.tokopedia.people.analytic.UserProfileAnalytics.Action.VIEW_SHARE_SCREENSHOT_BOTTOMSHEET
import com.tokopedia.people.analytic.UserProfileAnalytics.Category.FEED_USER_PROFILE
import com.tokopedia.people.analytic.UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWER_TAB
import com.tokopedia.people.analytic.UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWING_TAB
import com.tokopedia.people.analytic.UserProfileAnalytics.Category.FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET
import com.tokopedia.people.analytic.UserProfileAnalytics.Category.FEED_USER_PROFILE_POST
import com.tokopedia.people.analytic.UserProfileAnalytics.Category.FEED_USER_PROFILE_VIDEO
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.BUSINESS_UNIT
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.CONTENT
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.CREATIVE
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.CURRENT_SITE
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.ECOMMERCE
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.EVENT
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.EVENT_ACTION
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.EVENT_CATEGORY
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.EVENT_LABEL
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.ID
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.LIVE
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.NAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.NOT_LIVE
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.POSITION
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.PROMOTIONS
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.PROMO_CLICK
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.SCREEN_NAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.SELF
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.SESSION_IRIS
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.TOKOPEDIA_SELLER
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.USER_ID
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.VISITOR
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.VOD
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_FEED
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_OPEN_SCREEN
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_SELECT_CONTENT
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_VIEW_COMMUNICATION
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_VIEW_ITEM
import com.tokopedia.people.analytic.UserProfileAnalytics.ScreenName.FEED_USER_PROFILE_PROFILE_RECOMMENDATION_CAROUSEL
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import javax.inject.Inject

class UserProfileTrackerImpl @Inject constructor(
    private val trackingQueue: TrackingQueue,
) : UserProfileTracker {

    override fun openUserProfile(userId: String, live: Boolean) {
        val label = if (live) LIVE
        else NOT_LIVE
        
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_OPEN_SCREEN
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        map[SCREEN_NAME] = "$FEED_USER_PROFILE - $label"
        map[USER_ID] = userId

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickBack(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_BACK
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"
        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickShare(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_SHARE
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickBurgerMenu(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_BURGER_MENU
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickProfilePicture(userId: String, self: Boolean, activityId: String) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_PROFILE_PICTURE
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$activityId - $userId - $label - live"
        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickFollowers(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_FOLLOWER
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickFollowing(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_FOLLOWING
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickSelengkapnya(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_SELENGKAPNYA
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickFollow(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_FOLLOW
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickUnfollow(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_UNFOLLOW
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun clickVideoTab(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_VIDEO_TAB
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    override fun impressionVideo(
        userId: String,
        self: Boolean,
        live: Boolean,
        activityId: String,
        imageUrl: String,
        videoPosition: Int
    ) {
        val label = if (self) SELF
        else VISITOR
        
        val labelLive = if (live) LIVE
        else VOD
        
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_VIEW_ITEM
        map[EVENT_ACTION] = IMPRESSION_VIDEO
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$activityId - $userId - $label - $labelLive"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        val promoMap = mutableMapOf<String, Any>()
        promoMap[CREATIVE] = imageUrl
        promoMap[POSITION] = videoPosition
        promoMap[ID] = activityId
        promoMap[NAME] = FEED_USER_PROFILE_VIDEO
        map[PROMOTIONS] = listOf(promoMap)
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickVideo(
        userId: String,
        self: Boolean,
        live: Boolean,
        activityId: String,
        imageUrl: String,
        videoPosition: Int
    ) {
        val label = if (self) SELF
        else VISITOR

        val labelLive = if (live) LIVE
        else VOD
        
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_SELECT_CONTENT
        map[EVENT_ACTION] = CLICK_VIDEO
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$activityId - $userId - $label - $labelLive"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        val promoMap = mutableMapOf<String, Any>()
        promoMap[CREATIVE] = imageUrl
        promoMap[POSITION] = videoPosition
        promoMap[ID] = activityId
        promoMap[NAME] = FEED_USER_PROFILE_VIDEO
        map[PROMOTIONS] = listOf(promoMap)
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickFeedTab(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_FEED_TAB
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun impressionPost(
        userId: String,
        self: Boolean,
        activityId: String,
        imageUrl: String,
        postPosition: Int,
        mediaType: String
    ) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_VIEW_ITEM
        map[EVENT_ACTION] = IMPRESSION_POST
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$activityId - $userId - $label - $mediaType"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        val promoMap = mutableMapOf<String, Any>()
        promoMap[CREATIVE] = imageUrl
        promoMap[POSITION] = postPosition
        promoMap[ID] = activityId
        promoMap[NAME] = FEED_USER_PROFILE_POST
        map[PROMOTIONS] = listOf(promoMap)
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickPost(
        userId: String,
        self: Boolean,
        activityId: String,
        imageUrl: String,
        postPosition: Int,
        mediaType: String
    ) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_SELECT_CONTENT
        map[EVENT_ACTION] = CLICK_POST
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$activityId - $userId - $label - $mediaType"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        val promoMap = mutableMapOf<String, Any>()
        promoMap[CREATIVE] = imageUrl
        promoMap[POSITION] = postPosition
        promoMap[ID] = activityId
        promoMap[NAME] = FEED_USER_PROFILE_POST
        map[PROMOTIONS] = listOf(promoMap)
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickShareButton(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_COMMUNICATION
        map[EVENT_ACTION] = CLICK_SHARE_BUTTON
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickCloseShareButton(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_COMMUNICATION
        map[EVENT_ACTION] = CLICK_CLOSE_SHARE_BUTTON
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickShareChannel(userId: String, self: Boolean, channel: String) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_COMMUNICATION
        map[EVENT_ACTION] = CLICK_SHARE_CHANNEL
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$channel - $userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun viewShareChannel(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_VIEW_COMMUNICATION
        map[EVENT_ACTION] = VIEW_SHARE_CHANNEL
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun viewScreenshotShareBottomsheet(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_VIEW_COMMUNICATION
        map[EVENT_ACTION] = VIEW_SHARE_SCREENSHOT_BOTTOMSHEET
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickCloseScreenshotShareBottomsheet(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_COMMUNICATION
        map[EVENT_ACTION] = CLICK_CLOSE_SHARE_SCREENSHOT_BOTTOMSHEET
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickChannelScreenshotShareBottomsheet(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_COMMUNICATION
        map[EVENT_ACTION] = CLICK_CHANNEL_SHARE_SCREENSHOT_BOTTOMSHEET
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickAccessMedia(userId: String, self: Boolean, allow: String) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_COMMUNICATION
        map[EVENT_ACTION] = CLICK_ACCESS_MEDIA
        map[EVENT_CATEGORY] = FEED_USER_PROFILE
        map[EVENT_LABEL] = "$allow - $userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun openFollowersTab(userId: String) {
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_OPEN_SCREEN
        map[SCREEN_NAME] = FEED_USER_PROFILE_FOLLOWER_TAB

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUserFollowers(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_USER
        map[EVENT_CATEGORY] = FEED_USER_PROFILE_FOLLOWER_TAB
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickFollowFromFollowers(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_FOLLOW
        map[EVENT_CATEGORY] = FEED_USER_PROFILE_FOLLOWER_TAB
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUnfollowFromFollowers(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_UNFOLLOW
        map[EVENT_CATEGORY] = FEED_USER_PROFILE_FOLLOWER_TAB
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun openFollowingTab(userId: String) {
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_OPEN_SCREEN
        map[SCREEN_NAME] = FEED_USER_PROFILE_FOLLOWING_TAB

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUserFollowing(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_USER
        map[EVENT_CATEGORY] = FEED_USER_PROFILE_FOLLOWING_TAB
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickFollowFromFollowing(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_FOLLOW
        map[EVENT_CATEGORY] = FEED_USER_PROFILE_FOLLOWING_TAB
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUnfollowFromFollowing(userId: String, self: Boolean) {
        val label = if (self) SELF
        else VISITOR

        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_CLICK_FEED
        map[EVENT_ACTION] = CLICK_UNFOLLOW
        map[EVENT_CATEGORY] = FEED_USER_PROFILE_FOLLOWING_TAB
        map[EVENT_LABEL] = "$userId - $label"

        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    override fun impressionProfileCompletionPrompt(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = IMPRESSION_PROFILE_COMPLETION_PROMPT,
                label = userId
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickProfileCompletionPrompt(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_PROFILE_COMPLETION_PROMPT,
                label = userId
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun impressionProfileRecommendation(
        userId: String,
        shopId: String,
        imageUrl: String,
        postPosition: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_ITEM,
                category = FEED_USER_PROFILE,
                action = IMPRESSION_PROFILE_RECOMMENDATIONS_CAROUSEL,
                label = "$userId - $shopId"
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    EVENT_SELECT_CONTENT to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotionShopRecommendationCarousel(
                                shopId,
                                imageUrl,
                                postPosition
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickProfileRecommendation(
        userId: String,
        shopId: String,
        imageUrl: String,
        postPosition: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_CLICK,
                category = FEED_USER_PROFILE,
                action = CLICK_PROFILE_RECOMMENDATION,
                label = "$userId - $shopId"
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_CLICK to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotionShopRecommendationCarousel(
                                shopId,
                                imageUrl,
                                postPosition
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickFollowProfileRecommendation(userId: String, shopId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_FOLLOW_PROFILE_RECOMMENDATION,
                label = "$userId - $shopId"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickCreatePost(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_CREATE_POST,
                label = userId
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun impressionOnBoardingBottomSheetWithUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_HOME_PAGE,
                category = FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = IMPRESSION_ONBOARDING_BOTTOMSHEET_WITH_USERNAME,
                label = userId
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickLanjutOnBoardingBottomSheetWithUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITH_USERNAME,
                label = userId
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun impressionOnBoardingBottomSheetWithoutUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_HOME_PAGE,
                category = FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = IMPRESSION_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME,
                label = userId
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickLanjutOnBoardingBottomSheetWithoutUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME,
                label = userId
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickEditProfileButtonInOwnProfile(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_EDIT_PROFILE_BUTTON_IN_OWN_PROFILE,
                label = userId
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun sendAll() {
        trackingQueue.sendAll()
    }

    val currentSite: String
        get() = if (GlobalConfig.isSellerApp()) TOKOPEDIA_SELLER
        else TOKOPEDIA_MARKETPLACE

    private fun convertToPromotionShopRecommendationCarousel(
        imageUrl: String,
        shopID: String,
        position: Int
    ): HashMap<String, Any> {
        return hashMapOf(
            CREATIVE to imageUrl,
            POSITION to position,
            ID to shopID,
            NAME to FEED_USER_PROFILE_PROFILE_RECOMMENDATION_CAROUSEL,
        )
    }

}