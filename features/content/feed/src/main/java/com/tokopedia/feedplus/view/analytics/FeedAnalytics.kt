package com.tokopedia.feedplus.view.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author.Companion.TYPE_AFFILIATE
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking.Ecommerce.getEcommerceClick
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking.Ecommerce.getEcommerceView
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking.Event.PROMO_CLICK
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking.Event.PROMO_VIEW
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.indices
import kotlin.collections.set

/**
 * @author by astidhiyaa on 30/08/22
 */
class FeedAnalytics @Inject constructor(
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue
) {

    object Element {
        const val AVATAR = "avatar"
        const val IMAGE = "image"
        const val TAG = "tag"
        const val SHARE = "share"
        const val FOLLOW = "follow"
        const val UNFOLLOW = "unfollow"
        const val OPTION = "option "
        const val VIDEO = "video"
        const val PRODUCT = "product"
        const val LIKE = "like"
        const val UNLIKE = "unlike"
        const val COMMENT = "comment"
    }

    fun trackScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    /**
     * Send all pending analytics in trackingQueue
     */
    fun sendPendingAnalytics() {
        trackingQueue.sendAll()
    }

    private fun trackEnhancedEcommerceEvent(
        eventData: HashMap<String, Any>?
    ) {
        trackingQueue.putEETracking(eventData)
    }

    fun eventFeedViewShop(screenName: String, shopId: String, label: String) {
        val mapEvent = HashMap<String, Any>()
        mapEvent["screenName"] = screenName
        mapEvent["event"] = EVENT_CLICK_FEED
        mapEvent["eventCategory"] = CATEGORY_FEED
        mapEvent["eventAction"] = ACTION_VIEW
        mapEvent["eventLabel"] = label
        mapEvent["userId"] = userSession.userId
        mapEvent["productId"] = "0"
        mapEvent["shopId"] = shopId
        mapEvent["promoId"] = "0"
        trackEnhancedEcommerceEvent(mapEvent)
    }

    fun eventFeedClickProduct(screenName: String, productId: String, label: String) {
        val mapEvent = HashMap<String, Any>()
        mapEvent["screenName"] = screenName
        mapEvent["event"] = EVENT_CLICK_FEED
        mapEvent["eventCategory"] = CATEGORY_FEED
        mapEvent["eventAction"] = ACTION_CLICK
        mapEvent["eventLabel"] = label
        mapEvent["userId"] = userSession.userId
        mapEvent["productId"] = productId
        mapEvent["shopId"] = "0"
        mapEvent["promoId"] = "0"
        trackEnhancedEcommerceEvent(mapEvent)
    }

    fun eventFeedClickShop(screenName: String, shopId: String, label: String) {
        val mapEvent = HashMap<String, Any>()
        mapEvent["screenName"] = screenName
        mapEvent["event"] = EVENT_CLICK_FEED
        mapEvent["eventCategory"] = CATEGORY_FEED
        mapEvent["eventAction"] = ACTION_CLICK
        mapEvent["eventLabel"] = label
        mapEvent["userId"] = userSession.userId
        mapEvent["productId"] = "0"
        mapEvent["shopId"] = shopId
        mapEvent["promoId"] = "0"
        trackEnhancedEcommerceEvent(mapEvent)
    }

    fun eventTrackingEnhancedEcommerce(trackingData: HashMap<String, Any>) {
        trackEnhancedEcommerceEvent(trackingData)
    }

    private fun getEventEcommerceView(
        action: String, label: String,
        promotions: List<FeedEnhancedTracking.Promotion>,
        userId: Long
    ): HashMap<String, Any>? {
        return DataLayer.mapOf(
            EVENT_NAME, PROMO_VIEW,
            EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
            EVENT_ACTION, action,
            EVENT_LABEL, label,
            KEY_USER_ID, userId,
            KEY_USER_ID_MOD, userId % 50,
            EVENT_ECOMMERCE, getEcommerceView(promotions)
        ) as? HashMap<String, Any>
    }

    private fun getEventEcommerceClick(
        action: String,
        label: String,
        promotions: List<FeedEnhancedTracking.Promotion>,
        userId: Long
    ): HashMap<String, Any>? {
        return DataLayer.mapOf(
            EVENT_NAME, PROMO_CLICK,
            EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
            EVENT_ACTION, action,
            EVENT_LABEL, label,
            KEY_USER_ID, userId,
            KEY_USER_ID_MOD, userId % 50,
            EVENT_ECOMMERCE, getEcommerceClick(promotions)
        ) as? HashMap<String, Any>?
    }

    private fun singleOrMultiple(totalContent: Int): String {
        return if (totalContent == 1) SINGLE else MULTIPLE
    }

    fun eventTopadsRecommendationImpression(
        trackingList: List<TrackingRecommendationModel>,
        userId: Long
    ) {
        val firstAuthorId = if (trackingList.isEmpty()) "0" else trackingList[0].authorId
        val promotionList: MutableList<FeedEnhancedTracking.Promotion> = ArrayList()
        for ((templateType, _, _, _, _, _, authorId, cardPosition, adId) in trackingList) {
            promotionList.add(
                FeedEnhancedTracking.Promotion(
                    authorId,
                    "/content feed - topads - shop",
                    "",
                    "",
                    cardPosition,
                    "",
                    adId,
                    templateType
                )
            )
        }
        trackEnhancedEcommerceEvent(
            getEventEcommerceView(
                "impression - topads shop recommendation", firstAuthorId,
                promotionList,
                userId
            )
        )
    }

    // docs : https://docs.google.com/spreadsheets/d/1pnZfjiNKbAk8LR37DhNGSwm2jvM3wKqNJc2lfWLejXA/edit#gid=1878700964
    // screenshot 13
    fun eventTopadsRecommendationClick(
        templateType: String?, adId: String?, authorId: String,
        cardPosition: Int, userId: Long
    ) {
        val promotionList: MutableList<FeedEnhancedTracking.Promotion> = ArrayList()
        promotionList.add(
            FeedEnhancedTracking.Promotion(
                authorId,
                "/content feed - topads - shop",
                "",
                "",
                cardPosition,
                "",
                adId!!,
                templateType!!
            )
        )
        trackEnhancedEcommerceEvent(
            getEventEcommerceClick(
                "click", String.format(Locale.getDefault(),"avatar - topads shop recommendation - %s", authorId),
                promotionList,
                userId
            )
        )
    }

    fun eventFollowRecommendation(action: String?, authorType: String?, authorId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EVENT_CLICK_FEED,
            CONTENT_FEED_TIMELINE,
            String.format(Locale.getDefault(),"click %s - %s recommendation", action, authorType),
            authorId
        )
    }

    fun eventFollowCardPost(
        action: String?, activityName: String?, activityId: String?,
        mediaType: String?
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EVENT_CLICK_FEED,
            CONTENT_FEED_TIMELINE,
            String.format(Locale.getDefault(),"click %s - %s - %s", action, activityName, mediaType),
            activityId
        )
    }

    //#FEED015
    fun trackClickCreatePost(userId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EVENT_CLICK_FEED,
            CATEGORY_FEED,
            "click buat post",
            userId
        )
    }

    //#FEED016
    fun trackClickCreatePostAs(type: String, userId: String?, shopId: String?) {
        if (type.toLowerCase().contains(TYPE_AFFILIATE)) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_FEED,
                CATEGORY_FEED,
                "click post sebagai - user",
                userId
            )
        } else {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_FEED,
                CATEGORY_FEED,
                "click post sebagai - shop",
                shopId
            )
        }
    }

    fun eventCardPostClick(
        templateType: String?, activityName: String?, mediaType: String?,
        redirectUrl: String?, imageUrl: String?, authorId: String?,
        totalContent: Int, postId: String?, userId: Long,
        feedPosition: Int, recomId: Long
    ) {
        val promotionList: MutableList<FeedEnhancedTracking.Promotion> = ArrayList()
        promotionList.add(
            FeedEnhancedTracking.Promotion(
                postId!!, String.format(Locale.getDefault(),"/content feed - %s - %s", activityName, mediaType),
                imageUrl!!,
                redirectUrl!!,
                feedPosition,
                "",
                authorId!!, String.format(Locale.getDefault(),"%s - %s", templateType, singleOrMultiple(totalContent))
            )
        )
        trackEnhancedEcommerceEvent(
            getEventEcommerceClick(
                "click",
                String.format(POST_FORMAT_4_VALUE, activityName, mediaType, postId, recomId),
                promotionList,
                userId
            )
        )
    }

    fun eventProductGridImpression(
        productList: List<ProductEcommerce>,
        activityName: String?, postId: Int, userId: Int, recomId: Int
    ) {
        trackEnhancedEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, PRODUCT_VIEW,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                EVENT_ACTION, String.format(Locale.getDefault(),"impression product - %s", activityName),
                EVENT_LABEL, String.format(Locale.getDefault(),FORMAT_2_VALUE, postId, recomId),
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % 50,
                EVENT_ECOMMERCE, getProductEcommerceImpressions(
                    productList,
                    "/feed - system generated content"
                )
            ) as? HashMap<String, Any>?
        )
    }

    fun eventProductGridClick(
        product: ProductEcommerce,
        activityName: String?, postId: Int, userId: Int, recomId: Int
    ) {
        trackEnhancedEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, PRODUCT_CLICK,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                EVENT_ACTION, String.format(Locale.getDefault(),"click product - %s", activityName),
                EVENT_LABEL, String.format(Locale.getDefault(),FORMAT_2_VALUE, postId, recomId),
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % 50,
                EVENT_ECOMMERCE, getProductEcommerceClick(
                    product,
                    "/feed - system generated content"
                )
            ) as? HashMap<String, Any>?
        )
    }

    fun eventVoteImpression(
        activityName: String?, mediaType: String?, pollId: String,
        postId: String?, userId: Long
    ) {
        val promotionList: MutableList<FeedEnhancedTracking.Promotion> = ArrayList()
        promotionList.add(
            FeedEnhancedTracking.Promotion(
                postId!!, String.format(Locale.getDefault(),"/content feed - %s - %s", activityName, mediaType),
                "",
                "",
                0,
                "",
                "",
                ""
            )
        )
        trackEnhancedEcommerceEvent(
            getEventEcommerceView(
                String.format(Locale.getDefault(),"impression - %s - %s", activityName, mediaType),
                pollId,
                promotionList,
                userId
            )
        )
    }

    fun eventVoteClick(
        activityName: String?, mediaType: String?, pollId: String?, optionId: String?,
        optionName: String?, imageUrl: String?, postId: String?, userId: Long
    ) {
        val promotionList: MutableList<FeedEnhancedTracking.Promotion> = ArrayList()
        promotionList.add(
            FeedEnhancedTracking.Promotion(
                postId!!, String.format(Locale.getDefault(),"/content feed - %s - %s", activityName, mediaType),
                optionId!!,
                imageUrl!!,
                0,
                "",
                "0",
                ""
            )
        )
        trackEnhancedEcommerceEvent(
            getEventEcommerceView(
                "click",
                String.format(
                    Locale.getDefault(),
                    "post - %s - %s - %s - %s - %s",
                    activityName,
                    mediaType,
                    pollId,
                    optionId,
                    optionName
                ),
                promotionList,
                userId
            )
        )
    }

    fun eventGoToFeedDetail(postId: Int, recomId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EVENT_CLICK_FEED,
            CONTENT_FEED_TIMELINE,
            "click product to feed detail", String.format(FORMAT_2_VALUE, postId, recomId)
        )
    }

    fun eventDetailProductImpression(productList: List<ProductEcommerce>, userId: Int) {
        val firstProductId = if (productList.isEmpty()) "0" else productList[0].productId
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT_NAME, PRODUCT_VIEW,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE_DETAIL,
                EVENT_ACTION, "impression product",
                EVENT_LABEL, firstProductId,
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % 50,
                EVENT_ECOMMERCE, getProductEcommerceImpressions(
                    productList,
                    "/feed detail - product list"
                )
            )
        )
    }

    fun getPostType(type: String, isFollowed: Boolean): String {
        return if (type === TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !isFollowed) ASGC_RECOM else if (type === TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && isFollowed) ASGC else ASGC
    }

    fun eventDetailProductClick(
        product: ProductEcommerce,
        userId: String,
        shopId: String,
        activityId: String,
        type: String,
        isFollowed: Boolean,
        trackerId: String,
        campaignStatus: String,
        contentScore : String
    ) {
        val eventAction = ACTION_CLICK_PRODUCT + " - " + getPostType(type, isFollowed)
        val eventLabel = if (campaignStatus.isEmpty()) {
            activityId + " - " + shopId + " - " + contentScore + " - " + product.productId
        } else {
            activityId + " - " + shopId + " - " + contentScore + " - " + campaignStatus + " - " + product.productId
        }
        val map = DataLayer.mapOf(
            EVENT_NAME,
            PRODUCT_CLICK,
            EVENT_CATEGORY,
            CONTENT_FEED_TIMELINE_DETAIL,
            EVENT_ACTION,
            eventAction,
            EVENT_LABEL,
            eventLabel,
            KEY_USER_ID,
            userId,
            KEY_BUSINESS_UNIT_EVENT,
            KEY_BUSINESS_UNIT,
            KEY_CURRENT_SITE_EVENT,
            KEY_CURRENT_SITE,
            EVENT_ECOMMERCE,
            getProductEcommerceClick(
                product,
                "/feed - " + getPostType(type, isFollowed)
            )
        )
        if (trackerId.isNotEmpty()) map[KEY_TRACKER_ID] = trackerId
        trackEnhancedEcommerceEvent(map as? HashMap<String, Any>?)
    }

    fun eventNewPostClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT_NAME, EVENT_CLICK_FEED,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                EVENT_ACTION, POST_CLICK_VALUE,
                EVENT_LABEL, ""
            )
        )
    }

    private fun getProductEcommerceImpressions(
        productList: List<ProductEcommerce>,
        list: String
    ): HashMap<String, Any> {
        val products = ArrayList<Any>()
        for ((productId, productName, productPrice, position) in productList) {
            val productItem = HashMap<String, Any>()
            productItem["name"] = productName
            productItem["id"] = productId
            productItem["price"] = formatStringToInt(productPrice)
            productItem["list"] = list
            productItem["position"] = position
            productItem["brand"] = ""
            productItem["variant"] = ""
            productItem["category"] = ""
            products.add(productItem)
        }
        val ecommerce = HashMap<String, Any>()
        ecommerce["currencyCode"] = "IDR"
        ecommerce["impressions"] = products
        return ecommerce
    }

    private fun getProductEcommerceClick(
        product: ProductEcommerce,
        list: String
    ): HashMap<String, Any> {
        val productItem = HashMap<String, Any>()
        productItem["name"] = product.productName
        productItem["id"] = product.productId
        productItem["price"] = formatStringToInt(product.productPrice)
        productItem["list"] = list
        productItem["position"] = product.position + 1
        val products = ArrayList<Any>()
        products.add(productItem)
        val actionField = HashMap<String, Any>()
        actionField["list"] = list
        val click = HashMap<String, Any>()
        click["actionField"] = actionField
        click["products"] = products
        val ecommerce = HashMap<String, Any>()
        ecommerce["click"] = click
        return ecommerce
    }

    private fun formatStringToInt(price: String): Int {
        return try {
            Integer.valueOf(price.replace("[^\\d]".toRegex(), ""))
        } catch (e: Exception) {
            0
        }
    }

    fun sendFeedTopAdsHeadlineAdsImpression(
        eventAction: String?,
        eventLabel: String?,
        adId: String,
        position: Int,
        userId: String?
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, PROMO_VIEW,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, eventLabel,
                KEY_BUSINESS_UNIT_EVENT, KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT, KEY_CURRENT_SITE,
                KEY_USER_ID, userId,
                EVENT_ECOMMERCE, DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.mapOf(
                        PROMOTIONS, getHeadlineList(adId, position)
                    )
                )
            )
        )
    }

    fun sendFeedTopAdsHeadlineProductImpression(
        eventAction: String?,
        eventLabel: String?,
        products: List<Product>,
        position: Int,
        userId: String?
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, PRODUCT_VIEW,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, eventLabel,
                KEY_BUSINESS_UNIT_EVENT, KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT, KEY_CURRENT_SITE,
                KEY_USER_ID, userId,
                EVENT_ECOMMERCE, DataLayer.mapOf(
                    "currencyCode", "IDR",
                    "impressions", getProductList(products, position + 1)
                )
            )
        )
    }

    fun sendFeedTopAdsHeadlineProductClick(
        eventAction: String?,
        eventLabel: String?,
        products: List<Product>,
        position: Int,
        userId: String?
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT_NAME, PRODUCT_CLICK,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, eventLabel,
                KEY_BUSINESS_UNIT_EVENT, KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT, KEY_CURRENT_SITE,
                KEY_USER_ID, userId,
                EVENT_ECOMMERCE, DataLayer.mapOf(
                    "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                            "list", "/feed - topads"
                        ),
                        "products", getProductList(products, position)
                    )
                )
            )
        )
    }

    private fun getHeadlineList(adId: String, position: Int): List<Any> {
        val productList = ArrayList<Any>()
        val productItem = HashMap<String, Any>()
        productItem["name"] = "/feed - topads - card"
        productItem["id"] = adId
        productItem["position"] = position
        productItem["creative_name"] = ""
        productList.add(productItem)
        return productList
    }

    private fun getProductList(items: List<Product>, position: Int): List<Any> {
        val productList = ArrayList<Any>()
        for (i in items.indices) {
            val productItem = HashMap<String, Any>()
            productItem["name"] = items[i].name
            productItem["id"] = items[i].id
            productItem["position"] = position
            productItem["list"] = "/feed - topads"
            productItem["price"] = formatStringToInt(items[i].priceFormat)
            productItem["variant"] = ""
            productItem["brand"] = ""
            productItem["category"] = ""
            productItem["creative_name"] = items[i].image.m_url
            productList.add(productItem)
        }
        return productList
    }

    fun sendTopAdsHeadlineClickevent(eventAction: String?, eventLabel: String?, userId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT_NAME, EVENT_CLICK_FEED,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, eventLabel,
                KEY_BUSINESS_UNIT_EVENT, KEY_BUSINESS_UNIT,
                KEY_CURRENT_SITE_EVENT, KEY_CURRENT_SITE,
                KEY_USER_ID, userId
            )
        )
    }

    companion object {
        private const val EVENT_CLICK_FEED = "clickFeed"
        private const val EVENT_NAME = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
        private const val EVENT_ECOMMERCE = "ecommerce"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_ID_MOD = "userIdmodulo"
        private const val KEY_BUSINESS_UNIT = "content"
        private const val KEY_CURRENT_SITE = "tokopediamarketplace"
        private const val KEY_BUSINESS_UNIT_EVENT = "businessUnit"
        private const val KEY_CURRENT_SITE_EVENT = "currentSite"
        private const val CATEGORY_FEED = "Feed"
        private const val ACTION_IMPRESSION = "Impression"
        private const val ACTION_VIEW = "View"
        private const val ACTION_CLICK = "Click"
        private const val ACTION_CLICK_PRODUCT = "click - product"
        private const val PRODUCT_VIEW = "productView"
        private const val PRODUCT_CLICK = "productClick"
        private const val PROMOTIONS = "promotions"
        private const val POST_CLICK_VALUE = "click new post"
        private const val DASH = " - "
        private const val SINGLE = "single"
        private const val MULTIPLE = "multiple"
        private const val FORMAT_2_VALUE = "%s - %s"
        private const val FORMAT_4_VALUE = "%s - %s - %s - %s"
        private const val POST_FORMAT_4_VALUE = "post - %s - %s - %s - %s"

        //region Content Feed
        private const val CONTENT_FEED = "content feed"
        private const val CONTENT_FEED_TIMELINE = "content feed timeline"
        private const val CONTENT_FEED_TIMELINE_DETAIL = "content feed timeline - product detail"
        private const val CONTENT_FEED_TIMELINE_BOTTOM_SHEET =
            "content feed timeline - bottom sheet"
        private const val ASGC = "asgc"
        private const val ASGC_RECOM = "asgc recom"
    }
}
