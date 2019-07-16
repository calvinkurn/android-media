package com.tokopedia.feedcomponent.analytics.posttag

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by yfsx on 04/04/19.
 */
class PostTagAnalytics @Inject constructor(private val userSessionInterface: UserSessionInterface) {
    companion object {
        internal const val PRODUCT_VIEW = "productView"
        internal const val PRODUCT_CLICK = "productClick"

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
        private const val FORMAT_EVENT_ACTION = "%s - %s - %s - %s";

        private const val EVENT_NAME = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
        private const val EVENT_ECOMMERCE = "ecommerce"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_ID_MOD = "userIdmodulo"

        private const val REGEX_NUMERIC = "[^\\d]"
    }

    object Screen {
        const val FEED = "/feed"
        const val PROFILE = "/user-profile-socialcommerce"
        const val MY_PROFILE = "/my-profile-socialcommerce"
        const val PROFILE_DETAIL = "/user-profile-socialcommerce-content-detail"
        const val MY_PROFILE_DETAIL = "/my-profile-socialcommerce-content-detail"
    }

    object Event {
        const val EVENT_CLICK_TOP_PROFILE = "clickTopProfile"
        const val EVENT_CLICK_SOCIAL_COMMERCE = "clickSocialCommerce"
        const val EVENT_VIEW_PROFILE = "viewProfile"

    }

    object Category {
        const val CONTENT_FEED_TIMELINE = "content feed timeline"
        const val USER_PROFILE_SOCIALCOMMERCE = "user profile socialcommerce"
        const val MY_PROFILE_SOCIALCOMMERCE = "my profile socialcommerce"
        const val USER_PROFILE_SOCIALCOMMERCE_DETAIL = "user profile socialcommerce - content detail"
        const val MY_PROFILE_SOCIALCOMMERCE_DETAIL = "my profile socialcommerce - content detail"
    }

    object Action {
        const val CLICK = "click"
        const val IMPRESSION = "impression"
        const val IMPRESSION_PRODUCT = "impression product"
        const val PRODUCT = "product"
    }

    object ListSource {
        const val FEED = "/feed - produk di post - %s"
        const val MY_PROFILE_PAGE = "/my profile page - produk di post"
        const val USER_PROFILE_PAGE = "/user profile page - produk di post"
        const val MY_PROFILE_PAGE_DETAIL = "/my profile page detail - produk di post"
        const val USER_PROFILE_PAGE_DETAIL = "/user profile page detail - produk di post"
    }

