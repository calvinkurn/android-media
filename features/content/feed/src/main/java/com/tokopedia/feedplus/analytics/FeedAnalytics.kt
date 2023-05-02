package com.tokopedia.feedplus.analytics

import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 14/04/23
 */
class FeedAnalytics @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) {

    companion object {
        const val KEY_EVENT_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT_EVENT = "businessUnit"
        const val KEY_CURRENT_SITE_EVENT = "currentSite"
        const val KEY_TRACKER_ID = "trackerId"
        const val KEY_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        const val KEY_SCREEN_NAME = "screenName"

        const val BUSINESS_UNIT_CONTENT = "content"
        const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
        const val CATEGORY_UNIFIED_FEED = "unified feed"

        private const val POST_TYPE_ASGC = "asgc"
        private const val POST_TYPE_SGC = "sgc"
        private const val POST_TYPE_UGC = "ugc"
        private const val POST_TYPE_ASGC_RECOM = "asgc recom"
        private const val POST_TYPE_TOPADS = "topads"
        private const val POST_TYPE_UGC_RECOM = "ugc recom"
        private const val POST_TYPE_SGC_RECOM = "sgc recom"

        private const val CONTENT_TYPE_SHORT = "play short video"
        private const val CONTENT_TYPE_LIVE_PREVIEW = "play live preview"
        private const val CONTENT_TYPE_LIVE_VOD = "play vod after live "

        private const val AUTHOR_TOKOPEDIA = 1
        private const val AUTHOR_SELLER = 2
        private const val AUTHOR_USER = 3

        const val ENTRY_POINT_NAV_BUTTON = "nav button"
        const val ENTRY_POINT_SHARE_LINK = "share link"
        const val ENTRY_POINT_PUSH_NOTIF = "push notif"

        const val UNIFIED_FEED_WATCH_VIDEO_POST = "/unified feed - watch video post"
    }

    private object Event {
        const val SELECT_CONTENT = "select_content"
        const val VIEW_ITEM = "view_item"
        const val VIEW_CONTENT_IRIS = "viewContentIris"
        const val CLICK_CONTENT = "clickContent"
        const val OPEN_SCREEN = "openScreen"
    }

    private object Category {
        const val UNIFIED_FEED = "unified feed"
    }

    private object Action {
        const val VIEW_POST = "view - post"
        const val CLICK_PAUSE_VIDEO = "click - screen pause video"
        const val CLICK_HOLD_SEEKER_BAR_VIDEO = "click - tap hold seeker bar video"
        const val SWIPE_UP_DOWN_CONTENT = "swipe - up down content"
        const val SWIPE_RIGHT_LEFT_MULTIPLE_POST = "swipe - right left multiple post"
        const val CLICK_CTA_BUTTON_CAMPAIGN = "click - cta button campaign"
        const val CLICK_REMIND_ME_BUTTON = "click - ingatkan saya button"
        const val CLICK_ACTIVE_REMIND_ME_BUTTON = "click - pengingat aktif button"
        const val CLICK_LIKE_BUTTON = "click - like button"
        const val CLICK_DOUBLE_LIKE_BUTTON = "click - double click like"
        const val CLICK_COMMENT_BUTTON = "click - comment button"
        const val CLICK_CLOSE_COMMENT_BUTTON = "click - close comment bottomsheet"
        const val CLICK_REPLY_COMMENT_BUTTON = "click - reply button"
        const val CLICK_AUTHOR_COMMENT_NAME_BUTTON = "click - name commenter"
        const val CLICK_AUTHOR_COMMENT_PICTURE_BUTTON = "click - profile picture commenter"
        const val VIEW_X_REPLIES = "view - lihat x balasan"
        const val CLICK_X_REPLIES = "click - lihat x balasan"
        const val HIDE_COMMENT_REPLIES = "click - sembunyikan comment"
        const val CLICK_COMMENT_TEXT_BOX = "click - text box"
        const val CLICK_SEND_MAIN_COMMENT = "click - send main comment"
        const val CLICK_SEND_REPLY_COMMENT = "click - send reply comment"
        const val CLICK_LONG_PRESS_SLIDE_COMMENT = "click - long press slide comment"
        const val CLICK_DELETE_COMMENT = "click - hapus comment"
        const val CLICK_REPORT_COMMENT = "click - laporkan comment"
        const val CLICK_REASON_REPORT_COMMENT = "click - reason laporkan comment"
    }

    private object Promotion {

    }

