package com.tokopedia.profile.analytics

import android.app.Activity
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 10/10/18.
 */
class ProfileAnalytics @Inject constructor(private val userSessionInterface: UserSessionInterface) {
    companion object {
        internal const val PROMO_VIEW = "promoView"
        internal const val PROMO_CLICK = "promoClick"

        private const val SCREEN_NAME = "screenName"
        private const val EVENT = "event"
        private const val CATEGORY = "eventCategory"
        private const val ACTION = "eventAction"
        private const val LABEL = "eventLabel"
        private const val USER_ID = "user_id"
        private const val CONTENT_POSITION = "content_position"
        private const val SINGLE = "single"
        private const val MULTIPLE = "multiple"
        private const val FORMAT_PROMOTION_NAME = "%s - %s - %s - %s";

        private const val EVENT_NAME = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
        private const val EVENT_ECOMMERCE = "ecommerce"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_ID_MOD = "userIdmodulo"
        private const val ACTION_IMPRESSION = "Impression"
        private const val ACTION_VIEW = "View"
        private const val ACTION_CLICK = "Click"
        private const val DASH = " - "
    }

    object Screen {
        const val PROFILE = "/user-profile-socialcommerce"
        const val MY_PROFILE = "/my-profile-socialcommerce"
    }

    object Event {
        const val EVENT_CLICK_TOP_PROFILE = "clickTopProfile"
        const val EVENT_CLICK_SOCIAL_COMMERCE = "clickSocialCommerce"
        const val EVENT_VIEW_PROFILE = "viewProfile"

    }

    object Category {
        const val KOL_TOP_PROFILE = "kol top profile"
        const val USER_PROFILE_SOCIALCOMMERCE = "user profile socialcommerce"
        const val MY_PROFILE_SOCIALCOMMERCE = "my profile socialcommerce"
        const val USER_PROFILE_PAGE = "user profile page"
        const val MY_PROFILE_PAGE = "my profile page"
    }

    object Action {
        const val CLICK_PROMPT = "click prompt"
        const val CLICK_FOLLOWING = "click following"
        const val CLICK_FOLLOW = "click follow"
        const val CLICK_UNFOLLOW = "click unfollow"
        const val CLICK_LIKE_ATTEMPT = "click like"
        const val CLICK_COMMENT_ATTEMPT = "click comment"
        const val CLICK_SHARE_THIS_PROFILE = "click bagikan profile ini"
        const val CLICK_SHARE_THIS_POST = "click share post ini"
        const val CLICK_SHARE_THIS_PROFILE_OPTION = "click option bagikan profil"
        const val CLICK_SHARE_THIS_POST_OPTION = "click option bagikan post"
        const val CLICK_SEE_DETAIL = "click lihat detail"
        const val CLICK_STATISTIC_PROFILE = "click statistics"
        const val CLICK_TAG = "click-%s-user-all-%s-tag"
        const val CLICK_CARD = "click-%s-user-all-%s-card"
        const val IMPRESSION_CARD = "impression-%s-user-all-%s"
        const val CLICK_BAGIKAN_PROFILE = "click bagikan profile ini"
        const val CLICK_LIKE = "click-%s-user-all-%s-like"
        const val CLICK_UNLIKE = "click-%s-user-all-%s-unlike"
        const val CLICK_COMMENT = "click-%s-user-all-%s-comment"
        const val CLICK_ADD_IMAGE = "click-%s-user-all-%s-add image"
        const val CLICK_ADD_RECCOMENDATION = "click tambah kurasi"
        const val CLICK_STATISTIC = "click affiliate statistic"
        const val CLICK_EMPTY_CTA = "cta byme"
    }

    object Label {
        const val GO_TO_FEED_FORMAT = "go to feed - %s"
    }

    object Element {
        const val AVATAR = "avatar"
        const val IMAGE = "image"
        const val TAG = "tag"
        const val SHARE = "share"
        const val FOLLOW = "follow"
        const val OPTION = "option "
        const val VIDEO = "video"
        const val PRODUCT = "product"

    }