    private fun getBasicViewPostTagEvent(screenName: String,
                                         category: String,
                                         postId: Int,
                                         postTag: PostTagItem,
                                         postTagPosition: Int,
                                         trackingModel: TrackingPostModel,
                                         listSource: String) {
        val impressionString = if (listSource.equals(ListSource.FEED)) Action.IMPRESSION else Action.IMPRESSION_PRODUCT
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        screenName,
                        category,
                        String.format(FORMAT_EVENT_ACTION,
                                impressionString,
                                trackingModel.templateType,
                                trackingModel.activityName,
                                Action.PRODUCT
                        ),
                        postId.toString(),
                        getProductList(
                                postTag.id.toIntOrZero(),
                                postTag.text,
                                formatPriceToInt(postTag.price),
                                "",
                                "",
                                "",
                                listSource,
                                postTagPosition
                        ),
                        userSessionInterface.userId.toIntOrZero()
                )
        )
    }

    private fun getBasicClickPostTagEvent(screenName: String,
                                          category: String,
                                          postId: Int,
                                          postTag: PostTagItem,
                                          postTagPosition: Int,
                                          trackingModel: TrackingPostModel,
                                          listSource: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getEventEcommerceClick(
                        screenName,
                        category,
                        String.format(FORMAT_EVENT_ACTION,
                                Action.CLICK,
                                trackingModel.templateType,
                                trackingModel.activityName,
                                Action.PRODUCT
                        ),
                        postId.toString(),
                        getProductList(
                                postTag.id.toIntOrZero(),
                                postTag.text,
                                formatPriceToInt(postTag.price),
                                "",
                                "",
                                "",
                                listSource,
                                postTagPosition
                        ),
                        userSessionInterface.userId.toIntOrZero(),
                        listSource
                )
        )
    }

    private fun formatPriceToInt(price: String): Int {
        var result = 0
        try {
            var rex = Regex(REGEX_NUMERIC)
            result = rex.replace(price, "").toInt()
        }catch (e: Exception) {
        }
        return result
    }

    private fun getProductList(id: Int, name: String, price: Int, brand: String, category: String,
                               variant: String, list: String, position: Int)
            : List<PostTagEnhancedTracking.Product> {
        val dataList = ArrayList<PostTagEnhancedTracking.Product>()
        dataList.add(PostTagEnhancedTracking.Product(
                id, name, price, brand, category, variant, list, position
        ))
        return dataList
    }

    private fun getEventEcommerceView(screenName: String,
                                      category: String,
                                      action: String,
                                      label: String,
                                      products: List<PostTagEnhancedTracking.Product>,
                                      userId: Int): Map<String, Any> {
        return DataLayer.mapOf(
                SCREEN_NAME, screenName,
                EVENT_NAME, PRODUCT_VIEW,
                EVENT_CATEGORY, category,
                EVENT_ACTION, action,
                EVENT_LABEL, label,
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % 50,
                EVENT_ECOMMERCE, PostTagEnhancedTracking.Ecommerce.getEcommerceView(products)
        )
    }

    private fun getEventEcommerceClick(screenName: String,
                                       category: String,
                                       action: String,
                                       label: String,
                                       products: List<PostTagEnhancedTracking.Product>,
                                       userId: Int,
                                       listSource: String): Map<String, Any> {
        return DataLayer.mapOf(
                SCREEN_NAME, screenName,
                EVENT_NAME, PRODUCT_CLICK,
                EVENT_CATEGORY, category,
                EVENT_ACTION, action,
                EVENT_LABEL, label,
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % 50,
                EVENT_ECOMMERCE, PostTagEnhancedTracking.Ecommerce.getEcommerceClick(products, listSource)
        )
    }


    fun trackViewPostTagFeed(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            author: String,
            trackingModel: TrackingPostModel) {
        getBasicViewPostTagEvent(
                Screen.FEED,
                Category.CONTENT_FEED_TIMELINE,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                String.format(ListSource.FEED, author))
    }

    fun trackClickPostTagFeed(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            author: String,
            trackingModel: TrackingPostModel) {
        getBasicClickPostTagEvent(
                Screen.FEED,
                Category.CONTENT_FEED_TIMELINE,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                String.format(ListSource.FEED, author))
    }

    fun trackViewPostTagProfileSelf(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            trackingModel: TrackingPostModel) {
        getBasicViewPostTagEvent(
                Screen.MY_PROFILE,
                Category.MY_PROFILE_SOCIALCOMMERCE,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                ListSource.MY_PROFILE_PAGE)
    }

    fun trackClickPostTagProfileSelf(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            trackingModel: TrackingPostModel) {
        getBasicClickPostTagEvent(
                Screen.MY_PROFILE,
                Category.MY_PROFILE_SOCIALCOMMERCE,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                ListSource.MY_PROFILE_PAGE)
    }

    fun trackViewPostTagProfileOther(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            trackingModel: TrackingPostModel) {
        getBasicViewPostTagEvent(
                Screen.PROFILE,
                Category.USER_PROFILE_SOCIALCOMMERCE,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                ListSource.USER_PROFILE_PAGE)
    }

    fun trackClickPostTagProfileOther(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            trackingModel: TrackingPostModel) {
        getBasicClickPostTagEvent(
                Screen.PROFILE,
                Category.USER_PROFILE_SOCIALCOMMERCE,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                ListSource.USER_PROFILE_PAGE)
    }

    fun trackViewPostTagProfileDetailSelf(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            trackingModel: TrackingPostModel) {
        getBasicViewPostTagEvent(
                Screen.MY_PROFILE_DETAIL,
                Category.MY_PROFILE_SOCIALCOMMERCE_DETAIL,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                ListSource.MY_PROFILE_PAGE_DETAIL)
    }

    fun trackClickPostTagProfileDetailSelf(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            trackingModel: TrackingPostModel) {
        getBasicClickPostTagEvent(
                Screen.MY_PROFILE_DETAIL,
                Category.MY_PROFILE_SOCIALCOMMERCE_DETAIL,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                ListSource.MY_PROFILE_PAGE_DETAIL)
    }

    fun trackViewPostTagProfileDetailOther(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            trackingModel: TrackingPostModel) {
        getBasicViewPostTagEvent(
                Screen.PROFILE_DETAIL,
                Category.USER_PROFILE_SOCIALCOMMERCE_DETAIL,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                ListSource.USER_PROFILE_PAGE_DETAIL)
    }

    fun trackClickPostTagProfileDetailOther(
            postId: Int,
            postTag: PostTagItem,
            postTagPosition: Int,
            trackingModel: TrackingPostModel) {
        getBasicClickPostTagEvent(
                Screen.PROFILE_DETAIL,
                Category.USER_PROFILE_SOCIALCOMMERCE_DETAIL,
                postId,
                postTag,
                postTagPosition,
                trackingModel,
                ListSource.USER_PROFILE_PAGE_DETAIL)
    }

}
