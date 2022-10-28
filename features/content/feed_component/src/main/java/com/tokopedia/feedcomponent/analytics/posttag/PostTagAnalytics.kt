package com.tokopedia.feedcomponent.analytics.posttag

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
        private const val FORMAT_EVENT_ACTION = "%s - %s - %s - %s";
        private const val FORMAT_2_VALUE = "%s - %s";
        private const val FORMAT_3_VALUE = "%s - %s - %s";
        private const val USER_ID_MOD_50 = 50

        private const val EVENT_NAME = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
        private const val EVENT_ECOMMERCE = "ecommerce"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_ID_MOD = "userIdmodulo"
    }

    object Screen {
        const val FEED = "/feed"
        const val FEED_SHOP = "/shop-feed"
    }

    object Category {
        const val CONTENT_FEED_TIMELINE = "content feed timeline"
        const val CONTENT_FEED_SHOP_TIMELINE = "content feed - shop page"
    }

    object Action {
        const val CLICK = "click"
        const val IMPRESSION = "impression"
        const val IMPRESSION_PRODUCT = "impression product"
        const val PRODUCT = "product"
    }

    object ListSource {
        const val FEED = "/feed - produk di post - %s"
        const val FEED_SHOP = "/feed shop page - produk di post"
    }

    private fun getBasicViewPostTagEvent(screenName: String,
                                         category: String,
                                         label: String,
                                         postItemList: List<PostTagItem>,
                                         trackingModel: TrackingPostModel,
                                         listSource: String,
                                         action: String = String.format(FORMAT_EVENT_ACTION,
                                                 if (listSource.equals(ListSource.FEED)) Action.IMPRESSION else Action.IMPRESSION_PRODUCT,
                                                 trackingModel.templateType,
                                                 trackingModel.activityName,
                                                 Action.PRODUCT
                                         )) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        screenName,
                        category,
                        action,
                        label,
                        postItemList.mapIndexed { pos, item ->
                            getProductList(
                                    item.id,
                                    item.text,
                                    item.price,
                                    "",
                                    "",
                                    "",
                                    listSource,
                                    pos
                            )
                        }.flatten(),
                        userSessionInterface.userId.toLongOrZero()
                )
        )
    }


    private fun getBasicViewFeedXProductEvent(screenName: String,
                                         category: String,
                                         label: String,
                                         postItemList: List<FeedXProduct>,
                                         trackingModel: TrackingPostModel,
                                         listSource: String,
                                         action: String = String.format(FORMAT_EVENT_ACTION,
                                                 if (listSource.equals(ListSource.FEED)) Action.IMPRESSION else Action.IMPRESSION_PRODUCT,
                                                 trackingModel.templateType,
                                                 trackingModel.activityName,
                                                 Action.PRODUCT
                                         )) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        screenName,
                        category,
                        action,
                        label,
                        postItemList.mapIndexed { pos, item ->
                            getProductList(
                                    item.id,
                                    item.name,
                                    item.price.toString(),
                                    "",
                                    "",
                                    "",
                                    listSource,
                                    pos
                            )
                        }.flatten(),
                        userSessionInterface.userId.toLongOrZero()
                )
        )
    }
    private fun getProductList(id: String, name: String, price: String, brand: String, category: String,
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
                                      userId: Long): Map<String, Any> {
        return DataLayer.mapOf(
                SCREEN_NAME, screenName,
                EVENT_NAME, PRODUCT_VIEW,
                EVENT_CATEGORY, category,
                EVENT_ACTION, action,
                EVENT_LABEL, label,
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % USER_ID_MOD_50,
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
                KEY_USER_ID_MOD, userId % USER_ID_MOD_50,
                EVENT_ECOMMERCE, PostTagEnhancedTracking.Ecommerce.getEcommerceClick(products, listSource)
        )
    }

    //row 7
    //docs - https://docs.google.com/spreadsheets/d/1pnZfjiNKbAk8LR37DhNGSwm2jvM3wKqNJc2lfWLejXA/edit#gid=1781959013
    fun trackViewPostTagFeed(
            postId: String,
            postTagItemList: List<FeedXProduct>,
            author: Int,
            trackingModel: TrackingPostModel) {
        getBasicViewFeedXProductEvent(
                Screen.FEED,
                Category.CONTENT_FEED_TIMELINE,
                String.format(FORMAT_2_VALUE, postId, trackingModel.recomId),
                postTagItemList,
                trackingModel,
                String.format(ListSource.FEED, author),
                action = String.format(FORMAT_3_VALUE, Action.IMPRESSION_PRODUCT, trackingModel.activityName, trackingModel.mediaType))
    }

    // row 28
    // docs - https://docs.google.com/spreadsheets/d/1pnZfjiNKbAk8LR37DhNGSwm2jvM3wKqNJc2lfWLejXA/edit#gid=1781959013
    fun trackViewPostTagFeedShop(
            postId: String,
            postTagItemList: List<PostTagItem>,
            author: String,
            trackingModel: TrackingPostModel) {
        getBasicViewPostTagEvent(
                Screen.FEED_SHOP,
                Category.CONTENT_FEED_SHOP_TIMELINE,
                postId.toString(),
                postTagItemList,
                trackingModel,
                String.format(ListSource.FEED_SHOP, author),
                action = String.format(FORMAT_3_VALUE, Action.IMPRESSION_PRODUCT, trackingModel.activityName, trackingModel.mediaType))
    }
}