    private fun getDefaultData(screenName: String, event: String, category: String, action: String,
                               label: String): MutableMap<String, Any> {
        return mutableMapOf(SCREEN_NAME to screenName,
                EVENT to event,
                CATEGORY to category,
                ACTION to action,
                LABEL to label,
                USER_ID to userSessionInterface.userId)
    }

    private fun setCustomDimensions(data: MutableMap<String, Any>, position: String)
            : MutableMap<String, Any> {
        data[CONTENT_POSITION] = position
        return data
    }

    private fun singleOrMultiple(hasMultipleContent: Boolean): String {
        return if (hasMultipleContent) MULTIPLE else SINGLE
    }

    private fun singleOrMultiple(totalContent: Int): String {
        return if (totalContent == 1)
            SINGLE
        else
            MULTIPLE
    }

    fun sendScreen(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun eventClickFollowing(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        Action.CLICK_FOLLOWING,
                        profileId
                )
        )
    }

    fun eventClickFollow(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        Action.CLICK_FOLLOW,
                        profileId
                )
        )
    }

    fun eventClickUnfollow(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        Action.CLICK_UNFOLLOW,
                        profileId
                )
        )
    }

    fun eventClickLike(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        Action.CLICK_LIKE_ATTEMPT,
                        profileId
                )
        )
    }

    fun eventClickComment(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        Action.CLICK_COMMENT_ATTEMPT,
                        profileId
                )
        )
    }

    fun eventClickFollowFooter(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        "click follow - sticky bottom bar",
                        profileId
                )
        )
    }

    fun eventClickUnfollowFooter(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        "click unfollow - sticky bottom bar",
                        profileId
                )
        )
    }

    fun eventClickShareProfileIni(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        Action.CLICK_SHARE_THIS_PROFILE,
                        profileId
                )
        )
    }

    fun eventClickSharePostIni(isOwner: Boolean, profileId: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        Action.CLICK_SHARE_THIS_POST,
                        profileId
                )
        )
    }

    fun eventClickShareProfileOpsiIni(isOwner: Boolean, profileId: String, source: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        Action.CLICK_SHARE_THIS_PROFILE_OPTION + " - " + source,
                        profileId
                )
        )
    }

    fun eventClickSharePostOpsiIni(isOwner: Boolean, profileId: String, source: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        screen,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        category,
                        Action.CLICK_SHARE_THIS_POST_OPTION + " - " + source,
                        profileId
                )
        )
    }

    fun eventClickStatistic(profileId: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        Screen.MY_PROFILE,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        Category.MY_PROFILE_SOCIALCOMMERCE,
                        Action.CLICK_STATISTIC_PROFILE,
                        profileId
                )
        )
    }


    fun eventClickTag(isOwner: Boolean, hasMultipleContent: Boolean, activityId: String,
                      activityType: String, position: String) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        val action = String.format(
                Action.CLICK_TAG,
                singleOrMultiple(hasMultipleContent),
                activityType
        )
        val data = getDefaultData(
                screen,
                Event.EVENT_CLICK_SOCIAL_COMMERCE,
                category,
                action,
                activityId
        )
        setCustomDimensions(data, position)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }


    fun eventClickCard(templateType: String, activityName: String,
                           trackingType: String, mediaType: String, tagsType: String,
                           redirectUrl: String, element: String, totalContent: Int,
                           postId: Int, position: Int, contentPosition: String, userId: Int, isOwner: Boolean) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        val name = if (isOwner) Category.MY_PROFILE_PAGE else Category.USER_PROFILE_PAGE
        val promotionList = ArrayList<ProfileEnhancedTracking.Promotion>()
        promotionList.add(ProfileEnhancedTracking.Promotion(
                postId,
                String.format(FORMAT_PROMOTION_NAME,
                        name,
                        activityName,
                        tagsType,
                        singleOrMultiple(totalContent)),
                redirectUrl + DASH + contentPosition,
                position,
                "",
                0,
                "")
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getEventEcommerceClick(
                        screen,
                        category,
                        ACTION_CLICK.toLowerCase() + DASH + templateType + DASH
                                + activityName + DASH + trackingType + DASH + element,
                        mediaType,
                        promotionList,
                        userId
                )
        )
    }


    fun eventViewCard(templateType: String, activityName: String,
                      trackingType: String, mediaType: String, tagsType: String,
                      redirectUrl: String,
                      totalContent: Int, postId: Int, position: Int, userId: Int, isOwner: Boolean) {
        val screen = if (isOwner) Screen.MY_PROFILE else Screen.PROFILE
        val category = if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE
        val name = if (isOwner) Category.MY_PROFILE_PAGE else Category.USER_PROFILE_PAGE
        val promotionList = ArrayList<ProfileEnhancedTracking.Promotion>()
        promotionList.add(ProfileEnhancedTracking.Promotion(
                postId,
                String.format(FORMAT_PROMOTION_NAME,
                        name,
                        activityName,
                        tagsType,
                        singleOrMultiple(totalContent)),
                String.format("%s - 0", redirectUrl),
                position,
                "",
                0,
                "")
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        screen,
                        category,
                        ACTION_IMPRESSION.toLowerCase() + DASH + templateType + DASH
                                + activityName + DASH + trackingType,
                        mediaType,
                        promotionList,
                        userId
                )
        )
    }

    fun eventClickLike(hasMultipleContent: Boolean, activityId: String, activityType: String) {
        val action = String.format(
                Action.CLICK_LIKE,
                singleOrMultiple(hasMultipleContent),
                activityType
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        Screen.PROFILE,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        Category.USER_PROFILE_SOCIALCOMMERCE,
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
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        Screen.PROFILE,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        Category.USER_PROFILE_SOCIALCOMMERCE,
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
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        Screen.PROFILE,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        Category.USER_PROFILE_SOCIALCOMMERCE,
                        action,
                        activityId
                )
        )
    }

    fun eventClickAfterFollow(name: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
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
                Event.EVENT_CLICK_SOCIAL_COMMERCE,
                Category.MY_PROFILE_SOCIALCOMMERCE,
                action,
                activityId
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }

    fun eventClickTambahRekomendasi() {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        Screen.MY_PROFILE,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        Category.MY_PROFILE_SOCIALCOMMERCE,
                        Action.CLICK_ADD_RECCOMENDATION,
                        ""
                )
        )
    }

    fun eventClickEmptyStateCta() {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDefaultData(
                        Screen.MY_PROFILE,
                        Event.EVENT_CLICK_SOCIAL_COMMERCE,
                        Category.MY_PROFILE_SOCIALCOMMERCE,
                        Action.CLICK_EMPTY_CTA,
                        ""
                )
        )
    }

    private fun getEventEcommerceView(screenName: String,
                                      category: String,
                                      action: String,
                                      label: String,
                                      promotions: List<ProfileEnhancedTracking.Promotion>,
                                      userId: Int): Map<String, Any> {
        return DataLayer.mapOf(
                SCREEN_NAME, screenName,
                EVENT_NAME, PROMO_VIEW,
                EVENT_CATEGORY, category,
                EVENT_ACTION, action,
                EVENT_LABEL, label,
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % 50,
                EVENT_ECOMMERCE, ProfileEnhancedTracking.Ecommerce.getEcommerceView(promotions)
        )
    }

    private fun getEventEcommerceClick(screenName: String,
                                       category: String,
                                       action: String,
                                       label: String,
                                       promotions: List<ProfileEnhancedTracking.Promotion>,
                                       userId: Int): Map<String, Any> {
        return DataLayer.mapOf(
                SCREEN_NAME, screenName,
                EVENT_NAME, PROMO_CLICK,
                EVENT_CATEGORY, category,
                EVENT_ACTION, action,
                EVENT_LABEL, label,
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % 50,
                EVENT_ECOMMERCE, ProfileEnhancedTracking.Ecommerce.getEcommerceClick(promotions)
        )
    }
}