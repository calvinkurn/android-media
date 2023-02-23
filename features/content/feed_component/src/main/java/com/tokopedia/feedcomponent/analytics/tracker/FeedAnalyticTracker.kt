package com.tokopedia.feedcomponent.analytics.tracker

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.createpost.common.view.viewmodel.MediaType
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.CLICK
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.CLICK_ADD_TO_CART
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.CLICK_FULL_SCREEN
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.CLICK_LANJUT_MENONTON
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.CLICK_SEK_SEKARANG
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.FORMAT_FIVE_PARAM
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.FORMAT_FOUR_PARAM
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.FORMAT_SIX_PARAM
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.FORMAT_THREE_PARAM
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.FORMAT_TWO_PARAM
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CATEGORY_FEED_TIMELINE
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CATEGORY_FEED_TIMELINE_BOTTOMSHEET
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CATEGORY_FEED_TIMELINE_COMMENT
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CATEGORY_FEED_TIMELINE_FEED_DETAIL
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CATEGORY_FEED_TIMELINE_MENU
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CONTENT_FEED_TIMELINE
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.ADD_TO_CART
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.CLICK_CONTENT
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.CLICK_FEED
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.CLICK_PG
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.CONTENT
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.OPEN_SCREEN
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.PRODUCT_CLICK
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.PRODUCT_VIEW
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.PROMO_VIEW
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Screen.MARKETPLACE
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Screen.SCREEN_DIMENSION_IS_FEED_EMPTY
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Screen.SCREEN_DIMENSION_IS_LOGGED_IN_STATUS
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 2019-08-28.
 */
