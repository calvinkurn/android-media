package com.tokopedia.people.analytic.tracker

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.people.analytic.UserProfileAnalytics
import com.tokopedia.track.builder.Tracker
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 07, 2023
 */
class UserProfileGeneralTrackerImpl @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) : UserProfileGeneralTracker {

    override fun openUserProfile(userId: String, live: Boolean) {
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_OPEN_SCREEN
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Variable.currentSite
        map[UserProfileAnalytics.Constants.IS_LOGGED_IN_STATUS] = "${userSession.isLoggedIn.not()}"
        map[UserProfileAnalytics.Constants.SCREEN_NAME] = "/${UserProfileAnalytics.Category.FEED_USER_PROFILE} - ${
            UserProfileAnalytics.Function.isLiveOrNotLive(
                live
            )
        }"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = UserProfileAnalytics.Variable.sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.KEY_TRACKER_ID] = "24604"

        UserProfileAnalytics.Variable.analyticTracker.sendGeneralEvent(map)
    }

    override fun clickBack(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_BACK,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24605",
            )
        )
    }

    override fun clickShare(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_SHARE,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24606",
            )
        )
    }

    override fun clickBurgerMenu(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_BURGER_MENU,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24607",
            )
        )
    }

    override fun clickProfilePicture(userId: String, self: Boolean, activityId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_PROFILE_PICTURE,
                label = "$activityId - $userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)} - live"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24608",
            )
        )
    }

    override fun clickFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_FOLLOWER,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24609",
            )
        )
    }

    override fun clickFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_FOLLOWING,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24610",
            )
        )
    }

    override fun clickSelengkapnya(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_SELENGKAPNYA,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24611",
            )
        )
    }

    override fun clickFollow(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_FOLLOW,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24612"
            )
        )
    }

    override fun clickUnfollow(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_UNFOLLOW,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24613",
            )
        )
    }

    override fun clickVideoTab(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_VIDEO_TAB,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24614",
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
                event = UserProfileAnalytics.Constants.PROMO_VIEW,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.IMPRESSION_VIDEO,
                label = "$activityId - $userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)} - ${UserProfileAnalytics.Function.isLiveOrVod(live)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.ECOMMERCE to hashMapOf(
                    UserProfileAnalytics.Constants.PROMO_VIEW to hashMapOf(
                        UserProfileAnalytics.Constants.PROMOTIONS to listOf(
                            convertToPromotion(activityId, imageUrl, videoPosition, "/${UserProfileAnalytics.Category.FEED_USER_PROFILE_VIDEO}")
                        )
                    )
                )
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24615",
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
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_VIDEO,
                label = "$activityId - $userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)} - ${UserProfileAnalytics.Function.isLiveOrVod(live)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24616",
            )
        )
    }

    override fun clickFeedTab(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_FEED_TAB,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24617",
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
                event = UserProfileAnalytics.Constants.PROMO_VIEW,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.IMPRESSION_POST,
                label = "$activityId - $userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)} - $mediaType"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.ECOMMERCE to hashMapOf(
                    UserProfileAnalytics.Constants.PROMO_VIEW to hashMapOf(
                        UserProfileAnalytics.Constants.PROMOTIONS to listOf(
                            convertToPromotion(activityId, imageUrl, postPosition + 1, "/${UserProfileAnalytics.Category.FEED_USER_PROFILE_POST}")
                        )
                    )
                )
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24619",
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
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_POST,
                label = "$activityId - $userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)} - $mediaType"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24620",
            )
        )
    }

    override fun openFollowersTab(userId: String) {
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_OPEN_SCREEN
        map[UserProfileAnalytics.Constants.SCREEN_NAME] = "/${UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWER_TAB}"
        map[UserProfileAnalytics.Constants.IS_LOGGED_IN_STATUS] = "${userSession.isLoggedIn.not()}"
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Variable.currentSite
        map[UserProfileAnalytics.Constants.KEY_TRACKER_ID] = "24621"
        UserProfileAnalytics.Variable.analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUserFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWER_TAB,
                action = UserProfileAnalytics.Action.CLICK_USER,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24622",
            )
        )
    }

    override fun clickFollowFromFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWER_TAB,
                action = UserProfileAnalytics.Action.CLICK_FOLLOW,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24623",
            )
        )
    }

    override fun clickUnfollowFromFollowers(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWER_TAB,
                action = UserProfileAnalytics.Action.CLICK_UNFOLLOW,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24638",
            )
        )
    }

    override fun openFollowingTab(userId: String) {
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_OPEN_SCREEN
        map[UserProfileAnalytics.Constants.SCREEN_NAME] = "/${UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWING_TAB}"
        map[UserProfileAnalytics.Constants.IS_LOGGED_IN_STATUS] = "${userSession.isLoggedIn.not()}"
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Variable.currentSite
        map[UserProfileAnalytics.Constants.KEY_TRACKER_ID] = "24639"
        UserProfileAnalytics.Variable.analyticTracker.sendGeneralEvent(map)
    }

    override fun clickUserFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWING_TAB,
                action = UserProfileAnalytics.Action.CLICK_USER,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24640",
            )
        )
    }

    override fun clickFollowFromFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWING_TAB,
                action = UserProfileAnalytics.Action.CLICK_FOLLOW,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24641",
            )
        )
    }

    override fun clickUnfollowFromFollowing(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWING_TAB,
                action = UserProfileAnalytics.Action.CLICK_UNFOLLOW,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "24642",
            )
        )
    }

    override fun impressionProfileCompletionPrompt(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.IMPRESSION_PROFILE_COMPLETION_PROMPT,
                label = userId
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26372",
            )
        )
    }

    override fun clickProfileCompletionPrompt(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_PROFILE_COMPLETION_PROMPT,
                label = userId
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26373",
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
                event = UserProfileAnalytics.Constants.PROMO_VIEW,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.IMPRESSION_PROFILE_RECOMMENDATIONS_CAROUSEL,
                label = getShopRecomEventLabel(userId, shops)
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.ECOMMERCE to hashMapOf(
                    UserProfileAnalytics.Constants.PROMO_VIEW to hashMapOf(
                        UserProfileAnalytics.Constants.PROMOTIONS to listOf(
                            convertToPromotion(
                                shops.id.toString(),
                                shops.logoImageURL,
                                postPosition,
                                UserProfileAnalytics.ScreenName.FEED_USER_PROFILE_PROFILE_RECOMMENDATION_CAROUSEL
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26374",
            )
        )
    }

    override fun clickProfileRecommendation(
        userId: String,
        item: ShopRecomUiModelItem,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_PROFILE_RECOMMENDATION,
                label = getShopRecomEventLabel(userId, item)
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26375",
            )
        )
    }

    override fun clickFollowProfileRecommendation(userId: String, item: ShopRecomUiModelItem) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_CONTENT,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_FOLLOW_PROFILE_RECOMMENDATION,
                label = getShopRecomEventLabel(userId, item)
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26376",
            )
        )
    }

    override fun clickCreatePost(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_CREATE_POST,
                label = userId
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26377",
            )
        )
    }

    override fun impressionOnBoardingBottomSheetWithUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = UserProfileAnalytics.Action.IMPRESSION_ONBOARDING_BOTTOMSHEET_WITH_USERNAME,
                label = userId
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26378",
            )
        )
    }

    override fun clickLanjutOnBoardingBottomSheetWithUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = UserProfileAnalytics.Action.CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITH_USERNAME,
                label = userId
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26379",
            )
        )
    }

    override fun impressionOnBoardingBottomSheetWithoutUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = UserProfileAnalytics.Action.IMPRESSION_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME,
                label = userId
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26380",
            )
        )
    }

    override fun clickLanjutOnBoardingBottomSheetWithoutUsername(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET,
                action = UserProfileAnalytics.Action.CLICK_LANJUT_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME,
                label = userId
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "26381",
            )
        )
    }

    override fun clickEditProfileButtonInOwnProfile(userId: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_EDIT_PROFILE_BUTTON_IN_OWN_PROFILE,
                label = userId
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT,
                UserProfileAnalytics.Constants.KEY_TRACKER_ID to "33771",
            )
        )
    }

    /**
     * other
     */
    override fun clickShareButton(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_SHARE_BUTTON,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT
            )
        )
    }

    override fun clickCloseShareButton(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_CLOSE_SHARE_BUTTON,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT
            )
        )
    }

    override fun clickShareChannel(userId: String, self: Boolean, channel: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_SHARE_CHANNEL,
                label = "$channel - $userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT
            )
        )
    }

    override fun viewShareChannel(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_VIEW_COMMUNICATION,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.VIEW_SHARE_CHANNEL,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT
            )
        )
    }

    override fun viewScreenshotShareBottomsheet(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_VIEW_COMMUNICATION,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.VIEW_SHARE_SCREENSHOT_BOTTOMSHEET,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT
            )
        )
    }

    override fun clickCloseScreenshotShareBottomsheet(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_CLOSE_SHARE_SCREENSHOT_BOTTOMSHEET,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT
            )
        )
    }

    override fun clickChannelScreenshotShareBottomsheet(userId: String, self: Boolean) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_CHANNEL_SHARE_SCREENSHOT_BOTTOMSHEET,
                label = "$userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT
            )
        )
    }

    override fun clickAccessMedia(userId: String, self: Boolean, allow: String) {
        trackingQueue.putEETracking(
            EventModel(
                event = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION,
                category = UserProfileAnalytics.Category.FEED_USER_PROFILE,
                action = UserProfileAnalytics.Action.CLICK_ACCESS_MEDIA,
                label = "$allow - $userId - ${UserProfileAnalytics.Function.isSelfOrVisitor(self)}"
            ),
            hashMapOf(
                UserProfileAnalytics.Constants.CURRENT_SITE to UserProfileAnalytics.Variable.currentSite,
                UserProfileAnalytics.Constants.SESSION_IRIS to UserProfileAnalytics.Variable.sessionIris,
                UserProfileAnalytics.Constants.USER_ID to userId,
                UserProfileAnalytics.Constants.BUSINESS_UNIT to UserProfileAnalytics.Constants.CONTENT
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
            .setEvent(UserProfileAnalytics.Event.EVENT_CLICK_CONTENT)
            .setEventCategory(UserProfileAnalytics.Category.FEED_USER_PROFILE)
            .setEventAction("click - buat video")
            .setEventLabel("$userId - user")
            .setCustomProperty(UserProfileAnalytics.Constants.TRACKER_ID, "37593")
            .setBusinessUnit(UserProfileAnalytics.Constants.PLAY)
            .setCurrentSite(UserProfileAnalytics.Variable.currentSite)
            .setCustomProperty(
                UserProfileAnalytics.Constants.SESSION_IRIS,
                UserProfileAnalytics.Variable.sessionIris
            )
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    /**
     * Row 81
     */
    override fun viewCreateShorts(userId: String) {
        Tracker.Builder()
            .setEvent(UserProfileAnalytics.Event.EVENT_VIEW_CONTENT_IRIS)
            .setEventCategory(UserProfileAnalytics.Category.FEED_USER_PROFILE)
            .setEventAction("view - buat video")
            .setEventLabel("$userId - user")
            .setCustomProperty(UserProfileAnalytics.Constants.TRACKER_ID, "37604")
            .setBusinessUnit(UserProfileAnalytics.Constants.PLAY)
            .setCurrentSite(UserProfileAnalytics.Variable.currentSite)
            .setCustomProperty(
                UserProfileAnalytics.Constants.SESSION_IRIS,
                UserProfileAnalytics.Variable.sessionIris
            )
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
            UserProfileAnalytics.Constants.ID to shopID,
            UserProfileAnalytics.Constants.CREATIVE to imageUrl,
            UserProfileAnalytics.Constants.POSITION to position,
            UserProfileAnalytics.Constants.NAME to name
        )
    }
}
