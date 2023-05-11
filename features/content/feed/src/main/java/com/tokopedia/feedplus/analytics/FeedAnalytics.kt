package com.tokopedia.feedplus.analytics

import android.os.Bundle
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 14/04/23
 */
class FeedAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {

    private object Event {
        const val SELECT_CONTENT = "select_content"
        const val VIEW_ITEM = "view_item"
        const val VIEW_ITEM_LIST = "view_item_list"
        const val VIEW_CONTENT_IRIS = "viewContentIris"
        const val CLICK_CONTENT = "clickContent"
        const val OPEN_SCREEN = "openScreen"
        const val ADD_TO_CART = "add_to_cart"
    }

    private object Action {
        const val VIEW_POST = "view - post"
        const val CLICK_PAUSE_VIDEO = "click - screen pause video"
        const val CLICK_PRODUCT_LIST_BOTTOMSHEET = "view - product list bottomsheet"
        const val CLICK_HOLD_SEEKER_BAR_VIDEO = "click - tap hold seeker bar video"
        const val SWIPE_UP_DOWN_CONTENT = "swipe - up down content"
        const val SWIPE_RIGHT_LEFT_MULTIPLE_POST = "swipe - right left multiple post"
        const val CLICK_CTA_BUTTON_CAMPAIGN = "click - cta button campaign"
        const val CLICK_REMIND_ME_BUTTON = "click - ingatkan saya button"
        const val CLICK_ACTIVE_REMIND_ME_BUTTON = "click - pengingat aktif button"
        const val CLICK_LIKE_BUTTON = "click - like button"
        const val CLICK_DOUBLE_LIKE_BUTTON = "click - double click like"
        const val CLICK_OKE_SHARE = "click - oke share toaster"
        const val CLICK_THREE_DOTS_BUTTON = "click - three dots button"
        const val CLICK_WATCH_MODE = "click - mode nonton"
        const val CLICK_REPORT_CONTENT = "click - laporkan content"
        const val CLICK_REASON_REPORT_CONTENT = "click - reason laporkan content"
        const val VIEW_SUCCESS_REPORT_CONTENT = "view - success report content"
        const val CLICK_PRODUCT_TAG = "click - product tag"
        const val CLICK_PRODUCT_LABEL_PDP = "click - product label pdp"
        const val VIEW_VOUCHER_BOTTOMSHEET = "view - voucher bottomsheet"
        const val CLICK_PRODUCT = "click - product"
        const val CLICK_BUY_BUTTON = "click - beli button"
        const val CLICK_CART_BUTTON = "click - keranjang button"
        const val CLICK_CLOSE_PRODUCT_LIST = "click - close product list bottomsheet"
        const val CLICK_FOLLOW_BUTTON = "click - follow button"
        const val CLICK_CREATOR_NAME = "click - creator name"
        const val CLICK_CREATOR_PROFILE_PICTURE = "click - creator profile picture"
        const val CLICK_CONTENT_CAPTION = "click - content caption"
        const val CLICK_CONTENT_PRODUCT_LABEL = "click - product label bottomsheet"
        const val CLICK_LIVE_PREVIEW = "click - live preview"
    }

    private object EnhanceEcommerce {
        const val KEY_ITEM_LIST = "item_list"
        const val KEY_ITEMS = "items"
        const val KEY_PROMOTIONS = "promotions"
        const val KEY_DIMENSION40 = "dimension40"
        const val KEY_INDEX = "index"
        const val KEY_ITEM_BRAND = "item_brand"
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_ITEM_CATEGORY = "item_category"
        const val KEY_ITEM_ID = "item_id"
        const val KEY_ITEM_NAME = "item_name"
        const val KEY_ITEM_VARIANT = "item_variant"
        const val KEY_PRICE = "price"
        const val KEY_QUANTITY = "quantity"
        const val KEY_SHOP_ID = "shop_id"
        const val KEY_SHOP_NAME = "shop_name"
        const val KEY_SHOP_TYPE = "shop_type"

        const val KEY_CREATIVE_NAME = "creative_name"
        const val KEY_CREATIVE_SLOT = "creative_slot"

        const val ITEM_LIST_PRODUCT_LIST_BOTTOMSHEET = "/unified feed - product list bottomsheet"
        const val UNIFIED_FEED_CONTENT = "unified-feed-content\\\""
        const val CONTENT_IN_UNIFIED_FEED = "content in unified feed"
    }