class FeedAnalyticTracker
@Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSessionInterface: UserSessionInterface
) {

    private companion object {
        private const val USER_ID = "user_id"
        const val ECOMMERCE = "ecommerce"
        const val TRACKER_ID = "trackerId"
        const val DATA = "data"
        const val PRODUCTS = "products"
        const val ACTION_FIELD = "actionField"

        const val PROMOTIONS = "promotions"
        const val LIST = "list"
        const val CURRENCY_CODE = "currencyCode"
        const val CURRENCY_CODE_IDR = "IDR"

        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_SCREEN_NAME = "screenName"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_BUSINESS_UNIT_EVENT = "businessUnit"
        private const val KEY_CURRENT_SITE_EVENT = "currentSite"
        private const val KEY_EVENT_USER_ID = "userId"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val SGC_IMAGE = "sgc image"
        private const val LONG_VIDEO_SGC = "sgc long video"
        private const val TYPE_LONG_VIDEO: String = "long-video"
        private const val TYPE_VIDEO: String = "video"
        private const val TYPE_IMAGE: String = "image"
        private const val LONG_VIDEO_SGC_RECOM = "sgc long video recom"
        private const val SGC_VOD_PLAY = "sgc play long video"
        private const val SGC_VOD_PLAY_RECOM = "sgc play long video recom"
        private const val SGC_IMAGE_RECOM = "sgc image recom"
        private const val UGC_VOD_PLAY = "ugc play long video"
        private const val UGC_VOD_PLAY_RECOM = "ugc play long video recom"
        private const val ASGC = "asgc"
        private const val SGC_VIDEO = "sgc video"
        private const val VIDEO = "video"
        private const val Upcoming = "upcoming"
        private const val ASGC_RECOM = "asgc recom"
        private const val TOPADS = "topads"
        private const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT = "FeedXCardProductsHighlight"
        private const val TYPE_FEED_X_CARD_PLAY = "FeedXCardPlay"
        private const val TYPE_FEED_X_CARD_POST = "FeedXCardPost"
        private const val TYPE_FEED_X_CARD_PRODUCT_TOPADS = "topads_headline_new"
        private const val UGC_AUTHOR_TYPE = "3"
    }

    private object Event {
        const val CLICK_FEED = "clickFeed"
        const val CLICK_CONTENT = "clickContent"
        const val CLICK_SOCIAL_COMMERCE = "clickSocialCommerce"
        const val OPEN_SCREEN = "openScreen"
        const val ADD_TO_CART = "addToCart"
        const val CONTENT = "content"
        const val PROMO_CLICK = "promoClick"
        const val CLICK_PG = "clickPG"
        const val PROMO_VIEW = "promoView"
        const val PRODUCT_CLICK = "productClick"
        const val PRODUCT_VIEW = "productView"
        const val ACTION_FIELD = "actionField"

        const val ADD = "add"
    }

    private object Category {

        // trending
        const val CONTENT_EXPLORE_TRENDING = "explore page - trending"

        const val CONTENT_FEED_TIMELINE = "content feed timeline"
        const val FEED_DETAIL_PAGE = "feed detail page"

        const val MY_PROFILE_SOCIALCOMMERCE = "my profile socialcommerce"
        const val USER_PROFILE_SOCIALCOMMERCE = "user profile socialcommerce"

        const val CONTENT_FEED_SHOP_PAGE = "content feed - shop page"
        const val CONTENT_HASHTAG = "content hashtag"
        const val CONTENT_DETAIL_PAGE_HASHTAG = "content detail page - hashtag page"
        const val CATEGORY_FEED_TIMELINE = "content feed timeline"
        const val CATEGORY_FEED_TIMELINE_BOTTOMSHEET = "content feed timeline - bottom sheet"
        const val CATEGORY_FEED_TIMELINE_COMMENT = "content feed timeline - comment"
        const val CATEGORY_FEED_TIMELINE_MENU = "content feed timeline - three dots page"
        const val CATEGORY_FEED_TIMELINE_FEED_DETAIL = "content feed timeline - product detail"
    }

    private object Action {
        const val CLICK = "click"
        const val CLICK_BUY = "click beli"
        const val CLICK_SEE = "click lihat"
        const val CLICK_MEDIA = "click media"
        const val CLICK_SEE_ALL = "click lihat semua"
        const val CLICK_HASHTAG = "click hashtag"
        const val CLICK_READ_MORE = "click read more"
        const val CLICK_POST = "click post"
        const val CLICK_POST_HASHTAG = "click - post"
        const val CLICK_AVATAR = "click avatar"
        const val CLICK_FEED_PRODUCT_DETAIL = "click - shop"
        const val CLICK_SEK_SEKARANG = "cek sekarang"
        const val CLICK_LANJUT_MENONTON = "lanjut menonton"
        const val CLICK_FULL_SCREEN = "full screen"
        const val CLICK_ADD_TO_CART = "add to cart"

        const val IMPRESSION_POST = "impression post"

        const val PARAM_ACTION_LOGIN = "login"
        const val PARAM_ACTION_NONLOGIN = "nonlogin"
        const val ACTION_FEED_RECOM_USER = "avatar - %s recommendation - %s"
        const val ACTION_CLICK_MEDIAPREVIEW_AVATAR = "click - %s - media preview - %s"
        const val ACTION_CLICK_TOPADS_PROMOTED = "click - shop - topads shop recommendation - %s"
        const val FORMAT_TWO_PARAM = "%s - %s"
        const val FORMAT_THREE_PARAM = "%s - %s - %s"
        const val FORMAT_FOUR_PARAM = "%s - %s - %s - %s"
        const val FORMAT_FIVE_PARAM = "%s - %s - %s - %s - %s"
        const val FORMAT_SIX_PARAM = "%s - %s - %s - %s - %s - %s"
    }

    fun getEvent(isCampaign: Boolean, authorType: String = "", isAsgcRecomm: Boolean = false) =
        if (authorType == UGC_AUTHOR_TYPE || authorType == FollowCta.AUTHOR_UGC || isAsgcRecomm) CLICK_CONTENT else if (isCampaign) CLICK_PG else CLICK_FEED

    object Screen {
        const val FEED = "/feed"
        const val FEED_SHOP = "/shop-feed"
        const val MEDIA_PREVIEW = "$FEED/media-preview"
        const val TRENDING = "$FEED/trending-tab"
        const val HASHTAG = "$FEED/hashtag"
        const val HASHTAG_POST_LIST = "/hashtag page - post list"
        const val USER_PROFILE_PAGE = "/user profile page"
        const val USER_PROFILE_PAGE_DETAIL = "$USER_PROFILE_PAGE detail"
        const val MARKETPLACE = "tokopediamarketplace"

        const val SCREEN_DIMENSION_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        const val SCREEN_DIMENSION_IS_FEED_EMPTY = "isFeedEmpty"
    }

    private object Promotion {
        const val ID = "id"
        const val NAME = "name"
        const val CREATIVE = "creative"
        const val POSITION = "position"
        const val CREATIVE_URL = "creative_url"
        const val CATEGORY = "category"
        const val PROMO_ID = "promo_id"
        const val PROMO_CODE = "promo_code"
    }

    private object Product {
        const val ID = "id"
        const val NAME = "name"
        const val PRICE = "price"
        const val QTY = "quantity"
        const val SHOP_ID = "shop_id"
        const val CATEGORY_ID = "category_id"
        const val DIMENSION_40 = "dimension40"
        const val DIMENSION_39 = "dimension39"
        const val SHOP_NAME = "shop_name"
        const val SHOP_TYPE = "shop_type"
        const val MEDIA_PREVIEW = "/feed media preview - {role} post"
        const val MEDIA_PREVIEW_TAG = "{role}"
        const val CURRENCY_CODE = "currencyCode"
        const val CURRENCY_CODE_IDR = "IDR"
        const val BRAND = "brand"
        const val CATEGORY = "category"
        const val VARIANT = "variant"
        const val LIST = "list"
        const val POSITION = "position"
        const val ACTION_FIELD = "actionField"
        const val CLICK = "click"
        const val IMPRESSIONS = "impressions"
        const val ITEMS = "items"
        const val INDEX = "index"
    }

    private fun getPostType(
        type: String,
        isFollowed: Boolean,
        mediaType: String = "image",
        authorType: String
    ): String {
        return if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !isFollowed) {
            ASGC_RECOM
        } else if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && isFollowed) {
            ASGC
        } else if (type == TYPE_FEED_X_CARD_PLAY && !isFollowed && (authorType == UGC_AUTHOR_TYPE || authorType == FollowCta.AUTHOR_UGC)) {
            UGC_VOD_PLAY_RECOM
        } else if (type == TYPE_FEED_X_CARD_PLAY && isFollowed && authorType == UGC_AUTHOR_TYPE) {
            UGC_VOD_PLAY
        } else if (type == TYPE_FEED_X_CARD_PLAY && !isFollowed) {
            SGC_VOD_PLAY_RECOM
        } else if (type == TYPE_FEED_X_CARD_PLAY && isFollowed) {
            SGC_VOD_PLAY
        } else if (type == TYPE_FEED_X_CARD_PRODUCT_TOPADS) {
            TOPADS
        } else if (type == TYPE_FEED_X_CARD_POST && isFollowed && isLongVideo(mediaType)) {
            LONG_VIDEO_SGC
        } else if (type == TYPE_FEED_X_CARD_POST && !isFollowed && isLongVideo(mediaType)) {
            LONG_VIDEO_SGC_RECOM
        } else if (type == TYPE_FEED_X_CARD_POST && isVideo(mediaType)) {
            SGC_VIDEO
        } else if (type != TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !isFollowed) {
            SGC_IMAGE_RECOM
        } else {
            SGC_IMAGE
        }
    }

    private fun isVideo(mediaType: String): Boolean = mediaType == SGC_VIDEO || mediaType == VIDEO
    private fun isLongVideo(mediaType: String): Boolean = mediaType == TYPE_LONG_VIDEO

    //    https://docs.google.com/spreadsheets/d/1yFbEMzRj0_VdeVN7KfZIHZlv71uvX38XjfcYw7nPB3c/edit#gid=1359526861
    //    screenshot 19
    // https://mynakama.tokopedia.com/datatracker/requestdetail/view/942
    // 1
    fun eventClickFeedAvatar(
        feedTrackerData: FeedTrackerData,
        isCaption: Boolean
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType
        val isAsgcRecomm = feedTrackerData.postType == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !feedTrackerData.isFollowed

        val actionField = if (isCaption) {
            "shop name below"
        } else {
            "shop"
        }

        val finalLabel =
            if (feedTrackerData.campaignStatus.isNotEmpty()) {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.campaignStatus,
                    feedTrackerData.hasVoucher
                )
            } else {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            }
        var map = mapOf(
            KEY_EVENT to getEvent(feedTrackerData.campaignStatus.isNotEmpty(), isAsgcRecomm = isAsgcRecomm),
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                actionField,
                getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
            ),
            finalLabel,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        if (feedTrackerData.trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to feedTrackerData.trackerId)
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun evenClickMenu(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType
        val isAsgcRecomm = feedTrackerData.postType == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !feedTrackerData.isFollowed
        val finalLabel =
            if (feedTrackerData.campaignStatus.isNotEmpty()) {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.campaignStatus,
                    feedTrackerData.hasVoucher
                )
            } else {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            }
        var map = mapOf(
            KEY_EVENT to getEvent(feedTrackerData.campaignStatus.isNotEmpty(), isAsgcRecomm = isAsgcRecomm),
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "three dots",
                getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
            ),
            finalLabel,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        if (feedTrackerData.trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to feedTrackerData.trackerId)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickBottomSheetMenu(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type: String = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType
        val trackerId = feedTrackerData.trackerId
        val productId = feedTrackerData.productId
        val category =
            if (feedTrackerData.isProductDetailPage) CATEGORY_FEED_TIMELINE_FEED_DETAIL else CATEGORY_FEED_TIMELINE_BOTTOMSHEET
        val finalLabel =
            if (feedTrackerData.campaignStatus.isNotEmpty() && isFollowed) {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_SIX_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.campaignStatus,
                    productId,
                    feedTrackerData.hasVoucher
                )
            } else {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    productId,
                    feedTrackerData.hasVoucher
                )
            }
        var map = mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to category,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "three dots",
                getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
            ),
            finalLabel,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        if (trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to trackerId)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventTagClicked(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val mediaType = feedTrackerData.mediaType
        val isFollowed = feedTrackerData.isFollowed
        val type = feedTrackerData.postType
        val isAsgcRecomm = feedTrackerData.postType == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !feedTrackerData.isFollowed

        val asgcCampaignTrackerId =
            if (feedTrackerData.campaignStatus.isNotEmpty()) {
                if (isFollowed) "17983" else "13432"
            } else {
                if (!isFollowed) "40068" else ""
            }
        val typeAction = if (mediaType == MediaType.VIDEO && type != TYPE_FEED_X_CARD_PLAY) {
            "product tag"
        } else {
            "lihat produk"
        }

        val finalLabel =
            if (feedTrackerData.campaignStatus.isNotEmpty()) {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    feedTrackerData.shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.campaignStatus,
                    feedTrackerData.hasVoucher
                )
            } else {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    feedTrackerData.shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            }

        var map = mapOf(
            KEY_EVENT to getEvent(feedTrackerData.campaignStatus.isNotEmpty(), isAsgcRecomm = isAsgcRecomm),
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                typeAction,
                getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
            ),
            finalLabel,
            KEY_BUSINESS_UNIT_EVENT to "content",
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        if (feedTrackerData.campaignStatus.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to asgcCampaignTrackerId)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventImageClicked(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "image",
                getPostType(type, isFollowed, authorType = feedTrackerData.authorType)
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_FOUR_PARAM,
                activityId,
                shopId,
                feedTrackerData.contentSlotValue,
                feedTrackerData.hasVoucher
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventAddToWishlistClicked(
        activityId: String,
        productId: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        mediaType: String,
        trackerId: String = "",
        campaignStatus: String = "",
        contentScore: String,
        hasVoucher: Boolean = false,
        authorType: String = ""
    ) {
        val finalLabel = if (campaignStatus.isNotEmpty() && isFollowed) {
            KEY_EVENT_LABEL to String.format(
                FORMAT_SIX_PARAM,
                activityId,
                shopId,
                contentScore,
                campaignStatus,
                productId,
                hasVoucher
            )
        } else {
            if (type == TYPE_FEED_X_CARD_PLAY || mediaType == TYPE_LONG_VIDEO) {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    contentScore,
                    hasVoucher
                )
            } else {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    contentScore,
                    productId,
                    hasVoucher
                )
            }
        }
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_BOTTOMSHEET, campaignStatus, authorType)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    if (type == TYPE_FEED_X_CARD_PRODUCT_TOPADS) "add to wishlist" else "wishlist",
                    getPostType(type, isFollowed, mediaType, authorType)
                ),
                finalLabel,
                KEY_BUSINESS_UNIT_EVENT to CONTENT,
                KEY_CURRENT_SITE_EVENT to MARKETPLACE,

                KEY_EVENT_USER_ID to userSessionInterface.userId
            )
        )
        if (trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to trackerId)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventOnTagSheetItemBuyClicked(
        trackerData: FeedTrackerData
    ) {
        val activityId = trackerData.postId
        val shopId = trackerData.shopId
        val type = trackerData.postType
        val isFollowed = trackerData.isFollowed
        val mediaType = trackerData.mediaType
        val trackerId = if (trackerData.campaignStatus.isNotEmpty()) {
            if (trackerData.isFollowed) "13446" else "13432"
        } else {
            ""
        }

        val finalLabel = if (trackerData.campaignStatus.isNotEmpty() && isFollowed) {
            String.format(
                FORMAT_FIVE_PARAM,
                activityId,
                shopId,
                trackerData.contentSlotValue,
                trackerData.campaignStatus,
                trackerData.hasVoucher
            )
        } else {
            String.format(
                FORMAT_FOUR_PARAM,
                activityId,
                shopId,
                trackerData.contentSlotValue,
                trackerData.hasVoucher
            )
        }
        var map = getCommonMap(
            CATEGORY_FEED_TIMELINE_BOTTOMSHEET,
            trackerData.campaignStatus,
            trackerData.authorType
        )
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "lihat wishlist",
                    getPostType(type, isFollowed, mediaType, trackerData.authorType)
                ),
                KEY_EVENT_LABEL to finalLabel,
                KEY_BUSINESS_UNIT_EVENT to CONTENT,
                KEY_CURRENT_SITE_EVENT to MARKETPLACE,

                KEY_EVENT_USER_ID to userSessionInterface.userId
            )
        )
        if (trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to trackerId)
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventonShareProductClicked(
        trackerData: FeedTrackerData
    ) {
        val activityId = trackerData.postId
        val shopId = trackerData.shopId
        val type = trackerData.postType
        val isFollowed = trackerData.isFollowed
        val mediaType = trackerData.mediaType
        val category =
            if (trackerData.isProductDetailPage) CATEGORY_FEED_TIMELINE_FEED_DETAIL else CATEGORY_FEED_TIMELINE_BOTTOMSHEET

        var map = getCommonMap(category, authorType = trackerData.authorType)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "share",
                    getPostType(type, isFollowed, mediaType, trackerData.authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    trackerData.contentSlotValue,
                    trackerData.hasVoucher
                ),
                KEY_BUSINESS_UNIT_EVENT to CONTENT,
                KEY_CURRENT_SITE_EVENT to MARKETPLACE,
                KEY_EVENT_USER_ID to userSessionInterface.userId
            )
        )
        if (trackerData.trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to trackerData.trackerId)
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventGridProductItemClicked(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val product = feedTrackerData.product
        val index = feedTrackerData.mediaIndex

        val eventLabelFinal =
            if (feedTrackerData.campaignStatus.isNotEmpty() && isFollowed) {
                String.format(
                    FORMAT_SIX_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.campaignStatus,
                    product.id,
                    feedTrackerData.hasVoucher
                )
            } else {
                String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    product.id,
                    feedTrackerData.hasVoucher
                )
            }

        trackEnhancedEcommerceEventNew(
            PRODUCT_CLICK,
            CATEGORY_FEED_TIMELINE,
            String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "product",
                getPostType(type, isFollowed, authorType = feedTrackerData.authorType)
            ),
            eventLabelFinal,
            eCommerceData = DataLayer.mapOf(
                CLICK,
                mapOf(
                    "actionField" to mapOf(
                        "list" to "/feed - ${
                        getPostType(
                            type = TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT,
                            isFollowed,
                            authorType = feedTrackerData.authorType
                        )
                        }"
                    ),
                    "products" to getSingleProductListASGC(
                        product,
                        index + 1,
                        type,
                        isFollowed,
                        authorType = feedTrackerData.authorType
                    )
                )
            )
        )
    }

    fun eventAddToCartFeedVOD(
        channelId: String,
        productId: String,
        productName: String,
        price: String,
        quantity: Int,
        shopId: String,
        shopName: String,
        type: String,
        isFollowed: Boolean,
        mediaType: String,
        contentScore: String,
        hasVoucher: Boolean,
        authorType: String
    ) {
        trackEnhancedEcommerceEventNew(
            ADD_TO_CART,
            CATEGORY_FEED_TIMELINE_BOTTOMSHEET,
            String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                CLICK_ADD_TO_CART,
                getPostType(type, isFollowed, mediaType, authorType)
            ),
            String.format(
                FORMAT_FIVE_PARAM,
                channelId,
                shopId,
                contentScore,
                productId,
                hasVoucher
            ),
            eCommerceData = getCurrencyData() +
                getAddData(
                    getActionFieldData(
                        getListData(
                            "/feed - ${
                            getPostType(
                                type,
                                isFollowed,
                                mediaType,
                                authorType
                            )
                            }"
                        )
                    ) +
                        getProductsData(
                            listOf(
                                getProductData(
                                    productId,
                                    productName,
                                    price.getDigits().toZeroIfNull(),
                                    quantity,
                                    shopId,
                                    shopName,
                                    type,
                                    isFollowed,
                                    mediaType,
                                    authorType
                                )
                            )
                        )
                )
        )
    }

    fun sendClickRemindCampaignAsgcEvent(activityId: String, shopId: String, contentScore: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_PG,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "remind campaign",
                "asgc"
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_FOUR_PARAM,
                activityId,
                shopId,
                contentScore,
                "pre"
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_TRACKER_ID to "35343",
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun sendClickUnremindCampaignAsgcEvent(
        activityId: String,
        shopId: String,
        contentScore: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_PG,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "unremind campaign",
                "asgc"
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_FOUR_PARAM,
                activityId,
                shopId,
                contentScore,
                "pre"
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_TRACKER_ID to "35344",
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun sendClickAddToWishlistAsgcProductDetail(
        activityId: String,
        shopId: String,
        productId: String,
        campaignStatus: String,
        contentScore: String,
        hasVoucher: Boolean
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_PG,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_FEED_DETAIL,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "wishlist",
                "asgc"
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_SIX_PARAM,
                activityId,
                shopId,
                contentScore,
                campaignStatus,
                productId,
                hasVoucher
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_TRACKER_ID to "17988",
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun sendClickAddToCartAsgcProductDetail(
        activityId: String,
        shopId: String,
        productId: String,
        campaignStatus: String,
        productName: String,
        price: String,
        quantity: Int,
        shopName: String,
        contentScore: String,
        hasVoucher: Boolean,
        authorType: String
    ) {
        trackEnhancedEcommerceEventNew(
            ADD_TO_CART,
            CATEGORY_FEED_TIMELINE_FEED_DETAIL,
            String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                CLICK_ADD_TO_CART,
                "asgc"
            ),
            String.format(
                FORMAT_SIX_PARAM,
                activityId,
                shopId,
                contentScore,
                campaignStatus,
                productId,
                hasVoucher
            ),
            trackerId = "17987",
            eCommerceData = getCurrencyData() +
                getAddData(
                    getActionFieldData(getListData("/feed - asgc")) +
                        getProductsData(
                            listOf(
                                getProductData(
                                    productId,
                                    productName,
                                    price.getDigits().toZeroIfNull(),
                                    quantity,
                                    shopId,
                                    shopName,
                                    TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT,
                                    true,
                                    MediaType.IMAGE,
                                    authorType
                                )
                            )
                        )
                )
        )
    }

    fun sendClickAddToCartAsgcProductTagBottomSheet(
        activityId: String,
        shopId: String,
        productId: String,
        campaignStatus: String,
        productName: String,
        price: String,
        quantity: Int,
        shopName: String,
        contentScore: String,
        hasVoucher: Boolean,
        authorType: String
    ) {
        trackEnhancedEcommerceEventNew(
            ADD_TO_CART,
            CATEGORY_FEED_TIMELINE_BOTTOMSHEET,
            String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                CLICK_ADD_TO_CART,
                "asgc"
            ),
            String.format(
                FORMAT_SIX_PARAM,
                activityId,
                shopId,
                contentScore,
                campaignStatus,
                productId,
                hasVoucher
            ),
            trackerId = "17982",
            eCommerceData = getCurrencyData() +
                getAddData(
                    getActionFieldData(getListData("/feed - asgc")) +
                        getProductsData(
                            listOf(
                                getProductData(
                                    productId,
                                    productName,
                                    price.getDigits().toZeroIfNull(),
                                    quantity,
                                    shopId,
                                    shopName,
                                    TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT,
                                    true,
                                    MediaType.IMAGE,
                                    authorType
                                )
                            )
                        )
                )
        )
    }

    fun eventGridMoreProductCLicked(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val campaignStatus = feedTrackerData.campaignStatus
        val isAsgcRecomm = feedTrackerData.postType == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !feedTrackerData.isFollowed

        val finallabel =
            if (campaignStatus.isNotEmpty()) {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    campaignStatus,
                    feedTrackerData.hasVoucher
                )
            } else {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            }
        var map = mapOf(
            KEY_EVENT to getEvent(campaignStatus.isNotEmpty(), isAsgcRecomm = isAsgcRecomm),
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "more product",
                getPostType(type, isFollowed, authorType = feedTrackerData.authorType)
            ),
            finallabel,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        if (feedTrackerData.trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to feedTrackerData.trackerId)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickLikeButton(
        feedTrackerData: FeedTrackerData,
        doubleTap: Boolean = false,
        isLiked: Boolean
    ) {
        val activityId = feedTrackerData.postId
        val shopId = feedTrackerData.shopId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val mediaType = feedTrackerData.mediaType

        val likeType = if (doubleTap && isLiked) {
            "double tap like"
        } else if (doubleTap && !isLiked) {
            "double tap unlike"
        } else if (isLiked) {
            "like"
        } else {
            "unlike"
        }

        val finallabel =
            if (feedTrackerData.campaignStatus.isNotEmpty()) {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.campaignStatus,
                    feedTrackerData.hasVoucher
                )
            } else {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            }
        var map = getCommonMap(
            campaignStatus = feedTrackerData.campaignStatus,
            authorType = feedTrackerData.authorType
        )
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    likeType,
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                finallabel
            )
        )
        if (feedTrackerData.trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to feedTrackerData.trackerId)
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun getImpressionPostASGC(
        imageUrl: String,
        activityId: String,
        position: Int
    ): Map<String, Any> = DataLayer.mapOf(
        Promotion.CREATIVE,
        imageUrl,
        Promotion.ID,
        activityId,
        Promotion.NAME,
        "/feed - asgc - post",
        Promotion.POSITION,
        position + 1
    )

    private fun getImpressionPostSGC(
        imageUrl: String,
        activityId: String,
        position: Int,
        type: String,
        isFollowed: Boolean,
        mediaType: String,
        authorType: String
    ): Map<String, Any> = DataLayer.mapOf(
        Promotion.CREATIVE,
        imageUrl,
        Promotion.ID,
        activityId,
        Promotion.NAME,
        "/feed - ${
        getPostType(
            type,
            isFollowed,
            mediaType,
            authorType
        )
        } - ${if (mediaType == TYPE_LONG_VIDEO || mediaType == TYPE_VIDEO) TYPE_VIDEO else TYPE_IMAGE}",
        Promotion.POSITION,
        position + 1
    )

    private fun getImpressionPostSGCType(
        imageUrl: String,
        activityId: String,
        position: Int,
        type: String,
        isFollowed: Boolean,
        mediaType: String,
        authorType: String
    ): Map<String, Any> = DataLayer.mapOf(
        Promotion.CREATIVE,
        imageUrl,
        Promotion.ID,
        activityId,
        Promotion.NAME,
        "/feed - ${getPostType(type, isFollowed, mediaType, authorType)} - post",
        Promotion.POSITION,
        position + 1
    )

    fun eventImpressionProduct(
        activityId: String,
        productId: String,
        products: List<FeedXProduct>,
        shopId: String,
        isFollowed: Boolean,
        hasVoucher: Boolean
    ) {
        val type = if (productId == TYPE_FEED_X_CARD_PRODUCT_TOPADS) {
            TOPADS
        } else {
            if (!isFollowed) ASGC_RECOM else ASGC
        }
        trackEnhancedEcommerceEventNew(
            PRODUCT_VIEW,
            CONTENT_FEED_TIMELINE,
            String.format(
                FORMAT_THREE_PARAM,
                "impression",
                "product",
                type
            ),
            String.format(
                FORMAT_FOUR_PARAM,
                activityId,
                shopId,
                products[0].id,
                hasVoucher
            ),
            DataLayer.mapOf(
                Product.CURRENCY_CODE,
                Product.CURRENCY_CODE_IDR,
                "impressions",
                getProductItemASGC(products, type)
            )
        )
    }

    fun eventImpressionPostASGC(
        activityId: String,
        position: Int,
        imageUrl: String,
        shopId: String,
        hasVoucher: Boolean
    ) {
        trackEnhancedEcommerceEventNew(
            PROMO_VIEW,
            CONTENT_FEED_TIMELINE,
            String.format(
                FORMAT_THREE_PARAM,
                "impression",
                "post",
                "asgc"
            ),
            String.format(
                FORMAT_THREE_PARAM,
                activityId,
                shopId,
                hasVoucher
            ),
            getPromoViewData(
                getPromotionsData(
                    listOf(
                        getImpressionPostASGC(imageUrl, activityId, position)
                    )
                )
            )
        )
    }

    fun eventImpression(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val media = feedTrackerData.media
        val position = feedTrackerData.positionInFeed
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val isVideo = type == TYPE_FEED_X_CARD_PLAY
        val isLongVideo = type == TYPE_FEED_X_CARD_POST && media.type == TYPE_LONG_VIDEO
        var eventAction = ""
        var mediaType = ""
        when {
            isVideo -> {
                eventAction = String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "video",
                    getPostType(type, isFollowed, TYPE_VIDEO, feedTrackerData.authorType)
                )
                mediaType = TYPE_VIDEO
            }
            isLongVideo -> {
                eventAction = String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "video",
                    getPostType(type, isFollowed, TYPE_LONG_VIDEO, feedTrackerData.authorType)
                )
                mediaType = TYPE_LONG_VIDEO
            }
            else -> {
                mediaType = TYPE_IMAGE
                eventAction = String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "image",
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                )
            }
        }
        trackEnhancedEcommerceEventNew(
            PROMO_VIEW,
            CONTENT_FEED_TIMELINE,
            eventAction,
            String.format(
                FORMAT_FOUR_PARAM,
                activityId,
                shopId,
                feedTrackerData.contentSlotValue,
                feedTrackerData.hasVoucher
            ),
            getPromoViewData(
                getPromotionsData(
                    listOf(
                        getImpressionPostSGC(
                            media.mediaUrl,
                            activityId,
                            position,
                            type,
                            isFollowed,
                            mediaType,
                            feedTrackerData.authorType
                        )
                    )
                )
            )
        )
    }

    fun eventPostImpression(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val media = feedTrackerData.media
        val position = feedTrackerData.positionInFeed
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType

        trackEnhancedEcommerceEventNew(
            PROMO_VIEW,
            CONTENT_FEED_TIMELINE,
            String.format(
                FORMAT_THREE_PARAM,
                "impression",
                "post",
                getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
            ),
            String.format(
                FORMAT_FOUR_PARAM,
                activityId,
                shopId,
                feedTrackerData.contentSlotValue,
                feedTrackerData.hasVoucher
            ),
            getPromoViewData(
                getPromotionsData(
                    listOf(
                        getImpressionPostSGCType(
                            media.mediaUrl,
                            activityId,
                            position,
                            type,
                            isFollowed,
                            mediaType,
                            feedTrackerData.authorType
                        )
                    )
                )
            )
        )
    }

    fun eventClickOpenComment(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType
        var map = getCommonMap(authorType = feedTrackerData.authorType)
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "comment",
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickLihatSemuaComment(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType

        var map = getCommonMap(authorType = feedTrackerData.authorType)
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "lihat semua",
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickOpenShare(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val shopId = feedTrackerData.shopId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val mediaType = feedTrackerData.mediaType

        val finalLabel =
            if (feedTrackerData.campaignStatus.isNotEmpty() && isFollowed) {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.campaignStatus,
                    feedTrackerData.hasVoucher
                )
            } else {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            }
        var map = getCommonMap(
            campaignStatus = feedTrackerData.campaignStatus,
            authorType = feedTrackerData.authorType
        )
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "share",
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                finalLabel
            )
        )
        if (feedTrackerData.trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to feedTrackerData.trackerId)
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickReadMoreNew(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val shopId = feedTrackerData.shopId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val mediaType = feedTrackerData.mediaType
        var map = getCommonMap(authorType = feedTrackerData.authorType)
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "lihat selengkapnya",
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickCloseProductInfoSheet(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType
        val trackerId = feedTrackerData.trackerId
        val campaignStatus = feedTrackerData.campaignStatus
        val eventLabel = if (campaignStatus.isNotEmpty() && isFollowed) {
            String.format(
                FORMAT_FIVE_PARAM,
                activityId,
                shopId,
                feedTrackerData.contentSlotValue,
                campaignStatus,
                feedTrackerData.hasVoucher
            )
        } else {
            String.format(
                FORMAT_FOUR_PARAM,
                activityId,
                shopId,
                feedTrackerData.contentSlotValue,
                feedTrackerData.hasVoucher
            )
        }
        var map = getCommonMapBottomSheet(campaignStatus = campaignStatus)
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "x",
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                KEY_EVENT_LABEL to eventLabel
            )
        )
        if (trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to trackerId)
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickFullScreenIconVOD(feedTrackerData: FeedTrackerData) {
        val channelId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType
        var map = getCommonMap(authorType = feedTrackerData.authorType)
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    CLICK_FULL_SCREEN,
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    channelId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventSendWatchVODAnalytics(feedTrackerData: FeedTrackerData, time: Long) {
        val channelId = feedTrackerData.postId
        val isFollowed = feedTrackerData.isFollowed
        val mediaType = feedTrackerData.mediaType
        val type = feedTrackerData.postType
        var map = getCommonMap(authorType = feedTrackerData.authorType)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    "watch",
                    "video",
                    getPostType(
                        type,
                        isFollowed = isFollowed,
                        mediaType,
                        feedTrackerData.authorType
                    )
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    channelId,
                    feedTrackerData.shopId,
                    feedTrackerData.contentSlotValue,
                    time,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClicklanjutMenontonVOD(feedTrackerData: FeedTrackerData) {
        val channelId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType

        var map = getCommonMap(authorType = feedTrackerData.authorType)
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    CLICK_LANJUT_MENONTON,
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    channelId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventImpressionProductBottomSheet(
        activityId: String,
        products: List<FeedXProduct>,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        isProductDetailPage: Boolean,
        mediaType: String,
        trackerId: String = "",
        hasVoucher: Boolean = false,
        authorType: String
    ) {
        trackEnhancedEcommerceEventNew(
            PRODUCT_VIEW,
            if (isProductDetailPage) CATEGORY_FEED_TIMELINE_FEED_DETAIL else CATEGORY_FEED_TIMELINE_BOTTOMSHEET,
            String.format(
                FORMAT_THREE_PARAM,
                "impression",
                "product",
                getPostType(type, isFollowed, mediaType, authorType)
            ),
            if (mediaType == TYPE_LONG_VIDEO) {
                String.format(
                    FORMAT_THREE_PARAM,
                    activityId,
                    shopId,
                    hasVoucher
                )
            } else {
                String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    if (isProductDetailPage) {
                        products[products.size - 1].id
                    } else {
                        products[0].id
                    },
                    hasVoucher
                )
            },
            DataLayer.mapOf(
                Product.CURRENCY_CODE,
                Product.CURRENCY_CODE_IDR,
                "impressions",
                getProductItemSGC(products, type, isFollowed, mediaType, authorType)
            ),
            trackerId = trackerId
        )
    }

    fun eventClickPostTagitem(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val campaignStatus = feedTrackerData.campaignStatus
        val product = feedTrackerData.product
        val position = feedTrackerData.mediaIndex

        val trackerIdForAsgcCampaign = if (isFollowed) "17984" else "40067"
        val eventLabelFinal = if (campaignStatus.isNotEmpty()) {
            String.format(
                FORMAT_SIX_PARAM,
                activityId,
                shopId,
                feedTrackerData.contentSlotValue,
                campaignStatus,
                product.id,
                feedTrackerData.hasVoucher
            )
        } else {
            String.format(
                FORMAT_FIVE_PARAM,
                activityId,
                shopId,
                feedTrackerData.contentSlotValue,
                product.id,
                feedTrackerData.hasVoucher
            )
        }

        trackEnhancedEcommerceEventNew(
            PRODUCT_CLICK,
            CATEGORY_FEED_TIMELINE,
            String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "product tag",
                getPostType(type, isFollowed, authorType = feedTrackerData.authorType)
            ),
            eventLabelFinal,
            DataLayer.mapOf(
                CLICK,
                mapOf(
                    "actionField" to mapOf(
                        "list" to "/feed - ${
                        getPostType(
                            type,
                            isFollowed,
                            authorType = feedTrackerData.authorType
                        )
                        }"
                    ),
                    "products" to getSingleProductListASGC(
                        product,
                        position,
                        type,
                        isFollowed,
                        authorType = feedTrackerData.authorType
                    )
                )
            ),
            trackerId = if (campaignStatus.isNotEmpty() && isFollowed) trackerIdForAsgcCampaign else ""
        )
    }

    fun eventClickBSitem(
        feedTrackerData: FeedTrackerData,
        productPosition: Int
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType
        val trackerId = feedTrackerData.trackerId
        val campaignStatus = feedTrackerData.campaignStatus
        val product = feedTrackerData.product
        val finalLabel =
            if (campaignStatus.isNotEmpty() && isFollowed) {
                String.format(
                    FORMAT_SIX_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    campaignStatus,
                    product.id,
                    feedTrackerData.hasVoucher
                )
            } else {
                String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    product.id,
                    feedTrackerData.hasVoucher
                )
            }
        val postType = getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)

        trackEnhancedEcommerceEventNew(
            PRODUCT_CLICK,
            CATEGORY_FEED_TIMELINE_BOTTOMSHEET,
            String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "product",
                postType
            ),
            finalLabel,
            trackerId = trackerId,
            eCommerceData = DataLayer.mapOf(
                CLICK,
                mapOf(
                    "actionField" to mapOf(
                        "list" to "/feed - ${if (postType == ASGC || postType == ASGC_RECOM) "$postType detail" else postType}"
                    ),
                    "products" to getSingleProductListASGC(
                        product,
                        productPosition,
                        type,
                        isFollowed,
                        mediaType,
                        feedTrackerData.authorType
                    )
                )
            )
        )
    }

    fun clickSekSekarang(
        postId: String,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        authorType: String
    ) {
        var map = getCommonMap(CATEGORY_FEED_TIMELINE, authorType = authorType)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    CLICK_SEK_SEKARANG,
                    getPostType(type, isFollowed, authorType = authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_TWO_PARAM,
                    postId,
                    shopId
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickFollowitem(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType
        val trackerId = feedTrackerData.trackerId
        val followtext = if (isFollowed) {
            "unfollow"
        } else {
            "follow"
        }
        var map = mapOf(
            KEY_EVENT to getEvent(trackerId.isNotEmpty()),
            KEY_EVENT_CATEGORY to Category.CONTENT_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                followtext,
                getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_FOUR_PARAM,
                activityId,
                shopId,
                feedTrackerData.contentSlotValue,
                feedTrackerData.hasVoucher
            ),

            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        if (trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to trackerId)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickGreyArea(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postType
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.shopId
        val mediaType = feedTrackerData.mediaType
        val trackerId = feedTrackerData.trackerId
        val campaignStatus = feedTrackerData.campaignStatus
        val finalLabel =
            if (campaignStatus.isNotEmpty() && isFollowed) {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    campaignStatus,
                    feedTrackerData.hasVoucher
                )
            } else {
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            }
        var map = getCommonMap(
            CATEGORY_FEED_TIMELINE_BOTTOMSHEET,
            campaignStatus,
            authorType = feedTrackerData.authorType
        )
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "grey area",
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                finalLabel
            )
        )
        if (trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to trackerId)
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun getProductItemASGC(
        feedXProduct: List<FeedXProduct>,
        type: String
    ): List<Map<String, Any>> {
        val list: MutableList<Map<String, Any>> = mutableListOf()
        for (i in feedXProduct) {
            val map = createItemMapASGC(i, (feedXProduct.indexOf(i) + 1).toString(), type)
            list.add(map)
        }
        return list
    }

    private fun getProductItemSGC(
        feedXProduct: List<FeedXProduct>,
        type: String,
        isFollowed: Boolean,
        mediaType: String,
        authorType: String
    ): List<Map<String, Any>> {
        val list: MutableList<Map<String, Any>> = mutableListOf()
        for (i in feedXProduct) {
            val map = createItemMapSGC(
                i,
                (feedXProduct.indexOf(i) + 1).toString(),
                type,
                isFollowed,
                mediaType,
                list = "/feed - ${getPostType(type, isFollowed, mediaType, authorType)}",
                authorType = authorType
            )
            list.add(map)
        }
        return list
    }

    private fun getSingleProductListASGC(
        feedXProduct: FeedXProduct,
        index: Int,
        type: String,
        isFollowed: Boolean,
        mediaType: String = "",
        authorType: String
    ): List<Map<String, Any>> {
        val list: MutableList<Map<String, Any>> = mutableListOf()
        val map = if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT) {
            createItemMapASGC(
                feedXProduct,
                index.toString(),
                type
            )
        } else {
            createItemMapSGC(
                feedXProduct,
                index.toString(),
                type,
                isFollowed,
                mediaType,
                authorType = authorType
            )
        }
        list.add(map)
        return list
    }

    private fun createItemMapASGC(
        feedXProduct: FeedXProduct,
        index: String,
        type: String
    ): Map<String, Any> =
        DataLayer.mapOf(
            Product.INDEX, index,
            Product.BRAND, "",
            Product.CATEGORY, "",
            Product.ID, feedXProduct.id,
            Product.NAME, feedXProduct.name,
            Product.VARIANT, "",
            Product.PRICE,
            if (feedXProduct.isDiscount) feedXProduct.priceDiscount else feedXProduct.price,
            "dimension39", "/feed - asgc detail"
        )

    private fun createItemMapSGC(
        feedXProduct: FeedXProduct,
        index: String,
        type: String,
        isFollowed: Boolean,
        mediaType: String = "",
        list: String = "",
        authorType: String
    ): Map<String, Any> =
        DataLayer.mapOf(
            Product.INDEX, index,
            Product.BRAND, "",
            Product.CATEGORY, "",
            Product.ID, feedXProduct.id,
            Product.NAME, feedXProduct.name,
            Product.VARIANT, "",
            Product.LIST, list,
            Product.PRICE,
            if (feedXProduct.isDiscount) feedXProduct.priceDiscount.toString() else feedXProduct.price.toString(),
            "dimension39", "/feed - ${getPostType(type, isFollowed, mediaType, authorType)}"
        )

    fun eventCloseThreeDotBS(
        activityId: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        authorType: String
    ) {
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_MENU, authorType = authorType)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "x",
                    getPostType(type, isFollowed, authorType = authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_TWO_PARAM,
                    activityId,
                    shopId
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickThreeDotsOption(
        activityId: String,
        action: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        mediaType: String,
        trackerId: String = "",
        campaignStatus: String = "",
        authorType: String
    ) {
        val isAsgcRecomm = type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !isFollowed
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_MENU, campaignStatus, authorType, isAsgcRecomm)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    action,
                    getPostType(type, isFollowed, mediaType, authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_THREE_PARAM,
                    activityId,
                    shopId,
                    action
                )
            )
        )
        if (trackerId.isNotEmpty()) {
            map = map.plus(KEY_TRACKER_ID to trackerId)
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickGreyAreaThreeDots(feedTrackerData: FeedTrackerData) {
        val activityId = feedTrackerData.postId
        val type = feedTrackerData.postId
        val isFollowed = feedTrackerData.isFollowed
        val shopId = feedTrackerData.postId

        var map = getCommonMap(CATEGORY_FEED_TIMELINE_MENU, authorType = feedTrackerData.authorType)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "grey area",
                    getPostType(type, isFollowed, authorType = feedTrackerData.authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun openCommentDetailPage(loginState: Boolean) {
        val map = mapOf(
            KEY_EVENT to OPEN_SCREEN,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            SCREEN_DIMENSION_IS_LOGGED_IN_STATUS to loginState,
            KEY_EVENT_SCREEN_NAME to "/feed/comment-detail",

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickDeleteThreeDotsPage(
        actvityId: String,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        mediaType: String,
        authorType: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_MENU,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "delete",
                getPostType(type, isFollowed, mediaType, authorType)
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                actvityId,
                userSessionInterface.shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickDeleteConfirmThreeDotsPage(
        actvityId: String,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        isVideo: Boolean,
        authorType: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_MENU,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "confirm delete",
                getPostType(type, isFollowed, if (isVideo) TYPE_VIDEO else TYPE_IMAGE, authorType)
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                actvityId,
                shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickHashTag(
        hashTag: String,
        authorId: String,
        postId: String,
        postType: String,
        isVideoPost: Boolean,
        isFollowed: Boolean,
        isCommentPage: Boolean,
        contentSlotValue: String = "",
        hasVoucher: Boolean = false,
        authorType: String = ""
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to if (isCommentPage) CATEGORY_FEED_TIMELINE_COMMENT else CATEGORY_FEED_TIMELINE,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "hashtag",
                getPostType(
                    postType,
                    isFollowed,
                    if (isVideoPost) TYPE_VIDEO else TYPE_IMAGE,
                    authorType
                )
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_FIVE_PARAM,
                postId,
                authorId,
                contentSlotValue,
                hashTag,
                hasVoucher
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickBackButtonCommentPage(
        actvityId: String,
        shopId: String,
        isVideoPost: Boolean,
        isFollowed: Boolean,
        postType: String,
        authorType: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "back",
                getPostType(
                    postType,
                    isFollowed,
                    if (isVideoPost) TYPE_VIDEO else TYPE_IMAGE,
                    authorType
                )

            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                actvityId,
                shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickShopCommentPage(
        actvityId: String,
        authorId: String,
        isVideoPost: Boolean,
        isFollowed: Boolean,
        postType: String,
        authorType: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "shop",
                getPostType(
                    postType,
                    isFollowed,
                    if (isVideoPost) TYPE_VIDEO else TYPE_IMAGE,
                    authorType
                )
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                actvityId,
                authorId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickCreatorPageCommentPage(
        actvityId: String,
        authorId: String,
        isVideoPost: Boolean,
        isFollowed: Boolean,
        postType: String,
        userId: String,
        authorType: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "comment creator",
                getPostType(
                    postType,
                    isFollowed,
                    if (isVideoPost) TYPE_VIDEO else TYPE_IMAGE,
                    authorType
                )

            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_THREE_PARAM,
                actvityId,
                authorId,
                userId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickReportCommentPage(
        actvityId: String,
        authorId: String,
        isVideoPost: Boolean,
        isFollowed: Boolean,
        postType: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "report",
                getPostType(
                    postType,
                    isFollowed,
                    if (isVideoPost) TYPE_VIDEO else TYPE_IMAGE,
                    authorId
                )
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                actvityId,
                authorId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickDeleteCommentPage(
        actvityId: String,
        authorId: String,
        isVideoPost: Boolean,
        isFollowed: Boolean,
        postType: String,
        authorType: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "delete",
                getPostType(
                    postType,
                    isFollowed,
                    if (isVideoPost) TYPE_VIDEO else TYPE_IMAGE,
                    authorType
                )
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                actvityId,
                authorId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickSendCommentPage(
        actvityId: String,
        authorId: String,
        isVideoPost: Boolean,
        isFollowed: Boolean,
        postType: String,
        authorType: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "send",
                getPostType(
                    postType,
                    isFollowed,
                    if (isVideoPost) TYPE_VIDEO else TYPE_IMAGE,
                    authorType
                )
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                actvityId,
                authorId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickKembalikanCommentPage(
        actvityId: String,
        authorId: String,
        isVideoPost: Boolean,
        isFollowed: Boolean,
        postType: String,
        authorType: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "kembalikan to undo delete",
                getPostType(
                    postType,
                    isFollowed,
                    if (isVideoPost) TYPE_VIDEO else TYPE_IMAGE,
                    authorType
                )
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                actvityId,
                authorId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickMuteButton(
        feedTrackerData: FeedTrackerData
    ) {
        val activityId = feedTrackerData.postId
        val isFollowed = feedTrackerData.isFollowed
        val mediaType = feedTrackerData.mediaType
        var map = getCommonMap(authorType = feedTrackerData.authorType)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "mute",
                    getPostType(
                        feedTrackerData.postType,
                        isFollowed,
                        mediaType,
                        feedTrackerData.authorType
                    )
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    activityId,
                    feedTrackerData.shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickSoundVOD(feedTrackerData: FeedTrackerData) {
        val channelId = feedTrackerData.postId
        val isFollowed = feedTrackerData.isFollowed
        val mediaType = feedTrackerData.mediaType
        var map = getCommonMap(authorType = feedTrackerData.authorType)
        val type = feedTrackerData.postType
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "sound",
                    getPostType(type, isFollowed, mediaType, feedTrackerData.authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FOUR_PARAM,
                    channelId,
                    feedTrackerData.shopId,
                    feedTrackerData.contentSlotValue,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickOnVideo(
        activityId: String,
        authorId: String,
        isFollowed: Boolean,
        contentScore: String,
        authorType: String
    ) {
        var map = getCommonMap(authorType = authorType)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "video area",
                    getPostType("", isFollowed, TYPE_VIDEO, authorType)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_THREE_PARAM,
                    activityId,
                    authorId,
                    contentScore

                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventWatchVideo(feedTrackerData: FeedTrackerData, time: Long) {
        val activityId = feedTrackerData.postId
        val isFollowed = feedTrackerData.isFollowed
        val mediaType = feedTrackerData.mediaType
        var map = getCommonMap(authorType = feedTrackerData.authorType)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    "watch",
                    "video",
                    getPostType(
                        feedTrackerData.postType,
                        isFollowed = isFollowed,
                        mediaType,
                        feedTrackerData.authorType
                    )
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_FIVE_PARAM,
                    activityId,
                    feedTrackerData.shopId,
                    feedTrackerData.contentSlotValue,
                    time,
                    feedTrackerData.hasVoucher
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun getCommonMap(
        category: String = CATEGORY_FEED_TIMELINE,
        campaignStatus: String = "",
        authorType: String = "",
        isAsgcRecomm: Boolean = false
    ): Map<String, String> {
        return mapOf(
            KEY_EVENT to getEvent(campaignStatus.isNotEmpty(), authorType, isAsgcRecomm),
            KEY_EVENT_CATEGORY to category,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
    }

    private fun getCommonMapBottomSheet(
        category: String = CATEGORY_FEED_TIMELINE_BOTTOMSHEET,
        campaignStatus: String = ""
    ): Map<String, String> {
        return mapOf(
            KEY_EVENT to getEvent(campaignStatus.isNotEmpty()),
            KEY_EVENT_CATEGORY to category,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
    }

    //    https://docs.google.com/spreadsheets/d/1yFbEMzRj0_VdeVN7KfZIHZlv71uvX38XjfcYw7nPB3c/edit#gid=1359526861
    //    screenshot 31
    fun eventClickMediaPreviewAvatar(targetId: String, targetType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CLICK_FEED,
            CATEGORY_FEED_TIMELINE,
            String.format(
                Action.ACTION_CLICK_MEDIAPREVIEW_AVATAR,
                targetType,
                if (userSessionInterface.isLoggedIn) Action.PARAM_ACTION_LOGIN else Action.PARAM_ACTION_NONLOGIN
            ),
            targetId
        )
    }

    //    https://docs.google.com/spreadsheets/d/1yFbEMzRj0_VdeVN7KfZIHZlv71uvX38XjfcYw7nPB3c/edit#gid=1359526861
    //    screenshot 15
    fun eventClickFeedDetailAvatar(activityId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CLICK_FEED,
            Category.CATEGORY_FEED_TIMELINE_FEED_DETAIL,
            Action.CLICK_FEED_PRODUCT_DETAIL,
            String.format(FORMAT_TWO_PARAM, shopId, activityId)
        )
    }

    //    https://docs.google.com/spreadsheets/d/1yFbEMzRj0_VdeVN7KfZIHZlv71uvX38XjfcYw7nPB3c/edit#gid=1359526861
    //    screenshot 7
    fun eventClickTopadsPromoted(shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CLICK_FEED,
            CATEGORY_FEED_TIMELINE,
            Action.ACTION_CLICK_TOPADS_PROMOTED,
            shopId
        )
    }

    /**
     *
     *  * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     *
     */
    fun eventOpenMediaPreview() {
        trackOpenScreenEvent(Screen.MEDIA_PREVIEW)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 31
     *
     * @param activityId - postId
     */
    fun eventMediaDetailClickAvatar(activityId: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_FEED_TIMELINE,
            Action.CLICK_AVATAR,
            activityId
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 32
     *
     * @param activityId - postId
     */
    fun eventMediaDetailClickLihat(activityId: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_FEED_TIMELINE,
            Action.CLICK_SEE,
            activityId
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 34
     *
     * @param productId - productId
     */
    fun eventMediaDetailClickBuy(
        role: String,
        productId: String,
        productName: String,
        price: String,
        quantity: Int,
        shopId: String,
        shopName: String,
        authorType: String
    ) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_FEED_TIMELINE,
            Action.CLICK_BUY,
            productId
        )
        trackEnhancedEcommerceEvent(
            CLICK_FEED,
            Category.CONTENT_FEED_TIMELINE,
            Action.CLICK_BUY,
            productId,
            DataLayer.mapOf(
                Product.CURRENCY_CODE,
                Product.CURRENCY_CODE_IDR,
                Event.ADD,
                getMediaPreviewAddData(
                    role,
                    getProductsDataList(
                        listOf(
                            getProductData(
                                productId,
                                productName,
                                price.getDigits().toZeroIfNull(),
                                quantity,
                                shopId,
                                shopName,
                                authorType = authorType
                            )
                        )
                    )
                )
            )
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 42
     *
     * @param activityId - postId
     */
    fun eventOpenTrendingPage() {
        trackOpenScreenEvent(Screen.TRENDING)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 35
     *
     * @param activityId - postId
     */
    fun eventTrendingClickMedia(activityId: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_EXPLORE_TRENDING,
            Action.CLICK_MEDIA,
            activityId
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 3
     *
     * @param activityId - postId
     */
    fun eventTrendingClickSeeAll(activityId: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_EXPLORE_TRENDING,
            Action.CLICK_SEE_ALL,
            activityId
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 3
     *
     * @param activityId - postId
     */
    fun eventTrendingClickProfile(activityId: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_EXPLORE_TRENDING,
            Action.CLICK_AVATAR,
            activityId
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 1
     *
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     * @param hashtag - the hashtag name
     */
    fun eventTimelineClickHashtag(
        activityId: String,
        activityName: String,
        mediaType: String,
        hashtag: String
    ) {
        eventClickHashtag(
            CLICK_FEED,
            Category.CONTENT_FEED_TIMELINE,
            activityId,
            activityName,
            mediaType,
            hashtag
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 2
     *
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     * @param hashtag - the hashtag name
     */
    fun eventDetailClickHashtag(
        activityId: String,
        activityName: String,
        mediaType: String,
        hashtag: String
    ) {
        eventClickHashtag(
            CLICK_FEED,
            Category.FEED_DETAIL_PAGE,
            activityId,
            activityName,
            mediaType,
            hashtag
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 39
     *
     * @param isOwner - `true` indicates opening self-profile, `false` indicates opening other-profile
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     * @param hashtag - the hashtag name
     */
    fun eventProfileClickHashtag(isOwner: Boolean, activityId: String, hashtag: String) {
        eventClickHashtag(
            Event.CLICK_SOCIAL_COMMERCE,
            if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE,
            activityId,
            hashtag
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 27
     *
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     */
    fun eventTimelineClickReadMore(activityId: String, activityName: String, mediaType: String) {
        eventClickReadMore(
            CLICK_FEED,
            Category.CONTENT_FEED_TIMELINE,
            activityId,
            activityName,
            mediaType
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 28
     *
     * @param isOwner - `true` indicates opening self-profile, `false` indicates opening other-profile
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     */
    fun eventProfileClickReadMore(
        isOwner: Boolean,
        activityId: String,
        activityName: String,
        mediaType: String
    ) {
        eventClickReadMore(
            Event.CLICK_SOCIAL_COMMERCE,
            if (isOwner) Category.MY_PROFILE_SOCIALCOMMERCE else Category.USER_PROFILE_SOCIALCOMMERCE,
            activityId,
            activityName,
            mediaType
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 29
     *
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     */
    fun eventShopPageClickReadMore(activityId: String, activityName: String, mediaType: String) {
        eventClickReadMore(
            CLICK_FEED,
            Category.CONTENT_FEED_SHOP_PAGE,
            activityId,
            activityName,
            mediaType
        )
    }

    /**
     * row 5 for feed page
     * row 26 for shop feed page
     * docs: https://docs.google.com/spreadsheets/d/1pnZfjiNKbAk8LR37DhNGSwm2jvM3wKqNJc2lfWLejXA/edit#gid=1781959013
     * Screenshot xx (not included)
     *
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     */
    fun eventImageImpressionPost(
        screenName: String,
        activityId: String,
        activityName: String,
        mediaType: String,
        imageUrl: String,
        recomId: Long,
        rowNumber: Int
    ) {
        var eventCategory = ""
        var promotionsNameInitial = ""
        var eventLabel = ""
        when (screenName) {
            Screen.FEED -> {
                eventCategory = Category.CONTENT_FEED_TIMELINE
                promotionsNameInitial = "/content feed"
                eventLabel = "$activityId - $recomId"
            }
            Screen.FEED_SHOP -> {
                eventCategory = Category.CONTENT_FEED_SHOP_PAGE
                promotionsNameInitial = "/feed shop page"
                eventLabel = activityId
            }
        }
        trackEnhancedEcommerceEvent(
            Event.PROMO_VIEW,
            eventCategory,
            "${Action.IMPRESSION_POST} - $activityName - $mediaType",
            eventLabel,
            getPromoViewData(
                getPromotionsData(
                    listOf(
                        getPromotionData(
                            activityId,
                            "$promotionsNameInitial - $activityName - $mediaType",
                            imageUrl,
                            rowNumber
                        )
                    )
                )
            )
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1pnZfjiNKbAk8LR37DhNGSwm2jvM3wKqNJc2lfWLejXA/edit#gid=1878700964
     * Screenshot xx (not included)
     *
     * @param activityName - activity name
     * @param activityId - postId
     * @param mediaType - video or image
     */
    fun eventShopPageClickPost(
        activityId: String,
        activityName: String,
        mediaType: String,
        imageUrl: String,
        rowNumber: Int
    ) {
        eventClickReadMore(
            CLICK_FEED,
            Category.CONTENT_FEED_SHOP_PAGE,
            activityId,
            activityName,
            mediaType
        )
        trackEnhancedEcommerceEvent(
            CLICK_FEED,
            Category.CONTENT_FEED_SHOP_PAGE,
            Action.CLICK,
            String.format("post - %s - %s - %s", activityName, activityId, mediaType),
            getPromoClickData(
                getPromotionsData(
                    listOf(
                        getPromotionData(
                            activityId,
                            String.format("/feed shop page - %s - %s", activityName, mediaType),
                            imageUrl,
                            rowNumber
                        )
                    )
                )
            )
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 4
     *
     * @param activityId - postId
     * @param hashtag - hashtag name
     * @param position - position of item in list
     */
    fun eventHashtagPageClickThumbnail(
        activityId: String,
        hashtag: String,
        position: Int
    ) {
        trackEnhancedEcommerceEvent(
            Event.PROMO_CLICK,
            Category.CONTENT_HASHTAG,
            Action.CLICK_POST,
            activityId,
            getPromoClickData(
                getPromotionsData(
                    listOf(
                        getPromotionData(
                            activityId,
                            Screen.HASHTAG_POST_LIST,
                            hashtag,
                            position
                        )
                    )
                )
            )
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1L9tKULOcYsEDRC9NWANTiwijXI9aSNp6VAMAQN5E90g/edit?pli=1#gid=1645146163
     * Screenshot 4
     *
     * @param activityId - postId
     * @param hashtag - hashtag name
     * @param position - position of item in list
     */
    fun eventContentDetailHashtagPageClickThumbnail(
        activityId: String,
        hashtag: String,
        source: String
    ) {
        val generalData = DataLayer.mapOf(
            EVENT, Event.CLICK_PG,
            EVENT_ACTION, Action.CLICK_POST_HASHTAG,
            EVENT_CATEGORY, Category.CONTENT_DETAIL_PAGE_HASHTAG,
            EVENT_LABEL, "$activityId - $hashtag - - $source",
            BUSINESS_UNIT, CONTENT,
            CURRENT_SITE, MARKETPLACE,
            KEY_EVENT_USER_ID, userSessionInterface.userId,
            TRACKER_ID, "34616"
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(generalData)
    }

    /**
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 5
     *
     * @param id - either shopId or userId
     */
    fun eventHashtagPageClickNameAvatar(id: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_HASHTAG,
            Action.CLICK_AVATAR,
            id
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 6
     *
     * @param activityId - postId
     * @param hashtag - hashtag name
     * @param position - position of item in list
     */
    fun eventHashtagPageViewPost(
        activityId: String,
        hashtag: String,
        position: Int
    ) {
        trackEnhancedEcommerceEvent(
            Event.PROMO_VIEW,
            Category.CONTENT_HASHTAG,
            Action.IMPRESSION_POST,
            activityId,
            getPromoViewData(
                getPromotionsData(
                    listOf(
                        getPromotionData(
                            activityId,
                            Screen.HASHTAG_POST_LIST,
                            hashtag,
                            position
                        )
                    )
                )
            )
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     *
     */
    fun eventOpenHashtagScreen() {
        trackOpenScreenEvent(Screen.HASHTAG)
    }

    /**
     * Send all pending analytics in trackingQueue
     */
    fun sendPendingAnalytics() {
        trackingQueue.sendAll()
    }

    /**
     * Base track click read more
     */
    fun eventClickReadMore(
        eventName: String,
        eventCategory: String,
        activityId: String,
        activityName: String,
        mediaType: String
    ) {
        trackGeneralEvent(
            eventName = eventName,
            eventCategory = eventCategory,
            eventAction = "${Action.CLICK_READ_MORE} - $activityName - $mediaType",
            eventLabel = activityId
        )
    }

    /**
     * Base track click hashtag
     */
    private fun eventClickHashtag(
        eventName: String,
        eventCategory: String,
        activityId: String,
        hashtag: String
    ) {
        trackGeneralEvent(
            eventName = eventName,
            eventCategory = eventCategory,
            eventAction = Action.CLICK_HASHTAG,
            eventLabel = "$activityId - $hashtag"
        )
    }

    private fun eventClickHashtag(
        eventName: String,
        eventCategory: String,
        activityId: String,
        activityName: String,
        mediaType: String,
        hashtag: String
    ) {
        trackGeneralEvent(
            eventName = eventName,
            eventCategory = eventCategory,
            eventAction = "${Action.CLICK_HASHTAG} - $activityName - $mediaType",
            eventLabel = "$activityId - $hashtag"
        )
    }

    /**
     * Base tracker function
     */
    private fun trackGeneralEvent(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            getGeneralData(eventName, eventCategory, eventAction, eventLabel)
        )
    }

    private fun trackOpenScreenEvent(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            mapOf(
                EVENT.capitalize() to Event.OPEN_SCREEN
            )
        )
    }

    private fun trackOpenScreenEventC2s(
        screenName: String,
        isLoggedInStatus: String,
        isFeedEmpty: String
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            mapOf(
                SCREEN_DIMENSION_IS_LOGGED_IN_STATUS to isLoggedInStatus,
                SCREEN_DIMENSION_IS_FEED_EMPTY to isFeedEmpty
            )
        )
    }

    private fun trackEnhancedEcommerceEvent(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        eCommerceData: Map<String, Any>
    ) {
        trackingQueue.putEETracking(
            getGeneralData(eventName, eventCategory, eventAction, eventLabel)
                .plus(getEcommerceData(eCommerceData)) as HashMap<String, Any>
        )
    }

    private fun trackEnhancedEcommerceEventNew(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        eCommerceData: Map<String, Any>,
        trackerId: String = ""
    ) {
        if (trackerId.isEmpty()) {
            trackingQueue.putEETracking(
                getGeneralDataNew(eventName, eventCategory, eventAction, eventLabel)
                    .plus(getEcommerceData(eCommerceData)) as HashMap<String, Any>
            )
        } else {
            trackingQueue.putEETracking(
                getGeneralDataNew(eventName, eventCategory, eventAction, eventLabel)
                    .plus(KEY_TRACKER_ID to trackerId)
                    .plus(getEcommerceData(eCommerceData)) as HashMap<String, Any>

            )
        }
    }

    /**
     * Data Generator
     */
    private fun getGeneralData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ): Map<String, Any> = DataLayer.mapOf(
        EVENT, eventName,
        EVENT_CATEGORY, eventCategory,
        EVENT_ACTION, eventAction,
        EVENT_LABEL, eventLabel,
        USER_ID, userSessionInterface.userId
    )

    /**
     * Data Generator
     */
    private fun getGeneralDataNew(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ): Map<String, Any> = DataLayer.mapOf(
        EVENT, eventName,
        EVENT_CATEGORY, eventCategory,
        EVENT_ACTION, eventAction,
        EVENT_LABEL, eventLabel,
        KEY_EVENT_USER_ID, userSessionInterface.userId,
        KEY_BUSINESS_UNIT_EVENT, CONTENT,
        KEY_CURRENT_SITE_EVENT, MARKETPLACE
    )

    private fun getEcommerceData(data: Any): Map<String, Any> = DataLayer.mapOf(ECOMMERCE, data)

    private fun getPromoClickData(data: Any): Map<String, Any> =
        DataLayer.mapOf(Event.PROMO_CLICK, data)

    private fun getPromoViewData(data: Any): Map<String, Any> =
        DataLayer.mapOf(Event.PROMO_VIEW, data)

    private fun getPromotionsData(
        promotionDataList: List<Any>
    ): Map<String, Any> = DataLayer.mapOf(PROMOTIONS, promotionDataList)

    private fun getPromotionData(
        id: String,
        name: String,
        creative: String,
        position: Int,
        creativeUrl: String = "",
        category: String = "",
        promoId: String = "",
        promoCode: String = ""
    ): Map<String, Any> = DataLayer.mapOf(
        Promotion.ID, id,
        Promotion.NAME, name,
        Promotion.CREATIVE, creative,
        Promotion.POSITION, position,
        Promotion.CREATIVE_URL, creativeUrl,
        Promotion.CATEGORY, category,
        Promotion.PROMO_ID, promoId,
        Promotion.PROMO_CODE, promoCode
    )

    private fun getAddData(data: Any): Map<String, Any> = DataLayer.mapOf(Event.ADD, data)

    private fun getActionFieldData(data: Any): Map<String, Any> =
        DataLayer.mapOf(ACTION_FIELD, data)

    private fun getListData(data: Any): Map<String, Any> = DataLayer.mapOf(LIST, data)

    private fun getCurrencyData(): Map<String, Any> =
        DataLayer.mapOf(Product.CURRENCY_CODE, Product.CURRENCY_CODE_IDR)

    private fun getMediaPreviewAddData(
        role: String,
        productDataList: List<Any>
    ): Map<String, Any> = DataLayer.mapOf(
        Event.ACTION_FIELD,
        getMediaPreviewActionFieldData(role),
        PRODUCTS,
        productDataList
    )

    private fun getMediaPreviewActionFieldData(
        role: String
    ): Map<String, Any> =
        DataLayer.mapOf(LIST, Product.MEDIA_PREVIEW.replace(Product.MEDIA_PREVIEW_TAG, role))

    private fun getProductsData(
        productDataList: List<Any>
    ): Map<String, Any> = DataLayer.mapOf(PRODUCTS, productDataList)

    private fun getProductsDataList(
        productDataList: List<Any>
    ): List<Any> = DataLayer.listOf(productDataList)

    private fun getProductData(
        id: String,
        name: String,
        price: Int,
        quantity: Int,
        shopId: String,
        shopName: String,
        type: String = "",
        isFollowed: Boolean = false,
        mediaType: String = "",
        authorType: String
    ): Map<String, Any> = DataLayer.mapOf(
        Product.ID, id,
        Product.CATEGORY_ID, id,
        Product.NAME, name,
        Product.PRICE, price,
        Product.QTY, quantity,
        Product.SHOP_ID, shopId,
        Product.SHOP_NAME, shopName,
        Product.SHOP_TYPE, "",
        Product.VARIANT, "",
        Product.BRAND, "",
        Product.CATEGORY, "",
        Product.DIMENSION_40, "/feed - ${getPostType(type, isFollowed, mediaType, authorType)}"
    )

    fun getEcommerceView(listProduct: List<ProductItem>): Map<String, Any> {
        return DataLayer.mapOf(
            CURRENCY_CODE,
            CURRENCY_CODE_IDR,
            Product.IMPRESSIONS,
            getProducts(listProduct)
        )
    }

    fun getEcommerceClick(listProduct: List<ProductItem>, listSource: String): Map<String, Any> {
        return DataLayer.mapOf(Product.CLICK, getEcommerceClickValue(listProduct, listSource))
    }

    fun getEcommerceClickValue(
        listProduct: List<ProductItem>,
        listSource: String
    ): Map<String, Any> {
        return DataLayer.mapOf(
            Product.ACTION_FIELD,
            getEcommerceActionFieldValue(listSource),
            PRODUCTS,
            getProducts(listProduct)
        )
    }

    fun getEcommerceActionFieldValue(listSource: String): Map<String, Any> {
        return DataLayer.mapOf(Product.LIST, listSource)
    }

    fun getProducts(listProduct: List<ProductItem>): List<Any> {
        val list = ArrayList<Map<String, Any>>()
        for (promo in listProduct) {
            val map = createProductMap(promo)
            list.add(map)
        }
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun createProductMap(product: ProductItem): Map<String, Any> {
        val map = java.util.HashMap<String, Any>()
        map[Product.ID] = product.id.toString()
        map[Product.NAME] = product.name
        map[Product.PRICE] = product.price
        map[Product.BRAND] = product.brand
        map[Product.CATEGORY] = product.category
        map[Product.VARIANT] = product.variant
        map[Product.LIST] = product.list
        map[Product.POSITION] = product.position
        return map
    }

    private fun getProductItemList(
        id: Int,
        name: String,
        price: Int,
        brand: String,
        category: String,
        variant: String,
        list: String,
        position: Int
    ): List<ProductItem> {
        val dataList = ArrayList<ProductItem>()
        dataList.add(
            ProductItem(
                id,
                name,
                price,
                brand,
                category,
                variant,
                list,
                position
            )
        )
        return dataList
    }

    class ProductItem(
        id: Int,
        name: String,
        price: Int,
        brand: String,
        category: String,
        variant: String,
        list: String,
        position: Int
    ) {
        var id: Int = 0
            internal set
        var name: String
            internal set
        var price: Int = 0
            internal set
        var brand: String
            internal set
        var category: String
            internal set
        var variant: String
            internal set
        var list: String
            internal set
        var position: Int
            internal set

        init {
            this.id = id
            this.name = name
            this.price = price
            this.brand = brand
            this.category = category
            this.variant = variant
            this.list = list
            this.position = position
        }
    }
}
