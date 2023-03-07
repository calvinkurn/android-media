package com.tokopedia.people.analytic.tracker

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.kotlin.extensions.view.EMPTY
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
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.KEY_TRACKER_ID
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.NAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.PLAY
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.POSITION
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.PROMOTIONS
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.PROMO_VIEW
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.SCREEN_NAME
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.SESSION_IRIS
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.TRACKER_ID
import com.tokopedia.people.analytic.UserProfileAnalytics.Constants.USER_ID
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_CONTENT
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_FEED
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_OPEN_SCREEN
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_VIEW_COMMUNICATION
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_VIEW_CONTENT_IRIS
import com.tokopedia.people.analytic.UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE
import com.tokopedia.people.analytic.UserProfileAnalytics.Function.isLiveOrNotLive
import com.tokopedia.people.analytic.UserProfileAnalytics.Function.isLiveOrVod
import com.tokopedia.people.analytic.UserProfileAnalytics.Function.isSelfOrVisitor
import com.tokopedia.people.analytic.UserProfileAnalytics.ScreenName.FEED_USER_PROFILE_PROFILE_RECOMMENDATION_CAROUSEL
import com.tokopedia.people.analytic.UserProfileAnalytics.Variable.analyticTracker
import com.tokopedia.people.analytic.UserProfileAnalytics.Variable.currentSite
import com.tokopedia.people.analytic.UserProfileAnalytics.Variable.sessionIris
import com.tokopedia.track.builder.Tracker
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UserProfileTrackerImpl @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) : UserProfileTracker {

    override fun openUserProfile(userId: String, live: Boolean) {
        val map = mutableMapOf<String, Any>()
        map[EVENT] = EVENT_OPEN_SCREEN
        map[BUSINESS_UNIT] = CONTENT
        map[CURRENT_SITE] = currentSite
        map[IS_LOGGED_IN_STATUS] = "${userSession.isLoggedIn.not()}"
        map[SCREEN_NAME] = "/$FEED_USER_PROFILE - ${isLiveOrNotLive(live)}"
        map[SESSION_IRIS] = sessionIris
        map[USER_ID] = userId
        map[KEY_TRACKER_ID] = "24604"

        analyticTracker.sendGeneralEvent(map)
    }

    override fun clickBack(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_BACK,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24605",
            )
        )
    }

    override fun clickShare(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_SHARE,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24606",
            )
        )
    }

    override fun clickBurgerMenu(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_HOME_PAGE,
                category = FEED_USER_PROFILE,
                action = CLICK_BURGER_MENU,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24607",
            )
        )
    }

    override fun clickProfilePicture(userId: String, self: Boolean, activityId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_PROFILE_PICTURE,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - live"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24608",
            )
        )
    }

    override fun clickFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_FOLLOWER,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24609",
            )
        )
    }

    override fun clickFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_FOLLOWING,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24610",
            )
        )
    }

    override fun clickSelengkapnya(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_SELENGKAPNYA,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24611",
            )
        )
    }

    override fun clickFollow(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_FOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24612"
            )
        )
    }

    override fun clickUnfollow(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_UNFOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24613",
            )
        )
    }

    override fun clickVideoTab(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_VIDEO_TAB,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24614",
            )
        )
    }

    override fun impressionVideo(
        userId: String,
        self: Boolean,
        live: Boolean,
        activityId: String,
        imageUrl: String,
        videoPosition: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_VIEW,
                category = FEED_USER_PROFILE,
                action = IMPRESSION_VIDEO,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - ${isLiveOrVod(live)}"
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_VIEW to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotion(activityId, imageUrl, videoPosition, "/$FEED_USER_PROFILE_VIDEO")
                        )
                    )
                )
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24615",
            )
        )
    }

    override fun clickVideo(
        userId: String,
        self: Boolean,
        live: Boolean,
        activityId: String,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_CONTENT,
                category = FEED_USER_PROFILE,
                action = CLICK_VIDEO,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - ${isLiveOrVod(live)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24616",
            )
        )
    }

    override fun clickFeedTab(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE,
                action = CLICK_FEED_TAB,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24617",
            )
        )
    }

    override fun impressionPost(
        userId: String,
        self: Boolean,
        activityId: String,
        imageUrl: String,
        postPosition: Int,
        mediaType: String
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_VIEW,
                category = FEED_USER_PROFILE,
                action = IMPRESSION_POST,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - $mediaType"
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_VIEW to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotion(activityId, imageUrl, postPosition + 1, "/$FEED_USER_PROFILE_POST")
                        )
                    )
                )
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24619",
            )
        )
    }

    override fun clickPost(
        userId: String,
        self: Boolean,
        activityId: String,
        mediaType: String
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_CONTENT,
                category = FEED_USER_PROFILE,
                action = CLICK_POST,
                label = "$activityId - $userId - ${isSelfOrVisitor(self)} - $mediaType"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24620",
            )
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
        map[KEY_TRACKER_ID] = "24621"
        analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUserFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWER_TAB,
                action = CLICK_USER,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24622",
            )
        )
    }

    override fun clickFollowFromFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWER_TAB,
                action = CLICK_FOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24623",
            )
        )
    }

    override fun clickUnfollowFromFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWER_TAB,
                action = CLICK_UNFOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24638",
            )
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
        map[KEY_TRACKER_ID] = "24639"
        analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUserFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWING_TAB,
                action = CLICK_USER,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24640",
            )
        )
    }

    override fun clickFollowFromFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWING_TAB,
                action = CLICK_FOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24641",
            )
        )
    }

    override fun clickUnfollowFromFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_FEED,
                category = FEED_USER_PROFILE_FOLLOWING_TAB,
                action = CLICK_UNFOLLOW,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "24642",
            )
        )
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
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26372",
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
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26373",
            )
        )
    }

    override fun impressionProfileRecommendation(
        userId: String,
        shops: ShopRecomUiModelItem,
        postPosition: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = PROMO_VIEW,
                category = FEED_USER_PROFILE,
                action = IMPRESSION_PROFILE_RECOMMENDATIONS_CAROUSEL,
                label = getShopRecomEventLabel(userId, shops)
            ),
            hashMapOf(
                ECOMMERCE to hashMapOf(
                    PROMO_VIEW to hashMapOf(
                        PROMOTIONS to listOf(
                            convertToPromotion(
                                shops.id.toString(),
                                shops.logoImageURL,
                                postPosition,
                                FEED_USER_PROFILE_PROFILE_RECOMMENDATION_CAROUSEL
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26374",
            )
        )
    }

    override fun clickProfileRecommendation(
        userId: String,
        item: ShopRecomUiModelItem,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_CONTENT,
                category = FEED_USER_PROFILE,
                action = CLICK_PROFILE_RECOMMENDATION,
                label = getShopRecomEventLabel(userId, item)
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26375",
            )
        )
    }

    override fun clickFollowProfileRecommendation(userId: String, item: ShopRecomUiModelItem) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_CONTENT,
                category = FEED_USER_PROFILE,
                action = CLICK_FOLLOW_PROFILE_RECOMMENDATION,
                label = getShopRecomEventLabel(userId, item)
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26376",
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
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26377",
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
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26378",
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
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26379",
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
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26380",
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
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "26381",
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
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "33771",
            )
        )
    }

    /**
     * other
     */
    override fun clickShareButton(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_SHARE_BUTTON,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickCloseShareButton(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_CLOSE_SHARE_BUTTON,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickShareChannel(userId: String, self: Boolean, channel: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_SHARE_CHANNEL,
                label = "$channel - $userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun viewShareChannel(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = VIEW_SHARE_CHANNEL,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun viewScreenshotShareBottomsheet(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_VIEW_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = VIEW_SHARE_SCREENSHOT_BOTTOMSHEET,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickCloseScreenshotShareBottomsheet(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_CLOSE_SHARE_SCREENSHOT_BOTTOMSHEET,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickChannelScreenshotShareBottomsheet(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_CHANNEL_SHARE_SCREENSHOT_BOTTOMSHEET,
                label = "$userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    override fun clickAccessMedia(userId: String, self: Boolean, allow: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_CLICK_COMMUNICATION,
                category = FEED_USER_PROFILE,
                action = CLICK_ACCESS_MEDIA,
                label = "$allow - $userId - ${isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                CURRENT_SITE to currentSite,
                SESSION_IRIS to sessionIris,
                USER_ID to userId,
                BUSINESS_UNIT to CONTENT
            )
        )
    }

    /**
     * Mynakama Tracker
     * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
     *
     * Row 70
     */
    override fun clickCreateShorts(userId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventCategory(FEED_USER_PROFILE)
            .setEventAction("click - buat video")
            .setEventLabel("$userId - user")
            .setCustomProperty(TRACKER_ID, "37593")
            .setBusinessUnit(PLAY)
            .setCurrentSite(currentSite)
            .setCustomProperty(SESSION_IRIS, sessionIris)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    /**
     * Row 81
     */
    override fun viewCreateShorts(userId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_CONTENT_IRIS)
            .setEventCategory(FEED_USER_PROFILE)
            .setEventAction("view - buat video")
            .setEventLabel("$userId - user")
            .setCustomProperty(TRACKER_ID, "37604")
            .setBusinessUnit(PLAY)
            .setCurrentSite(currentSite)
            .setCustomProperty(SESSION_IRIS, sessionIris)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    override fun sendAll() {
        trackingQueue.sendAll()
    }

    private fun getShopRecomEventLabel(userId: String, item: ShopRecomUiModelItem) =
        when (item.type) {
            ShopRecomUiModelItem.FOLLOW_TYPE_SHOP -> {
                "$userId - shop - ${item.id}"
            }
            ShopRecomUiModelItem.FOLLOW_TYPE_BUYER -> {
                "$userId - user - ${item.id}"
            }
            else -> String.EMPTY
        }

    private fun convertToPromotion(
        shopID: String,
        imageUrl: String,
        position: Int,
        name: String
    ): HashMap<String, Any> {
        return hashMapOf(
            ID to shopID,
            CREATIVE to imageUrl,
            POSITION to position,
            NAME to name
        )
    }
}