    fun eventPostImpression(
        trackerData: FeedTrackerDataModel,
        activityId: String,
        index: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.VIEW_ITEM,
            generateGeneralTrackerBundleData(
                Event.VIEW_ITEM,
                CATEGORY_UNIFIED_FEED,
                Action.VIEW_POST,
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
                "41567"
            ).also {
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_PROMOTIONS,
                    arrayListOf(
                        Bundle().apply {
                            putString(
                                EnhanceEcommerce.KEY_CREATIVE_NAME,
                                EnhanceEcommerce.UNIFIED_FEED_CONTENT
                            )
                            putInt(EnhanceEcommerce.KEY_CREATIVE_SLOT, index + 1)
                            putString(EnhanceEcommerce.KEY_ITEM_ID, activityId)
                            putString(
                                EnhanceEcommerce.KEY_ITEM_NAME,
                                EnhanceEcommerce.CONTENT_IN_UNIFIED_FEED
                            )
                        }
                    )
                )
            }
        )
    }

    fun eventClickPauseVideo(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PAUSE_VIDEO,
                getEventLabel(trackerData),
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
                getEventLabel(trackerData),
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
        tabType: String,
        entryPoint: String,
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.SWIPE_UP_DOWN_CONTENT,
                "${getPrefix(tabType)} - $entryPoint",
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
                getEventLabel(trackerData),
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
                getEventLabel(trackerData),
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
                getEventLabel(trackerData),
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
                getEventLabel(trackerData),
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
                getEventLabel(trackerData),
                "41578"
            )
        )
    }

    fun eventShareCopyLinkSuccess(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_OKE_SHARE,
                getEventLabel(trackerData),
                "41597"
            )
        )
    }

    fun eventClickThreeDots(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_THREE_DOTS_BUTTON,
                getEventLabel(trackerData),
                "41598"
            )
        )
    }

    fun eventClickWatchMode(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_WATCH_MODE,
                getEventLabel(trackerData),
                "41599"
            )
        )
    }

    fun eventClickReportContent(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_REPORT_CONTENT,
                getEventLabel(trackerData),
                "41600"
            )
        )
    }

    fun eventClickReasonReportContent(trackerData: FeedTrackerDataModel, reportType: String) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_REASON_REPORT_CONTENT,
                "${getEventLabel(trackerData)} - $reportType",
                "41601"
            )
        )
    }

    fun eventViewSuccessReportContent(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.VIEW_CONTENT_IRIS,
                CATEGORY_UNIFIED_FEED,
                Action.VIEW_SUCCESS_REPORT_CONTENT,
                getEventLabel(trackerData),
                "41602"
            )
        )
    }

    fun eventClickProductTag(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PRODUCT_TAG,
                getEventLabel(trackerData),
                "41603"
            )
        )
    }

    fun eventClickProductLabel(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.SELECT_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PRODUCT_LABEL_PDP,
                getEventLabel(trackerData),
                "41604"
            )
        )
    }

    fun eventViewProductListBottomSheets(
        trackerData: FeedTrackerDataModel,
        productList: List<FeedCardProductModel>
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.VIEW_ITEM_LIST,
            generateGeneralTrackerBundleData(
                Event.VIEW_ITEM_LIST,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PRODUCT_LIST_BOTTOMSHEET,
                getEventLabel(trackerData),
                "41605"
            ).also {
                it.putString(
                    EnhanceEcommerce.KEY_ITEM_LIST,
                    EnhanceEcommerce.ITEM_LIST_PRODUCT_LIST_BOTTOMSHEET
                )
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_ITEMS,
                    ArrayList(productList.mapIndexed { index, feedCardProductModel ->
                        Bundle().apply {
                            putString(EnhanceEcommerce.KEY_DIMENSION40, "")
                            putString(EnhanceEcommerce.KEY_INDEX, "${index + 1}")
                            putString(
                                EnhanceEcommerce.KEY_ITEM_BRAND,
                                feedCardProductModel.shopName
                            )
                            putString(EnhanceEcommerce.KEY_ITEM_CATEGORY, "")
                            putString(EnhanceEcommerce.KEY_ITEM_ID, feedCardProductModel.id)
                            putString(EnhanceEcommerce.KEY_ITEM_NAME, feedCardProductModel.name)
                            putString(EnhanceEcommerce.KEY_ITEM_VARIANT, "")
                            putDouble(EnhanceEcommerce.KEY_PRICE, feedCardProductModel.price)
                        }
                    })
                )
            }
        )
    }

    // TODO : Implement after MVP with the MVC, currently not used
