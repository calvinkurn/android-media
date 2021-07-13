package com.tokopedia.feedcomponent.analytics.tracker

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.CLICK
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.FORMAT_THREE_PARAM
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Action.FORMAT_TWO_PARAM
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CATEGORY_FEED_TIMELINE
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CATEGORY_FEED_TIMELINE_BOTTOMSHEET
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CATEGORY_FEED_TIMELINE_COMMENT
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CATEGORY_FEED_TIMELINE_MENU
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Category.CONTENT_FEED_TIMELINE
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.CLICK_FEED
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.CONTENT
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.OPEN_SCREEN
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.PRODUCT_CLICK
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.PRODUCT_VIEW
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Event.PROMO_VIEW
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Screen.MARKETPLACE
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Screen.SCREEN_DIMENSION_IS_FEED_EMPTY
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker.Screen.SCREEN_DIMENSION_IS_LOGGED_IN_STATUS
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 2019-08-28.
 */
class FeedAnalyticTracker
@Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSessionInterface: UserSessionInterface,
) {

    private companion object {
        private const val USER_ID = "user_id"
        const val ECOMMERCE = "ecommerce"
        const val DATA = "data"
        const val PRODUCTS = "products"
        const val ACTION_FIELD = "actionField"

        const val PROMOTIONS = "promotions"
        const val LIST = "list"
        const val CURRENCY_CODE = "currencyCode"
        const val CURRENCY_CODE_IDR = "IDR"

        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_SCREEN_NAME = "screenName"
        private const val KEY_OPEN_SCREEN_EVENT = "openScreen"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_BUSINESS_UNIT_EVENT = "businessUnit"
        private const val KEY_CURRENT_SITE_EVENT = "currentSite"
        private const val KEY_EVENT_USER_ID = "userId"
        private const val KEY_SESSION_IRIS = "sessionIris"
        private const val SGC_IMAGE = "sgc image"
        private const val ASGC = "asgc"
        private const val ASGC_RECOM= "asgc recom"
        private const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT= "FeedXCardProductsHighlight"
    }

    private object Event {
        const val CLICK_FEED = "clickFeed"
        const val CLICK_SOCIAL_COMMERCE = "clickSocialCommerce"
        const val OPEN_SCREEN = "openScreen"
        const val ADD_TO_CART = "addToCart"
        const val CONTENT = "content"

        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
        const val PRODUCT_CLICK = "productClick"
        const val PRODUCT_VIEW = "productView"
        const val ACTION_FIELD = "actionField"

        const val ADD = "add"
    }

    private object Category {

        //trending
        const val CONTENT_EXPLORE_TRENDING = "explore page - trending"

        const val CONTENT_FEED_TIMELINE = "content feed timeline"
        const val FEED_DETAIL_PAGE = "feed detail page"

        const val MY_PROFILE_SOCIALCOMMERCE = "my profile socialcommerce"
        const val USER_PROFILE_SOCIALCOMMERCE = "user profile socialcommerce"

        const val CONTENT_DETAIL = "$USER_PROFILE_SOCIALCOMMERCE - content detail"
        const val MY_CONTENT_DETAIL = "$MY_PROFILE_SOCIALCOMMERCE - content detail"

        const val CONTENT_FEED_SHOP_PAGE = "content feed - shop page"
        const val CONTENT_HASHTAG = "content hashtag"
        const val CONTENT_INTEREST_PICK = "content interest pick"
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
        const val CLICK_AVATAR = "click avatar"
        const val CLICK_INTEREST = "click interest"
        const val CLICK_INTEREST_SEE_ALL = "click interest - lihat semua"
        const val CLICK_INTEREST_CHECK_ACCOUNT = "click check akun yang sesuai"
        const val CLICK_INTEREST_CHECK_INSPIRATION = "click lihat inspirasi"
        const val CLICK_PRODUCT_RECOM = "click product recommendation"
        const val CLICK_CONTENT_RECOM = "click content recommendation"
        const val CLICK_FOLLOW = "click follow"
        const val CLICK_UNFOLLOW = "click unfollow"
        const val CLICK_FOLLOW_ALL = "click follow semua"
        const val CLICK_FEED_PRODUCT_DETAIL = "click - shop"
        const val CLICK_CONTENT_DETAIL_SHOP = "click - shop"
        const val CLICK_CONTENT_DETAIL_AFFILIATE = "click - user"

        const val IMPRESSION_PRODUCT_RECOM = "impression product recommendation"
        const val IMPRESSION_CONTENT_RECOM = "impression content recommendation"
        const val IMPRESSION_POST = "impression post"


        const val PARAM_ACTION_LOGIN = "login"
        const val PARAM_ACTION_NONLOGIN = "nonlogin"
        const val ACTION_FEED_RECOM_USER = "avatar - %s recommendation - %s"
        const val ACTION_CLICK_MEDIAPREVIEW_AVATAR = "click - %s - media preview - %s"
        const val ACTION_CLICK_TOPADS_PROMOTED = "click - shop - topads shop recommendation - %s"
        const val FORMAT_TWO_PARAM = "%s - %s"
        const val FORMAT_THREE_PARAM = "%s - %s - %s"


        object Field {
            object List {
                const val POSTED_PRODUCT = "produk di post"
                const val USER_PROFILE_PAGE_POSTED_PRODUCT =
                    "${Screen.USER_PROFILE_PAGE} - $POSTED_PRODUCT"
                const val USER_PROFILE_PAGE_DETAIL_POSTED_PRODUCT =
                    "${Screen.USER_PROFILE_PAGE_DETAIL} - $POSTED_PRODUCT"
                const val FEED_POSTED_PRODUCT = "${Screen.FEED} - $POSTED_PRODUCT"
            }
        }
    }

    object Screen {
        const val FEED = "/feed"
        const val FEED_SHOP = "/shop-feed"
        const val MEDIA_PREVIEW = "$FEED/media-preview"
        const val TRENDING = "$FEED/trending-tab"
        const val HASHTAG = "$FEED/hashtag"
        const val HASHTAG_POST_LIST = "/hashtag page - post list"
        const val USER_PROFILE_PAGE = "/user profile page"
        const val USER_PROFILE_PAGE_DETAIL = "$USER_PROFILE_PAGE detail"
        const val INTEREST_PICK_DETAIL = "/feed/interest-pick"
        const val HOME_FEED_SCREEN = "/feed"
        const val ONBOARDING_PROFILE_RECOM = "/feed/profile-recom"
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
        const val SHOP_NAME = "shop_name"
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

    private object ListSource {
        const val PROFILE_RECOM_SHOP_RECOM = "/feed profile recom - shop recommendation"
        const val PROFILE_FOLLOW_RECOM_SHOP_RECOM = "/feed follow recom - shop recommendation"
        const val PROFILE_FOLLOW_RECOM_USER_RECOM = "/feed follow recom - user recommendation"
        const val PROFILE_FOLLOW_RECOM_RECOM = "/feed follow recom - {usertype} recommendation"
        const val PROFILE_FOLLOW_RECOM_RECOM_IDENTIFIER = "{usertype}"
    }

    @Inject
    lateinit var irisSession: IrisSession

    private fun getPostType(type: String, isFollowed: Boolean):String{
        return if(type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !isFollowed)
            ASGC_RECOM
        else if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && isFollowed)
            ASGC
        else
            SGC_IMAGE
    }

    private fun getIrisSessionId(): String = irisSession.getSessionId()


    //    https://docs.google.com/spreadsheets/d/1GZuybElS3H9_H_wI3z7f4Q8Y8eGZhaFnE-OK9DnYsk4/edit#gid=956196839
    //    screenshot 47
    fun eventContentDetailClickShopNameAvatar(activityId: String, shopId: String) {
        trackGeneralEvent(
            Event.CLICK_SOCIAL_COMMERCE,
            if (shopId.equals(userSessionInterface.shopId)) Category.MY_CONTENT_DETAIL else Category.CONTENT_DETAIL,
            Action.CLICK_CONTENT_DETAIL_SHOP,
            String.format(Action.FORMAT_TWO_PARAM, shopId, activityId)
        )
    }

    //    https://docs.google.com/spreadsheets/d/1GZuybElS3H9_H_wI3z7f4Q8Y8eGZhaFnE-OK9DnYsk4/edit#gid=956196839
    //    screenshot 47
    fun eventContentDetailClickAffiliateNameAvatar(activityId: String, userId: String) {
        trackGeneralEvent(
            Event.CLICK_SOCIAL_COMMERCE,
            if (userId.equals(userSessionInterface.userId)) Category.MY_CONTENT_DETAIL else Category.CONTENT_DETAIL,
            Action.CLICK_CONTENT_DETAIL_AFFILIATE,
            String.format(Action.FORMAT_TWO_PARAM, userId, activityId)
        )
    }


    //    https://docs.google.com/spreadsheets/d/1yFbEMzRj0_VdeVN7KfZIHZlv71uvX38XjfcYw7nPB3c/edit#gid=1359526861
    //    screenshot 21
    fun eventClickFeedProfileRecommendation(targetId: String, targetType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CLICK_FEED,
            CATEGORY_FEED_TIMELINE,
            Action.CLICK,
            String.format(Action.ACTION_FEED_RECOM_USER, targetType, targetId)
        )
    }

    //    https://docs.google.com/spreadsheets/d/1yFbEMzRj0_VdeVN7KfZIHZlv71uvX38XjfcYw7nPB3c/edit#gid=1359526861
    //    screenshot 19
    //https://mynakama.tokopedia.com/datatracker/requestdetail/view/942
    //1
    fun eventClickFeedAvatar(
        activityId: String,
        type: String, isFollowed: Boolean, shopId: String,
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(FORMAT_THREE_PARAM, CLICK, "shop", getPostType(type, isFollowed)),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                activityId,
                shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun evenClickMenu(activityId: String, type: String, isFollowed: Boolean, shopId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "three dots",
                getPostType(type, isFollowed)
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                activityId,
                shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickBottomSheetMenu(activityId: String,type: String,isFollowed: Boolean, shopId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_BOTTOMSHEET,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "three dots",
                getPostType(type,isFollowed)
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                activityId,
                shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }


    fun eventTagClicked(
        activityId: String, type: String,
        isFollowed: Boolean,
        shopId: String,
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "lihat produk",
                getPostType(type,isFollowed)
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                activityId,
                shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to "content",
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventImageClicked(
        activityId: String, type: String,
        isFollowed: Boolean,
        shopId: String
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "image",
               getPostType(type,isFollowed)
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                activityId,
                shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventAddToWishlistClicked(
        activityId: String, productId: String, type: String,
        isFollowed: Boolean, shopId: String,
    ) {
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_BOTTOMSHEET)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "wishlist",
                    getPostType(type, isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_THREE_PARAM,
                    activityId,
                    shopId,
                    productId
                ),
                KEY_BUSINESS_UNIT_EVENT to CONTENT,
                KEY_CURRENT_SITE_EVENT to MARKETPLACE,
                KEY_SESSION_IRIS to getIrisSessionId(),
                KEY_EVENT_USER_ID to userSessionInterface.userId
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }
    fun eventOnTagSheetItemBuyClicked(
        activityId: String, type: String,
        isFollowed: Boolean,
        shopId: String,
    ) {
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_BOTTOMSHEET)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "lihat-wishlist",
                    getPostType(type,isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    Action.FORMAT_TWO_PARAM,
                    activityId,
                    shopId
                ),
                KEY_BUSINESS_UNIT_EVENT to CONTENT,
                KEY_CURRENT_SITE_EVENT to MARKETPLACE,
                KEY_SESSION_IRIS to getIrisSessionId(),
                KEY_EVENT_USER_ID to userSessionInterface.userId
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }
    fun eventonShareProductClicked(
        activityId: String, productId: String, type: String,
        isFollowed: Boolean, shopId: String,
    ) {
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_BOTTOMSHEET)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "share",
                    getPostType(type,isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    Action.FORMAT_TWO_PARAM,
                    activityId,
                    shopId
                ),
                KEY_BUSINESS_UNIT_EVENT to CONTENT,
                KEY_CURRENT_SITE_EVENT to MARKETPLACE,
                KEY_SESSION_IRIS to getIrisSessionId(),
                KEY_EVENT_USER_ID to userSessionInterface.userId
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }
    fun eventGridProductItemClicked(
        activityId: String, productId: String, type: String,
        isFollowed: Boolean,
        shopId: String,
        products: List<FeedXProduct>
    ) {

    trackEnhancedEcommerceEventNew(
        PRODUCT_CLICK,
        CATEGORY_FEED_TIMELINE,
        String.format(
            FORMAT_THREE_PARAM,
            CLICK,
            "product",
            getPostType(type, isFollowed)
        ),
        String.format(
            FORMAT_THREE_PARAM,
            activityId,
            shopId,
            productId
        ),
        DataLayer.mapOf(CLICK , mapOf(
                    "actionField" to mapOf(
                        "list" to "feed - sgc image"
                    ),
                    "products" to getProductItemASGC(products)
                )
            )
    )

    }
    fun eventGridMoreProductCLicked(
        activityId: String, type: String,
        isFollowed: Boolean, shopId: String,
    ) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "more product",
                getPostType(type,isFollowed)
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                activityId,
                shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }


    fun eventClickLikeButton(
        activityId: String,
        doubleTap: Boolean = false,
        isLiked: Boolean,
        type: String,
        isFollowed: Boolean,
        shopId: String,
    ) {
        val likeType = if (doubleTap && isLiked)
            "double tap like"
        else if (doubleTap && !isLiked)
            "double tap unlike"
        else
            "like"
        var map = getCommonMap()
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    likeType,
                    getPostType(type, isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    Action.FORMAT_TWO_PARAM,
                    activityId,
                    shopId
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun getImpressionList(media: List<FeedXMedia>): Any {
        val list: MutableList<Map<String, String>> = mutableListOf()
        var count = 0
        for (i in media) {
            count++
            val map = mapOf(
                "creative" to i.mediaUrl,
                "category" to "",
                "id" to i.id,
                "list" to "/feed - sgc image",
                "name" to "/feed - sgc image - image",
                "position" to "{product horizontal position from $count}",
            )
            list.add(map)
        }
        return list
    }

    private fun getImpressionPostASGC(imageUrl: String, activityId: String, position: Int): Map<String, Any>  = DataLayer.mapOf(
            Promotion.CREATIVE , imageUrl,
            Promotion.ID ,activityId,
            Promotion.NAME , "/feed - asgc - post",
            Promotion.POSITION , "$position",
        )


fun eventImpressionProductASGC(
    activityId: String,
    productId: String,
    products: List<FeedXProduct>,
    shopId: String,
) {
    trackEnhancedEcommerceEventNew(
        PRODUCT_VIEW,
        CONTENT_FEED_TIMELINE,
        String.format(
            FORMAT_THREE_PARAM,
            "impression",
            "product",
            "asgc"
        ),
        String.format(
            FORMAT_THREE_PARAM,
            activityId,
            shopId,
            products[0].id
        ),
        DataLayer.mapOf(
            Product.CURRENCY_CODE, Product.CURRENCY_CODE_IDR,
            "impressions", getProductItemASGC(products))
    )

}

    fun eventImpressionPostASGC(
        activityId: String,
        position: Int,
        imageUrl: String,
        shopId: String,
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
                FORMAT_TWO_PARAM,
                activityId,
                shopId
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
        activityId: String,
        list: List<FeedXMedia>,
        type: String,
        isFollowed: Boolean,
        shopId: String
    ) {
        var map = getCommonMap(PROMO_VIEW)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "image",
                    getPostType(type,isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    Action.FORMAT_TWO_PARAM,
                    activityId,
                    shopId
                ),
                ECOMMERCE to mapOf(
                    PROMO_VIEW to getImpressionList(list)
                ),
            )
        ) as MutableMap<String, String>
        TrackApp.getInstance().gtm.sendGeneralEvent(map.toMap())
    }

    fun eventClickOpenComment(activityId: String, type: String, isFollowed: Boolean, shopId: String) {
        var map = getCommonMap()
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "comment",
                    getPostType(type, isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    Action.FORMAT_TWO_PARAM,
                    activityId,
                    shopId
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickOpenShare(activityId: String, type: String, isFollowed: Boolean, shopId: String) {
        var map = getCommonMap()
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "share",
                    getPostType(type, isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    Action.FORMAT_TWO_PARAM,
                    activityId,
                    shopId
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickReadMoreNew(activityId: String, shopId: String) {
        var map = getCommonMap()
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    " lihat selengkapnya",
                    SGC_IMAGE
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

    fun eventClickCloseProductInfoSheet(activityId: String, type: String, isFollowed: Boolean, shopId: String) {
        var map = getCommonMap()
        map = map.plus(
            mapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    " x ",
                    getPostType(type, isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    Action.FORMAT_TWO_PARAM,
                    activityId,
                    shopId
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickBSitem(
        activityId: String,
        products: List<FeedXProduct>,
        position: Int,
        type: String,
        isFollowed: Boolean,
        shopId: String
    ) {

        val map = mapOf(
            KEY_EVENT to PRODUCT_CLICK,
            KEY_EVENT_CATEGORY to CATEGORY_FEED_TIMELINE + "- bottom sheet",
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "product",
                getPostType(type, isFollowed)
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_THREE_PARAM,
                activityId,
                shopId,
                products[position].id
            ),
            ECOMMERCE to mapOf(
                CLICK to mapOf(
                    "actionField" to mapOf(
                        "list" to "feed - sgc image"
                    ),
                    "products" to getProductItem(products)
                )
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickFollowitem(
        activityId: String,
        position: Int,
        type: String,
        isFollowed: Boolean,
        shopId: String,
    ) {

        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to Category.CONTENT_FEED_TIMELINE,
            KEY_EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "follow",
                getPostType(type, isFollowed)
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                activityId,
                shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }


    fun eventClickGreyArea(activityId: String, type: String, isFollowed: Boolean, shopId: String) {
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_BOTTOMSHEET)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "grey area",
                    getPostType(type,isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    Action.FORMAT_TWO_PARAM,
                    activityId,
                    shopId
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun getProductItem(feedXProduct: List<FeedXProduct>): List<Map<String, String>> {
        val list: MutableList<Map<String, String>> = mutableListOf()
        for (i in feedXProduct) {
            val map = mapOf(
                "item_brand" to "",
                "item_category" to "",
                "item_id" to i.id,
                "list" to "/feed - sgc image",
                "name" to i.name,
                "position" to "{product horizontal position from 1}",
                "price" to i.priceFmt,
                "item_variant" to ""
            )
            list.add(map)
        }
        return list
    }

    private fun getProductItemASGC(feedXProduct: List<FeedXProduct>): List<Map<String, Any>> {
        val list: MutableList<Map<String, Any>> = mutableListOf()
        for (i in feedXProduct) {
            val map = createItemMap(i, feedXProduct.indexOf(i).toString())
            list.add(map)
        }
        return list
    }

    private fun createItemMap(feedXProduct: FeedXProduct, index: String): Map<String, Any> =
        DataLayer.mapOf(
            Product.INDEX, index,
            Product.BRAND, "",
            Product.CATEGORY, "",
            Product.ID, feedXProduct.id,
            Product.NAME, feedXProduct.name,
            Product.VARIANT, "",
            Product.PRICE,
            if (feedXProduct.isDiscount) feedXProduct.priceDiscountFmt else feedXProduct.priceFmt,
            "dimension39", "/feed - asgc detail"
        )





    fun eventCloseThreeDotBS(activityId: String, type: String, isFollowed: Boolean, shopId: String) {
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_MENU)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "x",
                    getPostType(type, isFollowed)
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
    ) {
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_MENU)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "three dots menu",
                    getPostType(type, isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    FORMAT_THREE_PARAM,
                    activityId,
                    shopId,
                    action,
                )
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickGreyAreaThreeDots(activityId: String, type: String, isFollowed: Boolean, shopId: String) {
        var map = getCommonMap(CATEGORY_FEED_TIMELINE_MENU)
        map = map.plus(
            mutableMapOf(
                KEY_EVENT_ACTION to String.format(
                    FORMAT_THREE_PARAM,
                    CLICK,
                    "grey area",
                    getPostType(type, isFollowed)
                ),
                KEY_EVENT_LABEL to String.format(
                    Action.FORMAT_TWO_PARAM,
                    activityId,
                    shopId
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
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickHashTag(hashTag: String, activityId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "hashtag",
                SGC_IMAGE
            ),
            KEY_EVENT_LABEL to String.format(
                FORMAT_THREE_PARAM,
                activityId,
                userSessionInterface.shopId,
                "$hashTag"
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickBackButtonCommentPage(actvityId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "back",
                SGC_IMAGE
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                actvityId,
                userSessionInterface.shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickShopCommentPage(actvityId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "shop",
                SGC_IMAGE
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                actvityId,
                userSessionInterface.shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickCreatorPageCommentPage(actvityId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "comment creator",
                SGC_IMAGE
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                actvityId,
                userSessionInterface.shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickReportCommentPage(actvityId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "report",
                SGC_IMAGE
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                actvityId,
                userSessionInterface.shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickDeleteCommentPage(actvityId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "delete",
                SGC_IMAGE
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                actvityId,
                userSessionInterface.shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickSendCommentPage(actvityId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "delete",
                SGC_IMAGE
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                actvityId,
                userSessionInterface.shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickKembalikanCommentPage(actvityId: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_FEED,
            EVENT_CATEGORY to CATEGORY_FEED_TIMELINE_COMMENT,
            EVENT_ACTION to String.format(
                FORMAT_THREE_PARAM,
                CLICK,
                "kembalikan to undo delete",
                SGC_IMAGE
            ),
            KEY_EVENT_LABEL to String.format(
                Action.FORMAT_TWO_PARAM,
                actvityId,
                userSessionInterface.shopId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
            KEY_EVENT_USER_ID to userSessionInterface.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }


    private fun getCommonMap(category: String = CATEGORY_FEED_TIMELINE): Map<String, String> {
        return mapOf(
            KEY_EVENT to CLICK_FEED,
            KEY_EVENT_CATEGORY to category,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_SESSION_IRIS to getIrisSessionId(),
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
                Action.ACTION_CLICK_MEDIAPREVIEW_AVATAR, targetType,
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
            String.format(Action.FORMAT_TWO_PARAM, shopId, activityId)
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
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 9 & 13
     *
     */
    fun eventClickFeedInterestPick(optionName: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_INTEREST_PICK,
            Action.CLICK_INTEREST,
            optionName
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 10
     *
     */
    fun eventClickFeedInterestPickSeeAll() {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_INTEREST_PICK,
            Action.CLICK_INTEREST_SEE_ALL,
            ""
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 11
     *
     */
    fun eventClickFeedCheckAccount(countString: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_INTEREST_PICK,
            Action.CLICK_INTEREST_CHECK_ACCOUNT,
            countString
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 9
     *
     */
    fun eventClickFeedCheckInspiration(countString: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_INTEREST_PICK,
            Action.CLICK_INTEREST_CHECK_INSPIRATION,
            countString
        )
    }

    /**
     *
     *  * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     *
     */
    fun eventOpenInterestPickDetail() {
        trackOpenScreenEvent(Screen.INTEREST_PICK_DETAIL)
    }

    /**
     *
     *  * docs: https://docs.google.com/spreadsheets/d/1IRr-k5qfzFUz43mbkZDRtjKPAbXVrWDlHus5gCIqzFg/edit#gid=1450459047
     *
     */
    fun eventOpenFeedPlusFragment(isLoggedInStatus: Boolean, isFeedEmpty: Boolean) {
        trackOpenScreenEventC2s(
            Screen.HOME_FEED_SCREEN,
            isLoggedInStatus = isLoggedInStatus.toString(),
            isFeedEmpty = isFeedEmpty.toString()
        )
    }

    /**
     *
     *  * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     *
     */
    fun eventOpenOnboardingProfileRecom() {
        trackOpenScreenEvent(Screen.ONBOARDING_PROFILE_RECOM)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 19
     *
     */
    fun eventViewContentRecommendation(userId: String, position: Int, authorType: String) {
        trackEnhancedEcommerceEvent(
            Event.PROMO_VIEW,
            Category.CONTENT_INTEREST_PICK,
            Action.IMPRESSION_CONTENT_RECOM,
            userId,
            getPromoViewData(
                getPromotionsData(
                    listOf(
                        getPromotionData(
                            userId,
                            ListSource.PROFILE_FOLLOW_RECOM_RECOM.replace(
                                ListSource.PROFILE_FOLLOW_RECOM_RECOM_IDENTIFIER,
                                authorType
                            ),
                            userSessionInterface.name,
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
     * Screenshot 19
     *
     */
    fun eventClickContentRecommendation(activityId: String, position: Int, authorType: String) {
        trackEnhancedEcommerceEvent(
            Event.PROMO_CLICK,
            Category.CONTENT_INTEREST_PICK,
            Action.CLICK_CONTENT_RECOM,
            activityId,
            getPromoClickData(
                getPromotionsData(
                    listOf(
                        getPromotionData(
                            activityId,
                            ListSource.PROFILE_FOLLOW_RECOM_RECOM.replace(
                                ListSource.PROFILE_FOLLOW_RECOM_RECOM_IDENTIFIER,
                                authorType
                            ),
                            userSessionInterface.name,
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
     * Screenshot 17
     *
     */
    fun eventClickFollowRecomNameAndImage(activityId: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_INTEREST_PICK,
            Action.CLICK_AVATAR,
            activityId
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 18
     *
     */
    fun eventClickFollowShopOrProfile(activityId: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_INTEREST_PICK,
            Action.CLICK_FOLLOW,
            activityId
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 18
     *
     */
    fun eventClickUnFollowShopOrProfile(activityId: String) {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_INTEREST_PICK,
            Action.CLICK_UNFOLLOW,
            activityId
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 20
     *
     */
    fun eventClickFollowAll() {
        trackGeneralEvent(
            CLICK_FEED,
            Category.CONTENT_INTEREST_PICK,
            Action.CLICK_FOLLOW_ALL,
            ""
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
        shopId: Int,
        shopName: String,
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
                Product.CURRENCY_CODE, Product.CURRENCY_CODE_IDR,
                Event.ADD, getMediaPreviewAddData(
                    role,
                    getProductsDataList(
                        listOf(
                            getProductData(
                                productId,
                                productName,
                                price.getDigits().toZeroIfNull(),
                                quantity,
                                shopId,
                                shopName
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
        hashtag: String,
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
        hashtag: String,
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
        mediaType: String,
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
        screenName: String, activityId: String, activityName: String, mediaType: String,
        imageUrl: String, recomId: Int, rowNumber: Int,
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
        rowNumber: Int,
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
        position: Int,
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
        position: Int,
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
     *
     * docs: https://docs.google.com/spreadsheets/d/1pnZfjiNKbAk8LR37DhNGSwm2jvM3wKqNJc2lfWLejXA/edit#gid=53652256
     *
     * @param productId - id of the product
     * @param productName - name of the product
     * @param price - price of the product
     * @param quantity - quantity of the product (usually 1)
     * @param shopId - id of the shop owner
     * @param shopName - name of the shop owner (usually "")
     */
    fun eventProfileAddToCart(
        productId: String,
        productName: String,
        price: String,
        quantity: Int,
        shopId: Int,
        shopName: String,
    ) {

        eventAddToCart(
            Category.USER_PROFILE_SOCIALCOMMERCE,
            Action.Field.List.USER_PROFILE_PAGE_POSTED_PRODUCT,
            productId,
            productName,
            price,
            quantity,
            shopId,
            shopName
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1-rVN6kBgubg1Q9tY8HMiUK58rs2-T0Hkq13GPObaJtU/edit#gid=818371047
     *
     * @param productId - id of the product
     * @param productName - name of the product
     * @param price - price of the product
     * @param quantity - quantity of the product (usually 1)
     * @param shopId - id of the shop owner
     * @param shopName - name of the shop owner (usually "")
     * @param author - type of the post author (usually user or shop)
     */
    fun eventFeedAddToCart(
        productId: String,
        productName: String,
        price: String,
        quantity: Int,
        shopId: Int,
        shopName: String,
        author: String,
    ) {

        eventAddToCart(
            Category.CONTENT_FEED_TIMELINE,
            "${Action.Field.List.FEED_POSTED_PRODUCT} - $author",
            productId,
            productName,
            price,
            quantity,
            shopId,
            shopName
        )
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1-rVN6kBgubg1Q9tY8HMiUK58rs2-T0Hkq13GPObaJtU/edit#gid=818371047
     *
     * @param productId - id of the product
     * @param productName - name of the product
     * @param price - price of the product
     * @param quantity - quantity of the product (usually 1)
     * @param shopId - id of the shop owner
     * @param shopName - name of the shop owner (usually "")
     * @param author - type of the post author (usually user or shop)
     */
    fun eventContentDetailAddToCart(
        productId: String,
        productName: String,
        price: String,
        quantity: Int,
        shopId: Int,
        shopName: String,
        author: String,
    ) {

        eventAddToCart(
            Category.CONTENT_DETAIL,
            "${Action.Field.List.USER_PROFILE_PAGE_DETAIL_POSTED_PRODUCT} - $author",
            productId,
            productName,
            price,
            quantity,
            shopId,
            shopName
        )
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
        mediaType: String,
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
        hashtag: String,
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
        hashtag: String,
    ) {
        trackGeneralEvent(
            eventName = eventName,
            eventCategory = eventCategory,
            eventAction = "${Action.CLICK_HASHTAG} - $activityName - $mediaType",
            eventLabel = "$activityId - $hashtag"
        )
    }

    /**
     * Base track addToCart
     */

    private fun eventAddToCart(
        eventCategory: String,
        actionField: String,
        productId: String,
        productName: String,
        price: String,
        quantity: Int,
        shopId: Int,
        shopName: String,
    ) {
        trackEnhancedEcommerceEvent(
            Event.ADD_TO_CART,
            eventCategory,
            Action.CLICK_BUY,
            productId,
            eCommerceData = getCurrencyData() +
                    getAddData(
                        getActionFieldData(getListData(actionField)) +
                                getProductsData(
                                    listOf(
                                        getProductData(
                                            productId,
                                            productName,
                                            price.getDigits().toZeroIfNull(),
                                            quantity,
                                            shopId,
                                            shopName
                                        )
                                    )
                                )
                    )
        )
    }

    /**
     * Base tracker function
     */
    private fun trackGeneralEvent(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
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
        isFeedEmpty: String,
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
        eCommerceData: Map<String, Any>,
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
    ) {
        trackingQueue.putEETracking(
            getGeneralDataNew(eventName, eventCategory, eventAction, eventLabel)
                .plus(getEcommerceData(eCommerceData)) as HashMap<String, Any>
        )

    }

    /**
     * Data Generator
     */
    private fun getGeneralData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
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
        eventLabel: String,
    ): Map<String, Any> = DataLayer.mapOf(
        EVENT, eventName,
        EVENT_CATEGORY, eventCategory,
        EVENT_ACTION, eventAction,
        EVENT_LABEL, eventLabel,
        KEY_EVENT_USER_ID, userSessionInterface.userId,
        KEY_BUSINESS_UNIT_EVENT , CONTENT,
        KEY_CURRENT_SITE_EVENT , MARKETPLACE
    )

    private fun getEcommerceData(data: Any): Map<String, Any> = DataLayer.mapOf(ECOMMERCE, data)

    private fun getPromoClickData(data: Any): Map<String, Any> =
        DataLayer.mapOf(Event.PROMO_CLICK, data)

    private fun getPromoViewData(data: Any): Map<String, Any> =
        DataLayer.mapOf(Event.PROMO_VIEW, data)

    private fun getPromotionsData(
        promotionDataList: List<Any>,
    ): Map<String, Any> = DataLayer.mapOf(PROMOTIONS, promotionDataList)

    private fun getPromotionData(
        id: String,
        name: String,
        creative: String,
        position: Int,
        creativeUrl: String = "",
        category: String = "",
        promoId: String = "",
        promoCode: String = "",
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
        productDataList: List<Any>,
    ): Map<String, Any> = DataLayer.mapOf(
        Event.ACTION_FIELD, getMediaPreviewActionFieldData(role),
        PRODUCTS, productDataList
    )

    private fun getMediaPreviewActionFieldData(
        role: String,
    ): Map<String, Any> =
        DataLayer.mapOf(LIST, Product.MEDIA_PREVIEW.replace(Product.MEDIA_PREVIEW_TAG, role))

    private fun getProductsData(
        productDataList: List<Any>,
    ): Map<String, Any> = DataLayer.mapOf(PRODUCTS, productDataList)

    private fun getProductsDataList(
        productDataList: List<Any>,
    ): List<Any> = DataLayer.listOf(productDataList)

    private fun getProductData(
        id: String,
        name: String,
        price: Int,
        quantity: Int,
        shopId: Int,
        shopName: String,
    ): Map<String, Any> = DataLayer.mapOf(
        Product.ID, id,
        Product.NAME, name,
        Product.PRICE, price,
        Product.QTY, quantity,
        Product.SHOP_ID, shopId,
        Product.SHOP_NAME, shopName
    )

    fun getEcommerceView(listProduct: List<ProductItem>): Map<String, Any> {
        return DataLayer.mapOf(
            CURRENCY_CODE, CURRENCY_CODE_IDR,
            Product.IMPRESSIONS, getProducts(listProduct)
        )
    }

    fun getEcommerceClick(listProduct: List<ProductItem>, listSource: String): Map<String, Any> {
        return DataLayer.mapOf(Product.CLICK, getEcommerceClickValue(listProduct, listSource))
    }

    fun getEcommerceClickValue(
        listProduct: List<ProductItem>,
        listSource: String,
    ): Map<String, Any> {
        return DataLayer.mapOf(
            Product.ACTION_FIELD, getEcommerceActionFieldValue(listSource),
            PRODUCTS, getProducts(listProduct)
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
        id: Int, name: String, price: Int, brand: String, category: String,
        variant: String, list: String, position: Int,
    )
            : List<ProductItem> {
        val dataList = ArrayList<ProductItem>()
        dataList.add(
            ProductItem(
                id, name, price, brand, category, variant, list, position
            )
        )
        return dataList
    }

    class ProductItem(
        id: Int, name: String, price: Int, brand: String, category: String,
        variant: String, list: String, position: Int,
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