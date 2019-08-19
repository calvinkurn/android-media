package com.tokopedia.kol.feature.postdetail.view.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-06-28
 */
class KolPostDetailAnalytics @Inject constructor(private val userSession: UserSessionInterface) {


    fun eventClickLike(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_SOCIAL_COMMERCE,
                USER_PROFILE_SOCIALCOMMERCE_CONTENT_DETAIL,
                CLICK_LIKE,
                userId
        )
    }

    fun eventClickComment(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_SOCIAL_COMMERCE,
                USER_PROFILE_SOCIALCOMMERCE_CONTENT_DETAIL,
                CLICK_COMMENT,
                userId
        )
    }

    fun eventClickOtherPost(activityId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                getDefaultData(
                        "/user-profile-socialcommerce-content-detail",
                        "clickSocialCommerce",
                        "user profile socialcommerce - content detail",
                        "click post terkait",
                        activityId
                )
        )
    }

    fun eventImpressionOtherPost(activityId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                getDefaultData(
                        "/user-profile-socialcommerce-content-detail",
                        "viewSocialCommerce",
                        "user profile socialcommerce - content detail",
                        "impression post terkait",
                        activityId
                )
        )
    }

    private fun getDefaultData(screenName: String, event: String, category: String, action: String,
                               label: String): MutableMap<String, Any> {
        return mutableMapOf(SCREEN_NAME to screenName,
                EVENT to event,
                CATEGORY to category,
                ACTION to action,
                LABEL to label,
                USER_ID to userSession.userId)
    }

    companion object {
        private const val SCREEN_NAME = "screenName"
        private const val EVENT = "event"
        private const val CATEGORY = "eventCategory"
        private const val ACTION = "eventAction"
        private const val LABEL = "eventLabel"
        private const val USER_ID = "user_id"

        private const val EVENT_CLICK_SOCIAL_COMMERCE = "clickSocialCommerce"
        private const val USER_PROFILE_SOCIALCOMMERCE = "user profile socialcommerce"
        private const val USER_PROFILE_SOCIALCOMMERCE_CONTENT_DETAIL = USER_PROFILE_SOCIALCOMMERCE + "content detail"

        private const val CLICK_COMMENT = "click comment"
        private const val CLICK_LIKE = "click like"
    }

}
