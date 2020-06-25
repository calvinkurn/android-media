package com.tokopedia.thankyou_native.recommendationdigital.analytics

import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.thankyou_native.recommendationdigital.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.recommendationdigital.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DigitalRecommendationAnalytics @Inject constructor(
        @CoroutineMainDispatcher val mainDispatcher: dagger.Lazy<CoroutineDispatcher>,
        @CoroutineBackgroundDispatcher val backgroundDispatcher: dagger.Lazy<CoroutineDispatcher>) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm


    fun sendDigitalRecommendationItemDisplayed(trackingQueue: TrackingQueue, recommendationItem: RecommendationsItem,
                                               position: Int) {
        CoroutineScope(mainDispatcher.get()).launchCatchError(
                block = {
                    withContext(backgroundDispatcher.get()) {
                        val data: MutableMap<String, Any> = mutableMapOf(
                                KEY_EVENT to EVENT_PRODUCT_VIEW,
                                KEY_EVENT_CATEGORY to EVENT_CATEGORY_ORDER_COMPLETE,
                                KEY_EVENT_ACTION to EVENT_ACTION_PRODUCT_VIEW,
                                KEY_EVENT_LABEL to recommendationItem.type + " - " + recommendationItem.categoryName + " - " + (position + 1),
                                KEY_E_COMMERCE to getProductViewECommerceData(recommendationItem, position))
                        analyticTracker.sendEnhanceEcommerceEvent(data)

                        trackingQueue.putEETracking(data as HashMap<String, Any>)
                    }
                }, onError = {
            it.printStackTrace()
        }
        )
    }


    fun sendDigitalRecommendationItemClick(recommendationItem: RecommendationsItem,
                                           position: Int) {

        CoroutineScope(mainDispatcher.get()).launchCatchError(
                block = {
                    withContext(backgroundDispatcher.get()) {
                        val data: MutableMap<String, Any> = mutableMapOf(
                                KEY_EVENT to EVENT_PRODUCT_CLICK,
                                KEY_EVENT_CATEGORY to EVENT_CATEGORY_ORDER_COMPLETE,
                                KEY_EVENT_ACTION to EVENT_ACTION_CLICK_PRODUCT,
                                KEY_EVENT_LABEL to (position + 1),
                                KEY_E_COMMERCE to getProductClickECommerceData(recommendationItem, position))
                        analyticTracker.sendEnhanceEcommerceEvent(data)
                    }
                }, onError = {
            it.printStackTrace()
        }
        )
    }

    private fun getProductViewECommerceData(recommendationItem: RecommendationsItem,
                                            position: Int): Any {
        return mutableMapOf(
                KEY_CURRENCY_CODE to IDR_CURRENCY,
                KEY_IMPRESSION to mutableListOf(getProductDataMap(recommendationItem, position))
        )
    }

    private fun getProductClickECommerceData(recommendationItem: RecommendationsItem,
                                             position: Int): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_CLICK to mutableMapOf(
                        KEY_LIST to EVENT_LIST_RECOMMENDATION_ORDER_COMPLETE,
                        KEY_PRODUCTS to mutableListOf(getProductDataMap(recommendationItem, position))
                )
        )
    }

    private fun getProductDataMap(recommendationItem: RecommendationsItem,
                                  position: Int): MutableMap<String, Any?> {
        return mutableMapOf(
                KEY_PRODUCT_NAME to recommendationItem.categoryName,
                KEY_PRODUCT_ID to recommendationItem.productId,
                KEY_PRODUCT_PRICE to recommendationItem.productPrice.toString(),
                KEY_PRODUCT_BRAND to "",
                KEY_PRODUCT_CATEGORY to recommendationItem.categoryName,
                KEY_PRODUCT_VARIANT to "",
                KEY_LIST to EVENT_LIST_RECOMMENDATION_ORDER_COMPLETE,
                KEY_PRODUCT_POSITION to (position + 1)
        )
    }

    companion object {

        const val KEY_EVENT = "event"
        const val KEY_EVENT_CATEGORY = "eventCategory"
        const val KEY_EVENT_ACTION = "eventAction"
        const val KEY_EVENT_LABEL = "eventLabel"
        const val KEY_E_COMMERCE = "ecommerce"

        const val KEY_CLICK = "click"
        const val KEY_LIST = "list"
        const val KEY_PRODUCTS = "products"

        const val KEY_CURRENCY_CODE = "currencyCode"
        const val KEY_IMPRESSION = "impressions"
        const val IDR_CURRENCY = "IDR"


        const val EVENT_PRODUCT_CLICK = "productClick"
        const val EVENT_CATEGORY_ORDER_COMPLETE = "order complete"
        const val EVENT_ACTION_CLICK_PRODUCT = "click on widget recommendation"
        const val EVENT_LIST_RECOMMENDATION_ORDER_COMPLETE = "/recommendation-order complete-DG"


        const val EVENT_PRODUCT_VIEW = "productView"
        const val EVENT_ACTION_PRODUCT_VIEW = "impression on widget recommendation"

        const val KEY_PRODUCT_NAME = "name"
        const val KEY_PRODUCT_ID = "id"
        const val KEY_PRODUCT_PRICE = "price"
        const val KEY_PRODUCT_BRAND = "brand"
        const val KEY_PRODUCT_CATEGORY = "category"
        const val KEY_PRODUCT_VARIANT = "variant"
        const val KEY_PRODUCT_POSITION = "position"
    }

}