//    fun eventMvcWidgetImpression(trackerData: FeedTrackerDataModel) {
//        sendEventTracker(
//            generateGeneralTrackerData(
//                Event.VIEW_ITEM,
//                CATEGORY_UNIFIED_FEED,
//                Action.VIEW_VOUCHER_BOTTOMSHEET,
//                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
//                    getPostType(
//                        trackerData.typename,
//                        trackerData.type,
//                        trackerData.authorType,
//                        trackerData.isFollowing
//                    )
//                } - ${
//                    getContentType(
//                        trackerData.typename,
//                        trackerData.type,
//                        trackerData.mediaType
//                    )
//                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}",
//                "41606"
//            )
//        )
//        // TODO : send EE trackers
//    }

    fun eventClickProductInProductListBottomSheet(
        trackerData: FeedTrackerDataModel,
        productName: String,
        productId: String,
        productPrice: Double,
        shopName: String,
        index: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.SELECT_CONTENT,
            generateGeneralTrackerBundleData(
                Event.SELECT_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PRODUCT,
                "${getEventLabel(trackerData)} - $productId",
                "41608"
            ).also {
                it.putString(
                    EnhanceEcommerce.KEY_ITEM_LIST,
                    EnhanceEcommerce.ITEM_LIST_PRODUCT_LIST_BOTTOMSHEET
                )
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_ITEMS,
                    arrayListOf(
                        Bundle().apply {
                            putString(EnhanceEcommerce.KEY_DIMENSION40, "")
                            putString(EnhanceEcommerce.KEY_INDEX, "${index + 1}")
                            putString(
                                EnhanceEcommerce.KEY_ITEM_BRAND,
                                shopName
                            )
                            putString(EnhanceEcommerce.KEY_ITEM_CATEGORY, "")
                            putString(EnhanceEcommerce.KEY_ITEM_ID, productId)
                            putString(EnhanceEcommerce.KEY_ITEM_NAME, productName)
                            putString(EnhanceEcommerce.KEY_ITEM_VARIANT, "")
                            putDouble(EnhanceEcommerce.KEY_PRICE, productPrice)
                        }
                    ),
                )
            }
        )
    }

    fun eventClickBuyButton(
        trackerData: FeedTrackerDataModel,
        productName: String,
        productId: String,
        productPrice: Double,
        shopId: String,
        shopName: String,
        index: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.ADD_TO_CART,
            generateGeneralTrackerBundleData(
                Event.ADD_TO_CART,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_BUY_BUTTON,
                "${getEventLabel(trackerData)} - $productId",
                "41609"
            ).also {
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_ITEMS,
                    arrayListOf(
                        getProductTrackerBundle(
                            index,
                            shopName,
                            productId,
                            productName,
                            productPrice,
                            shopId
                        )
                    ),
                )
            }
        )
    }

    fun eventClickCartButton(
        trackerData: FeedTrackerDataModel,
        productName: String,
        productId: String,
        productPrice: Double,
        shopId: String,
        shopName: String,
        index: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.ADD_TO_CART,
            generateGeneralTrackerBundleData(
                Event.ADD_TO_CART,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CART_BUTTON,
                "${getEventLabel(trackerData)} - $productId",
                "41610"
            ).also {
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_ITEMS,
                    arrayListOf(
                        getProductTrackerBundle(
                            index,
                            shopName,
                            productId,
                            productName,
                            productPrice,
                            shopId
                        )
                    ),
                )
            }
        )
    }

    fun eventClickCloseProductListBottomSheet(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CLOSE_PRODUCT_LIST,
                getEventLabel(trackerData),
                "41612"
            )
        )
    }

    fun eventClickFollowButton(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_FOLLOW_BUTTON,
                getEventLabel(trackerData),
                "41613"
            )
        )
    }

    fun eventClickAuthorName(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CREATOR_NAME,
                getEventLabel(trackerData),
                "41614"
            )
        )
    }

    fun eventClickAuthorProfilePicture(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CREATOR_PROFILE_PICTURE,
                getEventLabel(trackerData),
                "41615"
            )
        )
    }

    fun eventClickContentCaption(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CONTENT_CAPTION,
                getEventLabel(trackerData),
                "41616"
            )
        )
    }

    fun eventClickContentProductLabel(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CONTENT_PRODUCT_LABEL,
                getEventLabel(trackerData),
                "41688"
            )
        )
    }

    fun eventClickLivePreview(
        trackerData: FeedTrackerDataModel,
        productId: String,
        authorName: String,
        index: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.ADD_TO_CART, addPromotionForLivePreview(
                generateGeneralTrackerBundleData(
                    Event.CLICK_CONTENT,
                    CATEGORY_UNIFIED_FEED,
                    Action.CLICK_LIVE_PREVIEW,
                    getEventLabel(trackerData),
                    "41689"
                ),
                productId,
                authorName,
                index
            )
        )
    }

    private fun addPromotionForLivePreview(
        bundle: Bundle,
        productId: String,
        authorName: String,
        index: Int
    ): Bundle {
        bundle.let {
            it.putParcelableArrayList(
                EnhanceEcommerce.KEY_PROMOTIONS,
                arrayListOf(
                    Bundle().apply {
                        putString(
                            EnhanceEcommerce.KEY_CREATIVE_NAME,
                            "live preview in feed unified"
                        )
                        putInt(EnhanceEcommerce.KEY_CREATIVE_SLOT, index + 1)
                        putString(EnhanceEcommerce.KEY_ITEM_ID, productId)
                        putString(
                            EnhanceEcommerce.KEY_ITEM_NAME,
                            authorName
                        )
                    }
                ),
            )
        }
        return bundle
    }

    private fun getEventLabel(trackerData: FeedTrackerDataModel) =
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
        } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${trackerData.entryPoint}"

    private fun getProductTrackerBundle(
        index: Int,
        shopName: String,
        productId: String,
        productName: String,
        productPrice: Double,
        shopId: String
    ) = Bundle().apply {
        putString(EnhanceEcommerce.KEY_CATEGORY_ID, "")
        putString(EnhanceEcommerce.KEY_DIMENSION40, "")
        putString(EnhanceEcommerce.KEY_INDEX, "${index + 1}")
        putString(
            EnhanceEcommerce.KEY_ITEM_BRAND,
            shopName
        )
        putString(EnhanceEcommerce.KEY_ITEM_CATEGORY, "")
        putString(EnhanceEcommerce.KEY_ITEM_ID, productId)
        putString(EnhanceEcommerce.KEY_ITEM_NAME, productName)
        putString(EnhanceEcommerce.KEY_ITEM_VARIANT, "")
        putDouble(EnhanceEcommerce.KEY_PRICE, productPrice)
        putString(EnhanceEcommerce.KEY_QUANTITY, "1")
        putString(EnhanceEcommerce.KEY_SHOP_ID, shopId)
        putString(EnhanceEcommerce.KEY_SHOP_NAME, shopName)
        putString(EnhanceEcommerce.KEY_SHOP_TYPE, "")
    }

    private fun sendEventTracker(params: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(params)
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

    private fun generateGeneralTrackerBundleData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ): Bundle = Bundle().apply {
        putString(EVENT, eventName)
        putString(EVENT_ACTION, eventAction)
        putString(EVENT_CATEGORY, eventCategory)
        putString(EVENT_LABEL, eventLabel)
        putString(KEY_EVENT_USER_ID, userSession.userId)
        putString(KEY_BUSINESS_UNIT_EVENT, BUSINESS_UNIT_CONTENT)
        putString(KEY_CURRENT_SITE_EVENT, CURRENT_SITE_MARKETPLACE)
        putString(KEY_TRACKER_ID, trackerId)
    }

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
        private const val CONTENT_TYPE_LIVE_VOD = "play vod after live"

        private const val AUTHOR_TOKOPEDIA = 1
        private const val AUTHOR_SELLER = 2
        private const val AUTHOR_USER = 3

        const val ENTRY_POINT_NAV_BUTTON = "nav button"
        const val ENTRY_POINT_SHARE_LINK = "share link"
        const val ENTRY_POINT_PUSH_NOTIF = "push notif"

        const val UNIFIED_FEED_WATCH_VIDEO_POST = "/unified feed - watch video post"

        fun getPrefix(tabType: String) =
            when (tabType) {
                FeedBaseFragment.TAB_TYPE_FOR_YOU -> "foryou"
                FeedBaseFragment.TAB_TYPE_FOLLOWING -> "following"
                else -> ""
            }

        fun getPostType(typename: String, type: String, authorType: Int, isFollowing: Boolean) =
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

        fun getContentType(typename: String, type: String, mediaType: String) = when {
            typename == FeedXCard.TYPE_FEED_X_CARD_PLAY && type == FeedXCard.TYPE_FEED_PLAY_CHANNEL -> CONTENT_TYPE_LIVE_VOD
            typename == FeedXCard.TYPE_FEED_X_CARD_PLAY && type == FeedXCard.TYPE_FEED_PLAY_SHORT_VIDEO -> CONTENT_TYPE_SHORT
            typename == FeedXCard.TYPE_FEED_X_CARD_PLAY && type == FeedXCard.TYPE_FEED_PLAY_LIVE -> CONTENT_TYPE_LIVE_PREVIEW
            else -> mediaType
        }
    }
}