    fun eventClickPauseVideo(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PAUSE_VIDEO,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41568"
            )
        )
    }

    fun eventHoldSeekBarVideo(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_HOLD_SEEKER_BAR_VIDEO,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41569"
            )
        )
    }

    fun eventWatchVideoPost() {
        val trackerData =
            generateGeneralTrackerData(
                Event.OPEN_SCREEN,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_HOLD_SEEKER_BAR_VIDEO,
                "", "41570"
            ).toMutableMap()
        trackerData[KEY_IS_LOGGED_IN_STATUS] = userSession.isLoggedIn
        trackerData[KEY_SCREEN_NAME] = UNIFIED_FEED_WATCH_VIDEO_POST

        sendEventTracker(trackerData)
    }

    fun eventSwipeUpDownContent(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.SWIPE_UP_DOWN_CONTENT,
                "${getPrefix(trackerData.tabType)} - ${trackerData.entryPoint}",
                "41571"
            )
        )
    }

    fun eventSwipeLeftRightMultiplePost(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.SWIPE_RIGHT_LEFT_MULTIPLE_POST,
                "${getPrefix(trackerData.tabType)} - ${trackerData.entryPoint}",
                "41572"
            )
        )
    }

    fun eventClickCampaignRibbon(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.SELECT_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CTA_BUTTON_CAMPAIGN,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41574"
            )
        )
        // TODO : IMPLEMENT EE
        /*
            "promotions": [
                {
                  "creative_name": "campaign post in unified feed",
                  "creative_slot": "{this is integer}",
                  "item_id": "{activity_id}",
                  "item_name": "{campaign name}"
                }
              ],
         */
    }

    fun eventClickRemindMe(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_REMIND_ME_BUTTON,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41575"
            )
        )
    }

    fun eventClickActiveRemindMe(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_ACTIVE_REMIND_ME_BUTTON,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41576"
            )
        )
    }

    fun eventClickLikeButton(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_LIKE_BUTTON,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41577"
            )
        )
    }

    fun eventDoubleClickLikeButton(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_DOUBLE_LIKE_BUTTON,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41578"
            )
        )
    }

    // TODO : Need to revisit all Comment Trackers after implement comment bottomsheet
    fun eventClickCommentButton(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_COMMENT_BUTTON,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41579"
            )
        )
    }

    fun eventClickCloseComment(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CLOSE_COMMENT_BUTTON,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41580"
            )
        )
    }

    fun eventClickReplyButton (
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_REPLY_COMMENT_BUTTON,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41581"
            )
        )
    }

    fun eventClickCommentAuthorName(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_AUTHOR_COMMENT_NAME_BUTTON,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41582"
            )
        )
    }

    fun eventClickCommentAuthorPicture(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_AUTHOR_COMMENT_PICTURE_BUTTON,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41583"
            )
        )
    }

    fun eventImpressionViewXReplies(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.VIEW_CONTENT_IRIS,
                CATEGORY_UNIFIED_FEED,
                Action.VIEW_X_REPLIES,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41584"
            )
        )
    }

    fun eventClickViewXReplies(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_X_REPLIES,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41585"
            )
        )
    }

    fun eventClickHideReplies(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.HIDE_COMMENT_REPLIES,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41586"
            )
        )
    }

    fun eventClickCommentTextBox(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_COMMENT_TEXT_BOX,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41587"
            )
        )
    }

    fun eventClickSendMainComment(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_SEND_MAIN_COMMENT,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41588"
            )
        )
    }

    fun eventClickSendReplyComment(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_SEND_REPLY_COMMENT,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41589"
            )
        )
    }

    fun eventLongPressSlideComment(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_LONG_PRESS_SLIDE_COMMENT,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41590"
            )
        )
    }

    fun eventDeleteComment(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_DELETE_COMMENT,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41591"
            )
        )
    }

    fun eventReportComment(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_REPORT_COMMENT,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41592"
            )
        )
    }

    fun eventReasonReportComment(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_REASON_REPORT_COMMENT,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                    getPostType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.authorType,
                        trackerData.isFollowing
                    )
                } - ${
                    getContentType(
                        trackerData.typename,
                        trackerData.type,
                        trackerData.mediaType
                    )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
                "41593"
            )
        )
    }

    private fun sendEventTracker(params: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(params)
    }

    private fun getPrefix(tabType: String) =
        when (tabType) {
            FeedBaseFragment.TAB_TYPE_FOR_YOU -> "untuk kamu tab"
            FeedBaseFragment.TAB_TYPE_FOLLOWING -> "following tab"
            else -> ""
        }

    private fun getPostType(typename: String, type: String, authorType: Int, isFollowing: Boolean) =
        when {
            typename == FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER && type == FeedXCard.TYPE_FEED_TOP_ADS -> POST_TYPE_TOPADS
            typename == FeedXCard.TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT && isFollowing -> POST_TYPE_ASGC
            typename == FeedXCard.TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT -> POST_TYPE_ASGC_RECOM
            authorType == AUTHOR_SELLER && isFollowing -> POST_TYPE_SGC
            authorType == AUTHOR_SELLER -> POST_TYPE_SGC_RECOM
            authorType == AUTHOR_USER && isFollowing -> POST_TYPE_UGC
            authorType == AUTHOR_USER -> POST_TYPE_UGC_RECOM
            authorType == AUTHOR_TOKOPEDIA && isFollowing -> POST_TYPE_ASGC
            authorType == AUTHOR_TOKOPEDIA -> POST_TYPE_ASGC_RECOM
            else -> ""
        }

    private fun getContentType(typename: String, type: String, mediaType: String) = when {
        typename == FeedXCard.TYPE_FEED_X_CARD_PLAY && type == FeedXCard.TYPE_FEED_PLAY_CHANNEL -> CONTENT_TYPE_LIVE_VOD
        typename == FeedXCard.TYPE_FEED_X_CARD_PLAY && type == FeedXCard.TYPE_FEED_PLAY_SHORT_VIDEO -> CONTENT_TYPE_SHORT
        typename == FeedXCard.TYPE_FEED_X_CARD_PLAY && type == FeedXCard.TYPE_FEED_PLAY_LIVE -> CONTENT_TYPE_LIVE_PREVIEW
        else -> mediaType
    }

    private fun generateGeneralTrackerData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ): Map<String, Any> = mapOf(
        EVENT to eventName,
        EVENT_CATEGORY to eventCategory,
        EVENT_ACTION to eventAction,
        EVENT_LABEL to eventLabel,
        KEY_EVENT_USER_ID to userSession.userId,
        KEY_BUSINESS_UNIT_EVENT to BUSINESS_UNIT_CONTENT,
        KEY_CURRENT_SITE_EVENT to CURRENT_SITE_MARKETPLACE,
        KEY_TRACKER_ID to trackerId
    )

}
