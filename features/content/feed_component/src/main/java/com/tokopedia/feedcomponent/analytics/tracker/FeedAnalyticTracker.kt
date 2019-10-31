package com.tokopedia.feedcomponent.analytics.tracker

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.collections.List

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
        const val DATA = "data"
        const val PRODUCTS = "products"
        const val ACTION_FIELD = "actionField"

        const val PROMOTIONS = "promotions"
        const val LIST = "list"
        const val CURRENCY_CODE = "currencyCode"
        const val CURRENCY_CODE_IDR = "IDR"
    }

    private object Event {
        const val CLICK_FEED = "clickFeed"
        const val CLICK_SOCIAL_COMMERCE = "clickSocialCommerce"
        const val OPEN_SCREEN = "openScreen"
        const val ADD_TO_CART = "addToCart"

        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
        const val PRODUCT_CLICK = "productClick"
        const val PRODUCT_VIEW = "productView"
        const val ECOMMERCE = "ecommerce"
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

        const val CONTENT_FEED_SHOP_PAGE = "content feed - shop page"
        const val CONTENT_HASHTAG = "content hashtag"
        const val CONTENT_INTEREST_PICK = "content interest pick"
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
        const val CLICK_INTEREST = "click interest"
        const val CLICK_INTEREST_SEE_ALL = "click interest - lihat semua"
        const val CLICK_INTEREST_CHECK_ACCOUNT = "click check akun yang sesuai"
        const val CLICK_INTEREST_CHECK_INSPIRATION = "click lihat inspirasi"
        const val CLICK_PRODUCT_RECOM = "click product recommendation"
        const val CLICK_CONTENT_RECOM = "click content recommendation"
        const val CLICK_FOLLOW = "click follow"
        const val CLICK_UNFOLLOW = "click unfollow"
        const val CLICK_FOLLOW_ALL = "click follow semua"

        const val IMPRESSION_PRODUCT_RECOM = "impression product recommendation"
        const val IMPRESSION_CONTENT_RECOM = "impression content recommendation"
        const val IMPRESSION_POST = "impression post"

        object Field {
            object List {
                const val POSTED_PRODUCT = "produk di post"
                const val USER_PROFILE_PAGE_POSTED_PRODUCT = "${Screen.USER_PROFILE_PAGE} - $POSTED_PRODUCT"
                const val USER_PROFILE_PAGE_DETAIL_POSTED_PRODUCT = "${Screen.USER_PROFILE_PAGE_DETAIL} - $POSTED_PRODUCT"
                const val FEED_POSTED_PRODUCT = "${Screen.FEED} - $POSTED_PRODUCT"
            }
        }
    }

    object Screen {
        const val FEED = "/feed"
        const val MEDIA_PREVIEW = "$FEED/media-preview"
        const val TRENDING = "$FEED/trending-tab"
        const val HASHTAG = "$FEED/hashtag"
        const val HASHTAG_POST_LIST = "/hashtag page - post list"
        const val USER_PROFILE_PAGE = "/user profile page"
        const val USER_PROFILE_PAGE_DETAIL = "$USER_PROFILE_PAGE detail"
        const val INTEREST_PICK_DETAIL = "/feed/interest-pick"
        const val ONBOARDING_PROFILE_RECOM = "/feed/profile-recom"
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
        const val BRAND = "brand"
        const val CATEGORY = "category"
        const val VARIANT = "variant"
        const val LIST = "list"
        const val POSITION = "position"
        const val ACTION_FIELD = "actionField"
        const val CLICK = "click"
        const val IMPRESSIONS = "impressions"
    }

    private object ListSource {
        const val PROFILE_RECOM_SHOP_RECOM = "/feed profile recom - shop recommendation"
        const val PROFILE_FOLLOW_RECOM_SHOP_RECOM = "/feed follow recom - shop recommendation"
        const val PROFILE_FOLLOW_RECOM_USER_RECOM = "/feed follow recom - user recommendation"
        const val PROFILE_FOLLOW_RECOM_RECOM = "/feed follow recom - {usertype} recommendation"
        const val PROFILE_FOLLOW_RECOM_RECOM_IDENTIFIER ="{usertype}"
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 9 & 13
     *
     */
    fun eventClickFeedInterestPick(optionName: String) {
        trackGeneralEvent(
                Event.CLICK_FEED,
                Category.CONTENT_INTEREST_PICK,
                Action.CLICK_INTEREST,
                optionName)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 10
     *
     */
    fun eventClickFeedInterestPickSeeAll() {
        trackGeneralEvent(
                Event.CLICK_FEED,
                Category.CONTENT_INTEREST_PICK,
                Action.CLICK_INTEREST_SEE_ALL,
                "")
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 11
     *
     */
    fun eventClickFeedCheckAccount(countString: String) {
        trackGeneralEvent(
                Event.CLICK_FEED,
                Category.CONTENT_INTEREST_PICK,
                Action.CLICK_INTEREST_CHECK_ACCOUNT,
                countString)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 9
     *
     */
    fun eventClickFeedCheckInspiration(countString: String) {
        trackGeneralEvent(
                Event.CLICK_FEED,
                Category.CONTENT_INTEREST_PICK,
                Action.CLICK_INTEREST_CHECK_INSPIRATION,
                countString)
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
                                listOf(getPromotionData(
                                        userId,
                                        ListSource.PROFILE_FOLLOW_RECOM_RECOM.replace(ListSource.PROFILE_FOLLOW_RECOM_RECOM_IDENTIFIER, authorType),
                                        userSessionInterface.name,
                                        position))
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
                                listOf(getPromotionData(
                                        activityId,
                                        ListSource.PROFILE_FOLLOW_RECOM_RECOM.replace(ListSource.PROFILE_FOLLOW_RECOM_RECOM_IDENTIFIER, authorType),
                                        userSessionInterface.name,
                                        position))
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
                Event.CLICK_FEED,
                Category.CONTENT_INTEREST_PICK,
                Action.CLICK_AVATAR,
                activityId)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 18
     *
     */
    fun eventClickFollowShopOrProfile(activityId: String) {
        trackGeneralEvent(
                Event.CLICK_FEED,
                Category.CONTENT_INTEREST_PICK,
                Action.CLICK_FOLLOW,
                activityId)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 18
     *
     */
    fun eventClickUnFollowShopOrProfile(activityId: String) {
        trackGeneralEvent(
                Event.CLICK_FEED,
                Category.CONTENT_INTEREST_PICK,
                Action.CLICK_UNFOLLOW,
                activityId)
    }

    /**
     *
     * docs: https://docs.google.com/spreadsheets/d/1hEISViRaJQJrHTo0MiDd7XjDWe1YPpGnwDKmKCtZDJ8/edit#gid=85816589
     * Screenshot 20
     *
     */
    fun eventClickFollowAll() {
        trackGeneralEvent(
                Event.CLICK_FEED,
                Category.CONTENT_INTEREST_PICK,
                Action.CLICK_FOLLOW_ALL,
                "")
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
    fun eventMediaDetailClickBuy(role: String,
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
                        Event.ADD, getMediaPreviewAddData(
                        role,
                        getProductsDataList(listOf(
                                getProductData(
                                        productId,
                                        productName,
                                        price.getDigits().toZeroIfNull(),
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
    fun eventProfileAddToCart(productId: String,
                              productName: String,
                              price: String,
                              quantity: Int,
                              shopId: Int,
                              shopName: String) {

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
    fun eventFeedAddToCart(productId: String,
                           productName: String,
                           price: String,
                           quantity: Int,
                           shopId: Int,
                           shopName: String,
                           author: String) {

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
    fun eventContentDetailAddToCart(productId: String,
                                    productName: String,
                                    price: String,
                                    quantity: Int,
                                    shopId: Int,
                                    shopName: String,
                                    author: String) {

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
            shopName: String
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
            USER_ID, userSessionInterface.userId
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

    private fun getAddData(data: Any): Map<String, Any> = DataLayer.mapOf(Event.ADD, data)

    private fun getActionFieldData(data: Any): Map<String, Any> = DataLayer.mapOf(ACTION_FIELD, data)

    private fun getListData(data: Any): Map<String, Any> = DataLayer.mapOf(LIST, data)

    private fun getCurrencyData(): Map<String, Any> = DataLayer.mapOf(Product.CURRENCY_CODE, Product.CURRENCY_CODE_IDR)

    private fun getMediaPreviewAddData(
            role: String,
            productDataList: List<Any>
    ): Map<String, Any> = DataLayer.mapOf(
            Event.ACTION_FIELD, getMediaPreviewActionFieldData(role),
            PRODUCTS, productDataList
    )

    private fun getMediaPreviewActionFieldData(role: String
    ): Map<String, Any> = DataLayer.mapOf(LIST, Product.MEDIA_PREVIEW.replace(Product.MEDIA_PREVIEW_TAG, role))

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

    fun getEcommerceView(listProduct: List<ProductItem>): Map<String, Any> {
        return DataLayer.mapOf(CURRENCY_CODE, CURRENCY_CODE_IDR,
                Product.IMPRESSIONS, getProducts(listProduct))
    }

    fun getEcommerceClick(listProduct: List<ProductItem>, listSource: String): Map<String, Any> {
        return DataLayer.mapOf(Product.CLICK, getEcommerceClickValue(listProduct, listSource))
    }

    fun getEcommerceClickValue(listProduct: List<ProductItem>, listSource: String): Map<String, Any> {
        return DataLayer.mapOf(
                Product.ACTION_FIELD, getEcommerceActionFieldValue(listSource),
                PRODUCTS, getProducts(listProduct))
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

    fun createProductMap(product: ProductItem) : Map<String, Any> {
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

    private fun getProductItemList(id: Int, name: String, price: Int, brand: String, category: String,
                                variant: String, list: String, position: Int)
            : List<ProductItem> {
        val dataList = ArrayList<ProductItem>()
        dataList.add(ProductItem(
                id, name, price, brand, category, variant, list, position
        ))
        return dataList
    }

    class ProductItem(id: Int, name: String, price: Int, brand: String, category: String,
                  variant: String, list: String, position: Int) {
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