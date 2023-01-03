package com.tokopedia.people.analytic.tracker

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
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
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.ID
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.IS_LOGGED_IN_STATUS
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.NAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.POSITION
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.PROMOTIONS
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.PROMO_CLICK
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.PROMO_VIEW
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.SCREEN_NAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.SESSION_IRIS
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.USER_ID
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_FEED
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_OPEN_SCREEN
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_VIEW_COMMUNICATION
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE
import com.tokopedia.people.analytic.UserProfileAnalytics.Function.isLiveOrNotLive
import com.tokopedia.people.analytic.UserProfileAnalytics.Function.isLiveOrVod
import com.tokopedia.people.analytic.UserProfileAnalytics.Function.isSelfOrVisitor
import com.tokopedia.people.analytic.UserProfileAnalytics.ScreenName.FEED_USER_PROFILE_PROFILE_RECOMMENDATION_CAROUSEL
import com.tokopedia.people.analytic.UserProfileAnalytics.Variable.analyticTracker
import com.tokopedia.people.analytic.UserProfileAnalytics.Variable.currentSite
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UserProfileTrackerImpl @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface,
) : UserProfileTracker {

    override fun openUserProfile(userId: String, live: Boolean) {
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_OPEN_SCREEN
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        map[IS_LOGGED_IN_STATUS] = "${userSession.isLoggedIn.not()}"
        map[SCREEN_NAME] = "/$FEED_USER_PROFILE - ${isLiveOrNotLive(live)}"
        map[SESSION_IRIS] = TrackApp.getInstance().gtm.irisSessionId
        map[USER_ID] = userId

        analyticTracker.sendGeneralEvent(map)
    }

    override fun clickBack(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_BACK,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickShare(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_SHARE,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickBurgerMenu(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_BURGER_MENU,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickProfilePicture(userId: String, self: Boolean, activityId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_PROFILE_PICTURE,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - live",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_FOLLOWER,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_FOLLOWING,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickSelengkapnya(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_SELENGKAPNYA,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickFollow(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_FOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickUnfollow(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_UNFOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickVideoTab(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_VIDEO_TAB,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun impressionVideo(
        userId: String,
        self: Boolean,
        live: Boolean,
        activityId: String,
        imageUrl: String,
        videoPosition: Int,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_VIEW,
                category = FEED_USER_PROFILE,
                action = IMPRESSION_VIDEO,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - ${isLiveOrVod(live)}",
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_VIEW to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotion(activityId, imageUrl, videoPosition, "/$FEED_USER_PROFILE_VIDEO"),
                        ),
                    ),
                ),
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickVideo(
        userId: String,
        self: Boolean,
        live: Boolean,
        activityId: String,
        imageUrl: String,
        videoPosition: Int,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_CLICK,
                category = FEED_USER_PROFILE,
                action = CLICK_VIDEO,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - ${isLiveOrVod(live)}",
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_CLICK to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotion(
                                activityId,
                                imageUrl,
                                videoPosition,
                                "/$FEED_USER_PROFILE_VIDEO",
                            ),
                        ),
                    ),
                ),
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickFeedTab(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_FEED_TAB,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun impressionPost(
        userId: String,
        self: Boolean,
        activityId: String,
        imageUrl: String,
        postPosition: Int,
        mediaType: String,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_VIEW,
                category = FEED_USER_PROFILE,
                action = IMPRESSION_POST,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - $mediaType",
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_VIEW to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotion(activityId, imageUrl, postPosition + 1, "/$FEED_USER_PROFILE_POST"),
                        ),
                    ),
                ),
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickPost(
        userId: String,
        self: Boolean,
        activityId: String,
        imageUrl: String,
        postPosition: Int,
        mediaType: String,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_CLICK,
                category = FEED_USER_PROFILE,
                action = CLICK_POST,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - $mediaType",
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_CLICK to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotion(
                                activityId,
                                imageUrl,
                                postPosition + 1,
                                "/$FEED_USER_PROFILE_POST",
                            ),
                        ),
                    ),
                ),
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickShareButton(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_SHARE_BUTTON,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickCloseShareButton(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_CLOSE_SHARE_BUTTON,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickShareChannel(userId: String, self: Boolean, channel: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_SHARE_CHANNEL,
                label = "$channel - $userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun viewShareChannel(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = VIEW_SHARE_CHANNEL,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun viewScreenshotShareBottomsheet(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = VIEW_SHARE_SCREENSHOT_BOTTOMSHEET,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickCloseScreenshotShareBottomsheet(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_CLOSE_SHARE_SCREENSHOT_BOTTOMSHEET,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickChannelScreenshotShareBottomsheet(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_CHANNEL_SHARE_SCREENSHOT_BOTTOMSHEET,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickAccessMedia(userId: String, self: Boolean, allow: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_ACCESS_MEDIA,
                label = "$allow - $userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun openFollowersTab(userId: String) {
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_OPEN_SCREEN
        map[SCREEN_NAME] = "/$FEED_USER_PROFILE_FOLLOWER_TAB"
        map[IS_LOGGED_IN_STATUS] = "${userSession.isLoggedIn.not()}"
        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUserFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWER_TAB,
                action = CLICK_USER,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickFollowFromFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWER_TAB,
                action = CLICK_FOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickUnfollowFromFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWER_TAB,
                action = CLICK_UNFOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun openFollowingTab(userId: String) {
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_OPEN_SCREEN
        map[SCREEN_NAME] = "/$FEED_USER_PROFILE_FOLLOWING_TAB"
        map[IS_LOGGED_IN_STATUS] = "${userSession.isLoggedIn.not()}"
        map[USER_ID] = userId
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUserFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWING_TAB,
                action = CLICK_USER,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickFollowFromFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWING_TAB,
                action = CLICK_FOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickUnfollowFromFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWING_TAB,
                action = CLICK_UNFOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun impressionProfileCompletionPrompt(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = IMPRESSION_PROFILE_COMPLETION_PROMPT,
                label = userId,
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickProfileCompletionPrompt(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_PROFILE_COMPLETION_PROMPT,
                label = userId,
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun impressionProfileRecommendation(
        userId: String,
        shops: ShopRecomUiModelItem,
        postPosition: Int,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_VIEW,
                category = FEED_USER_PROFILE,
                action = IMPRESSION_PROFILE_RECOMMENDATIONS_CAROUSEL,
                label = "$userId - $userId",
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_VIEW to hashMapOf(
                        PROMOTIONS to convertToPromotion(
                            shops.id.toString(),
                            shops.logoImageURL,
                            postPosition,
                            FEED_USER_PROFILE_PROFILE_RECOMMENDATION_CAROUSEL,
                        ),
                    ),
                ),
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickProfileRecommendation(
        userId: String,
        shopId: String,
        imageUrl: String,
        postPosition: Int,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_CLICK,
                category = FEED_USER_PROFILE,
                action = CLICK_PROFILE_RECOMMENDATION,
                label = "$userId - $shopId",
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_CLICK to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotion(
                                shopId,
                                imageUrl,
                                postPosition,
                                FEED_USER_PROFILE_PROFILE_RECOMMENDATION_CAROUSEL,
                            ),
                        ),
                    ),
                ),
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickFollowProfileRecommendation(userId: String, shopId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_FOLLOW_PROFILE_RECOMMENDATION,
                label = "$userId - $shopId",
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickCreatePost(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_CREATE_POST,
                label = userId,
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun impressionOnBoardingBottomSheetWithUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_HOME_PAGE,
                category = FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = IMPRESSION_ONBOARDING_BOTTOMSHEET_WITH_USERNAME,
                label = userId,
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickLanjutOnBoardingBottomSheetWithUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITH_USERNAME,
                label = userId,
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun impressionOnBoardingBottomSheetWithoutUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_HOME_PAGE,
                category = FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = IMPRESSION_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME,
                label = userId,
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickLanjutOnBoardingBottomSheetWithoutUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME,
                label = userId,
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun clickEditProfileButtonInOwnProfile(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_EDIT_PROFILE_BUTTON_IN_OWN_PROFILE,
                label = userId,
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
            ),
        )
    }

    override fun sendAll() {
        trackingQueue.sendAll()
    }

    private fun convertToPromotion(
        shopID: String,
        imageUrl: String,
        position: Int,
        name: String,
    ): HashMap<String, Any> {
        return hashMapOf(
            ID to shopID,
            CREATIVE to imageUrl,
            POSITION to position,
            NAME to name,
        )
    }
}
