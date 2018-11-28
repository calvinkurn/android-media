package com.tokopedia.profile.analytics

import android.app.Activity
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by milhamj on 10/10/18.
 */
class ProfileAnalytics @Inject constructor(private val analyticTracker: AnalyticTracker,
                                           private val userSessionInterface: UserSessionInterface) {
    companion object {
        private const val SCREEN_NAME = "screenName"
        private const val EVENT = "event"
        private const val CATEGORY = "eventCategory"
        private const val ACTION = "eventAction"
        private const val LABEL = "eventLabel"
        private const val USER_ID = "user_id"
        private const val CONTENT_POSITION = "content_position"
        private const val SINGLE = "single"
        private const val MULTIPLE = "multiple"
    }

    object Screen {
        const val PROFILE = "user profile page"
        const val MY_PROFILE = "my profile page"
    }

    object Event {
        const val EVENT_CLICK_TOP_PROFILE = "clickTopProfile"
        const val EVENT_CLICK_PROFILE = "clickProfile"
        const val EVENT_VIEW_PROFILE = "viewProfile"
    }

    object Category {
        const val KOL_TOP_PROFILE = "kol top profile"
        const val USER_PROFILE_PAGE = "user profile page"
        const val MY_PROFILE_PAGE = "my profile page"
    }

    object Action {
        const val CLICK_PROMPT = "click prompt"
        const val CLICK_FOLLOWING = "click following"
        const val CLICK_FOLLOW = "click follow"
        const val CLICK_UNFOLLOW = "click unfollow"
        const val CLICK_TAG = "click-%s-user-all-%s-tag"
        const val CLICK_CARD = "click-%s-user-all-%s-card"
        const val IMPRESSION_CARD = "impression-%s-user-all-%s"
        const val CLICK_BAGIKAN_PROFILE = "click bagikan profile ini"
        const val CLICK_LIKE = "click-%s-user-all-%s-like"
        const val CLICK_UNLIKE = "click-%s-user-all-%s-unlike"
        const val CLICK_COMMENT = "click-%s-user-all-%s-comment"
        const val CLICK_ADD_IMAGE = "click-%s-user-all-%s-add image"
        const val CLICK_ADD_RECCOMENDATION = "click tambah rekomendasi"
        const val CLICK_STATISTIC = "click affiliate statistic"
        const val CLICK_EMPTY_CTA = "cta byme"
    }

    object Label {
        const val GO_TO_FEED_FORMAT = "go to feed - %s"
    }

    private fun getDefaultData(screenName: String, event: String, category: String, action: String,
                               label: String): MutableMap<String, Any> {
        val data = HashMap<String, Any>()
        data.put(SCREEN_NAME, screenName)
        data.put(EVENT, event)
        data.put(CATEGORY, category)
        data.put(ACTION, action)
        data.put(LABEL, label)
        data.put(USER_ID, userSessionInterface.userId)
        return data
    }

    private fun setCustomDimensions(data: MutableMap<String, Any>, position: String)
            : MutableMap<String, Any> {
        data.put(CONTENT_POSITION, position)
        return data
    }

    private fun singleOrMultiple(hasMultipleContent: Boolean): String {
        return if (hasMultipleContent) MULTIPLE else SINGLE
    }

    fun sendScreen(activity: Activity, screenName: String) {
        analyticTracker.sendScreen(activity, screenName)
    }

    fun eventClickFollowing(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_PAGE else Category.USER_PROFILE_PAGE
        analyticTracker.sendEventTracking(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_PROFILE,
                        category,
                        Action.CLICK_FOLLOWING,
                        profileId
                )
        )
    }

    fun eventClickFollow(profileId: String) {
        analyticTracker.sendEventTracking(
                getDefaultData(
                        Screen.PROFILE,
                        Event.EVENT_CLICK_PROFILE,
                        Category.USER_PROFILE_PAGE,
                        Action.CLICK_FOLLOW,
                        profileId
                )
        )
    }

    fun eventClickUnfollow(profileId: String) {
        analyticTracker.sendEventTracking(
                getDefaultData(
                        Screen.PROFILE,
                        Event.EVENT_CLICK_PROFILE,
                        Category.USER_PROFILE_PAGE,
                        Action.CLICK_UNFOLLOW,
                        profileId
                )
        )
    }

    fun eventClickTag(isOwner: Boolean, hasMultipleContent: Boolean, activityId: String,
                      activityType: String, position: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_PAGE else Category.USER_PROFILE_PAGE
        val action = String.format(
                Action.CLICK_TAG,
                singleOrMultiple(hasMultipleContent),
                activityType
        )
        val data = getDefaultData(
                screen,
                Event.EVENT_CLICK_PROFILE,
                category,
                action,
                activityId
        )
        setCustomDimensions(data, position)
        analyticTracker.sendEventTracking(data)
    }

    fun eventClickCard(hasMultipleContent: Boolean, activityId: String, activityType: String,
                       position: String) {
        val action = String.format(
                Action.CLICK_CARD,
                singleOrMultiple(hasMultipleContent),
                activityType
        )
        val data = getDefaultData(
                Screen.PROFILE,
                Event.EVENT_CLICK_PROFILE,
                Category.USER_PROFILE_PAGE,
                action,
                activityId
        )
        setCustomDimensions(data, position)
        analyticTracker.sendEventTracking(data)
    }

    fun eventViewCard(isOwner: Boolean, hasMultipleContent: Boolean, activityId: String,
                      activityType: String, position: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_PAGE else Category.USER_PROFILE_PAGE
        val action = String.format(
                Action.IMPRESSION_CARD,
                singleOrMultiple(hasMultipleContent),
                activityType
        )
        val data = getDefaultData(
                screen,
                Event.EVENT_VIEW_PROFILE,
                category,
                action,
                activityId
        )
        setCustomDimensions(data, position)
        analyticTracker.sendEventTracking(data)
    }

    fun eventClickBagikanProfile(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_PAGE else Category.USER_PROFILE_PAGE
        analyticTracker.sendEventTracking(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_PROFILE,
                        category,
                        Action.CLICK_BAGIKAN_PROFILE,
                        profileId
                )
        )
    }

    fun eventClickLike(hasMultipleContent: Boolean, activityId: String, activityType: String) {
        val action = String.format(
                Action.CLICK_LIKE,
                singleOrMultiple(hasMultipleContent),
                activityType
        )
        analyticTracker.sendEventTracking(
                getDefaultData(
                        Screen.PROFILE,
                        Event.EVENT_CLICK_PROFILE,
                        Category.USER_PROFILE_PAGE,
                        action,
                        activityId
                )
        )
    }

    fun eventClickUnlike(hasMultipleContent: Boolean, activityId: String, activityType: String) {
        val action = String.format(
                Action.CLICK_UNLIKE,
                singleOrMultiple(hasMultipleContent),
                activityType
        )
        analyticTracker.sendEventTracking(
                getDefaultData(
                        Screen.PROFILE,
                        Event.EVENT_CLICK_PROFILE,
                        Category.USER_PROFILE_PAGE,
                        action,
                        activityId
                )
        )
    }

    fun eventClickComment(hasMultipleContent: Boolean, activityId: String, activityType: String) {
        val action = String.format(
                Action.CLICK_COMMENT,
                singleOrMultiple(hasMultipleContent),
                activityType
        )
        analyticTracker.sendEventTracking(
                getDefaultData(
                        Screen.PROFILE,
                        Event.EVENT_CLICK_PROFILE,
                        Category.USER_PROFILE_PAGE,
                        action,
                        activityId
                )
        )
    }

    fun eventClickAfterFollow(name: String) {
        analyticTracker.sendEventTracking(
                getDefaultData(
                        Screen.PROFILE,
                        Event.EVENT_CLICK_TOP_PROFILE,
                        Category.KOL_TOP_PROFILE,
                        Action.CLICK_PROMPT,
                        String.format(Label.GO_TO_FEED_FORMAT, name)
                )
        )
    }

    fun eventClickTambahGambar(hasMultipleContent: Boolean, activityId: String,
                               activityType: String) {
        val action = String.format(
                Action.CLICK_ADD_IMAGE,
                singleOrMultiple(hasMultipleContent),
                activityType
        )
        val data = getDefaultData(
                Screen.MY_PROFILE,
                Event.EVENT_CLICK_PROFILE,
                Category.MY_PROFILE_PAGE,
                action,
                activityId
        )
        analyticTracker.sendEventTracking(data)
    }

    fun eventClickTambahRekomendasi() {
        analyticTracker.sendEventTracking(
                getDefaultData(
                        Screen.MY_PROFILE,
                        Event.EVENT_CLICK_PROFILE,
                        Category.MY_PROFILE_PAGE,
                        Action.CLICK_ADD_RECCOMENDATION,
                        ""
                )
        )
    }

    fun eventClickStatistic() {
        analyticTracker.sendEventTracking(
                getDefaultData(
                        Screen.MY_PROFILE,
                        Event.EVENT_CLICK_PROFILE,
                        Category.MY_PROFILE_PAGE,
                        Action.CLICK_STATISTIC,
                        ""
                )
        )
    }

    fun eventClickEmptyStateCta() {
        analyticTracker.sendEventTracking(
                getDefaultData(
                        Screen.MY_PROFILE,
                        Event.EVENT_CLICK_PROFILE,
                        Category.MY_PROFILE_PAGE,
                        Action.CLICK_EMPTY_CTA,
                        ""
                )
        )
    }
}