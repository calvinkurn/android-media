package com.tokopedia.feedcomponent.analytics.tracker

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.HashMap

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
        const val PRODUCTS = "products"

        const val PROMOTIONS = "promotions"
    }

    private object Event {
        const val CLICK_FEED = "clickFeed"
        const val CLICK_SOCIAL_COMMERCE = "clickSocialCommerce"
        const val OPEN_SCREEN = "openScreen"

        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
        const val ACTION_FIELD = "actionField"
        const val LIST = "list"
        const val ADD = "add"
    }

    private object Category {

        //trending
        const val CONTENT_EXPLORE_TRENDING = "explore page - trending"

        const val CONTENT_FEED_TIMELINE = "content feed timeline"
        const val FEED_DETAIL_PAGE = "feed detail page"

        const val MY_PROFILE_SOCIALCOMMERCE = "my profile socialcommerce"
        const val USER_PROFILE_SOCIALCOMMERCE = "user profile socialcommerce"

        const val CONTENT_FEED_SHOP_PAGE = "content feed - shop page"
        const val CONTENT_HASHTAG = "content hashtag"
    }

    private object Action {
        const val CLICK_BUY = "click beli"
        const val CLICK_SEE = "click lihat"
        const val CLICK_MEDIA = "click media"
        const val CLICK_SEE_ALL = "click lihat semua"
        const val CLICK_HASHTAG = "click hashtag"
        const val CLICK_READ_MORE = "click read more"
        const val CLICK_POST = "click post"
        const val CLICK_AVATAR = "click avatar"

        const val IMPRESSION_POST = "impression post"
    }

    object Screen {
        const val MEDIA_PREVIEW = "/feed/media-preview"
        const val TRENDING = "/feed/trending-tab"
        const val HASHTAG = "/feed/hashtag"
        const val HASHTAG_POST_LIST = "/hashtag page - post list"
    }

    private object Promotion {
        const val ID = "id"
        const val NAME = "name"
        const val CREATIVE = "creative"
        const val POSITION = "position"
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
    }


    private val REGEX_NUMERIC = "[^\\d]"

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 39
     *
     * @param activityId - postId
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
                Event.CLICK_FEED,
                Category.CONTENT_FEED_TIMELINE,
                Action.CLICK_AVATAR,
                activityId)
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
                Event.CLICK_FEED,
                Category.CONTENT_FEED_TIMELINE,
                Action.CLICK_SEE,
                activityId)
    }
    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 34
     *
     * @param productId - productId
     */
    fun eventMediaDetailClickBuy(role:String,
                                 productId: String,
                                 productName: String,
                                 price: String,
                                 quantity: Int,
                                 shopId: Int,
                                 shopName: String) {
        trackGeneralEvent(
                Event.CLICK_FEED,
                Category.CONTENT_FEED_TIMELINE,
                Action.CLICK_BUY,
                productId)
        trackEnhancedEcommerceEvent(Event.CLICK_FEED,
                Category.CONTENT_FEED_TIMELINE,
                Action.CLICK_BUY,
                productId,
                DataLayer.mapOf(
                        Product.CURRENCY_CODE, Product.CURRENCY_CODE_IDR,
                        Event.ADD, getAddData(
                            role,
                            getProductsData(listOf(
                                    getProductData(
                                            productId,
                                            productName,
                                            formatPriceToInt(price),
                                            quantity,
                                            shopId,
                                            shopName)))
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
                Event.CLICK_FEED,
                Category.CONTENT_EXPLORE_TRENDING,
                Action.CLICK_MEDIA,
                activityId)
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
                Event.CLICK_FEED,
                Category.CONTENT_EXPLORE_TRENDING,
                Action.CLICK_SEE_ALL,
                activityId)
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
                Event.CLICK_FEED,
                Category.CONTENT_EXPLORE_TRENDING,
                Action.CLICK_AVATAR,
                activityId)
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
    fun eventTimelineClickHashtag(activityId: String, activityName: String, mediaType: String, hashtag: String) {
        eventClickHashtag(
                Event.CLICK_FEED,
                Category.CONTENT_FEED_TIMELINE,
                activityId,
                activityName,
                mediaType,
                hashtag)
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
    fun eventDetailClickHashtag(activityId: String, activityName: String, mediaType: String, hashtag: String) {
        eventClickHashtag(
                Event.CLICK_FEED,
                Category.FEED_DETAIL_PAGE,
                activityId,
                activityName,
                mediaType,
                hashtag)
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
                hashtag)
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
                Event.CLICK_FEED,
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
    fun eventProfileClickReadMore(isOwner: Boolean, activityId: String, activityName: String, mediaType: String) {
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
                Event.CLICK_FEED,
                Category.CONTENT_FEED_SHOP_PAGE,
                activityId,
                activityName,
                mediaType
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
                                listOf(getPromotionData(activityId, Screen.HASHTAG_POST_LIST, hashtag, position))
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
            Event.CLICK_FEED,
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
                                listOf(getPromotionData(activityId, Screen.HASHTAG_POST_LIST, hashtag, position))
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
    private fun eventClickReadMore(
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
            hashtag: String) {
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
            hashtag: String) {
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
            eventLabel: String) {
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

    private fun trackEnhancedEcommerceEvent(
            eventName: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            eCommerceData: Map<String, Any>
    ) {
        trackingQueue.putEETracking(getGeneralData(eventName, eventCategory, eventAction, eventLabel)
                .plus(getEcommerceData(eCommerceData)) as HashMap<String, Any>)

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
            USER_ID,  userSessionInterface.userId
    )

    private fun getEcommerceData(data: Any): Map<String, Any> = DataLayer.mapOf(ECOMMERCE, data)

    private fun getPromoClickData(data: Any): Map<String, Any> = DataLayer.mapOf(Event.PROMO_CLICK, data)

    private fun getPromoViewData(data: Any): Map<String, Any> = DataLayer.mapOf(Event.PROMO_VIEW, data)

    private fun getPromotionsData(
            promotionDataList: List<Any>
    ): Map<String, Any> = DataLayer.mapOf(PROMOTIONS, promotionDataList)

    private fun getPromotionData(
            id: String,
            name: String,
            creative: String,
            position: Int
    ): Map<String, Any> = DataLayer.mapOf(
        Promotion.ID, id,
        Promotion.NAME, name,
        Promotion.CREATIVE, creative,
        Promotion.POSITION, position
    )


    private fun getAddData(
            role: String,
            productDataList: List<Any>
    ): Map<String, Any> =  DataLayer.mapOf(
            Event.ACTION_FIELD, getActionFieldData(role),
            PRODUCTS, productDataList
            )

    private fun getActionFieldData(role: String
    ): Map<String, Any> = DataLayer.mapOf(Event.LIST, Product.MEDIA_PREVIEW.replace(Product.MEDIA_PREVIEW_TAG,role))

    private fun getProductsData(
            productDataList: List<Any>
    ): List<Any> = DataLayer.listOf(productDataList)

    private fun getProductData(
            id: String,
            name: String,
            price: Int,
            quantity: Int,
            shopId: Int,
            shopName: String
    ): Map<String, Any> = DataLayer.mapOf(
            Product.ID, id,
            Product.NAME, name,
            Product.PRICE, price,
            Product.QTY, quantity,
            Product.SHOP_ID, shopId,
            Product.SHOP_NAME, shopName
    )
    private fun formatPriceToInt(price: String): Int {
        var result = 0
        try {
            var rex = Regex(REGEX_NUMERIC)
            result = rex.replace(price, "").toInt()
        } catch (e: Exception) {
        }
        return result
    }
}