package com.tokopedia.profile.analytics

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by milhamj on 10/10/18.
 */
class ProfileAnalytics @Inject constructor(private val analyticTracker: AnalyticTracker,
                                           private val userSessionInterface: UserSessionInterface) {
    companion object {
        private const val EVENT = "event"
        private const val CATEGORY = "eventCategory"
        private const val ACTION = "eventAction"
        private const val LABEL = "eventLabel"
        private const val USER_ID = "user_id"
        private const val CONTENT_POSITION = "content_position"
        private const val USER_NAME = "user_name"
        private const val SHOP_ID = "shop_id"
        private const val PRODUCT_ID = "product_id"
        private const val SINGLE = "single"
        private const val MULTIPLE = "multiple"
    }

    object Event {
        const val EVENT_CLICK_TOP_PROFILE = "clickTopProfile"
        const val EVENT_CLICK_PROFILE = "clickProfile"
        const val EVENT_VIEW_PROFILE = "viewProfile"
    }

    object Category {
        const val KOL_TOP_PROFILE = "kol top profile"
        const val USER_PROFILE_PAGE = "user profile page"
    }

    object Action {
        const val CLICK_PROMPT = "click prompt"
        const val CLICK_FOLLOWING = "click following"
        const val CLICK_FOLLOWERS = "click followers"
        const val CLICK_TAG = "click-%s-user-all-%s-tag"
        const val CLICK_CARD = "click-%s-user-all-%s-card"
        const val IMPRESSION_CARD = "impression-%s-user-all-%s"
        const val CLICK_BAGIKAN_PROFILE = "click bagikan profile ini"
        const val CLICK_LIKE = "click-%s-user-all-%s-like"
        const val CLICK_UNLIKE = "click-%s-user-all-%s-unlike"
        const val CLICK_COMMENT = "click-%s-user-all-%s-comment"
    }

    object Label {
        const val GO_TO_FEED_FORMAT = "go to feed - %s"
    }

    private fun getDefaultData(event: String, category: String, action: String, label: String)
            : MutableMap<String, Any> {
        val data = HashMap<String, Any>()
        data.put(EVENT, event)
        data.put(CATEGORY, category)
        data.put(ACTION, action)
        data.put(LABEL, label)
        data.put(USER_ID, userSessionInterface.userId)
        return data
    }

    private fun getDefaultClickData(action: String, label: String): MutableMap<String, Any> {
        return getDefaultData(Event.EVENT_CLICK_PROFILE, Category.USER_PROFILE_PAGE, action, label)
    }

    private fun getDefaultViewData(action: String, label: String): MutableMap<String, Any> {
        return getDefaultData(Event.EVENT_VIEW_PROFILE, Category.USER_PROFILE_PAGE, action, label)
    }

    private fun setCustomDimensions(data: MutableMap<String, Any>, position: String, shopId: String,
                                    productId: String): MutableMap<String, Any> {
        data.put(CONTENT_POSITION, position)
        data.put(SHOP_ID, shopId)
        data.put(PRODUCT_ID, productId)
        return data
    }

    private fun singleOrMultiple(hasMultipleContent: Boolean): String {
        return if (hasMultipleContent) MULTIPLE else SINGLE
    }

    fun eventClickFollowing(profileId: String) {
        analyticTracker.sendEventTracking(
                getDefaultClickData(Action.CLICK_FOLLOWING, profileId)
        )
    }

    fun eventClickTag(hasMultipleContent: Boolean, activityId: String, activityType: String,
                      position: String, shopId: String, productId: String) {
        val data = getDefaultClickData(
                String.format(
                        Action.CLICK_TAG,
                        singleOrMultiple(hasMultipleContent),
                        activityType
                ),
                activityId
        )
        setCustomDimensions(data, position, shopId, productId)
        analyticTracker.sendEventTracking(data)
    }

    fun eventClickCard(hasMultipleContent: Boolean, activityId: String, activityType: String,
                       position: String, shopId: String, productId: String) {
        val data = getDefaultClickData(
                String.format(
                        Action.CLICK_CARD,
                        singleOrMultiple(hasMultipleContent),
                        activityType
                ),
                activityId
        )
        setCustomDimensions(data, position, shopId, productId)
        analyticTracker.sendEventTracking(data)
    }

    fun eventViewCard(hasMultipleContent: Boolean, activityId: String, activityType: String,
                      position: String, shopId: String, productId: String) {
        val data = getDefaultViewData(
                String.format(
                        Action.IMPRESSION_CARD,
                        singleOrMultiple(hasMultipleContent),
                        activityType
                ),
                activityId
        )
        setCustomDimensions(data, position, shopId, productId)
        analyticTracker.sendEventTracking(data)
    }

    fun eventClickBagikanProfile(profileId: String) {
        analyticTracker.sendEventTracking(
                getDefaultClickData(Action.CLICK_BAGIKAN_PROFILE, profileId)
        )
    }

    fun eventClickLike(hasMultipleContent: Boolean, activityId: String, activityType: String) {
        analyticTracker.sendEventTracking(
                getDefaultClickData(
                        String.format(
                                Action.CLICK_LIKE,
                                singleOrMultiple(hasMultipleContent),
                                activityType
                        ),
                        activityId
                )
        )
    }

    fun eventClickUnlike(hasMultipleContent: Boolean, activityId: String, activityType: String) {
        analyticTracker.sendEventTracking(
                getDefaultClickData(
                        String.format(
                                Action.CLICK_UNLIKE,
                                singleOrMultiple(hasMultipleContent),
                                activityType
                        ),
                        activityId
                )
        )
    }

    fun eventClickComment(hasMultipleContent: Boolean, activityId: String, activityType: String) {
        analyticTracker.sendEventTracking(
                getDefaultClickData(
                        String.format(
                                Action.CLICK_COMMENT,
                                singleOrMultiple(hasMultipleContent),
                                activityType
                        ),
                        activityId
                )
        )
    }
}