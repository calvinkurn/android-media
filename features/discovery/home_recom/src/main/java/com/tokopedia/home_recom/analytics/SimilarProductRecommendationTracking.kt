package com.tokopedia.home_recom.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by Lukas on 02/09/19
 */
object SimilarProductRecommendationTracking {
    private const val EVENT = "event"
    private const val EVENT_CATEGORY = "eventCategory"
    private const val EVENT_ACTION = "eventAction"
    private const val EVENT_LABEL = "eventLabel"

    private const val EVENT_PRODUCT_VIEW = "productView"
    private const val EVENT_PRODUCT_CLICK = "productClick"
    private const val EVENT_CLICK_RECOMMENDATION = "clickRecommendation"
    private const val EVENT_WISHLIST_RECOMMENDATION = "%s - wishlist on product recommendation"
    private const val EVENT_WISHLIST_RECOMMENDATION_NON_LOGIN = "add - wishlist on product recommendation - non login"
    private const val EVENT_CLICK_BACK_BUTTON = "click - back button"

    private const val EVENT_CATEGORY_SIMILAR_PRODUCT = "similar product recommendation page"
    private const val ACTION_IMPRESSION_PRODUCT_RECOMMENDATION = "impression - product recommendation"
    private const val ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN = "impression - product recommendation - non login"
    private const val ACTION_CLICK_PRODUCT_RECOMMENDATION = "click - product recommendation"
    private const val ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN = "click - product recommendation - non login"
    private const val EVENT_ACTION_CLICK_ANNOTATION_CHIP = "click - quick filter"
    private const val EVENT_ACTION_CLICK_FULL_FILTER_CHIP = "click - full filter chip on this page"
    private const val EVENT_ACTION_CLICK_SHOW_PRODUCT = "click - `tampilkan product` button after choose multiple filter / sort"

    private const val IMPRESSION = "impressions"
    private const val CLICK = "click"
    private const val ECOMMERCE = "ecommerce"
    private const val CURRENCY_CODE = "currencyCode"

    private const val FIELD_PRODUCTS = "products"
    private const val FIELD_PRODUCT_NAME = "name"
    private const val FIELD_PRODUCT_ID = "id"
    private const val FIELD_PRODUCT_PRICE = "price"
    private const val FIELD_PRODUCT_BRAND = "brand"
    private const val FIELD_PRODUCT_VARIANT = "variant"
    private const val FIELD_PRODUCT_CATEGORY = "category"
    private const val FIELD_PRODUCT_LIST = "list"
    private const val FIELD_PRODUCT_POSITION = "position"
    private const val FIELD_ACTION_FIELD = "actionField"
    private const val FIELD_ATTRIBUTE = "attribution"
    private const val FIELD_DIMENSION_61 = "dimension61"
    private const val FIELD_DIMENSION_83 = "dimension83"
    private const val FIELD_DIMENSION_90 = "dimension90"

    private const val LIST_PRODUCT_RECOMMENDATION = "/similarrecommendation - rekomendasi untuk anda - %s - ref: %s%s"
    private const val LIST_PRODUCT_RECOMMENDATION_NON_LOGIN = "/similarrecommendation - non login - rekomendasi untuk anda - %s - ref: %s%s"

    private const val PRODUCT_TOP_ADS = " - product topads"

    private const val VALUE_NONE_OTHER = "none / other"
    private const val VALUE_IDR = "IDR"
    private const val VALUE_EMPTY = ""
    private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
    private const val VALUE_BEBAS_ONGKIR_EXTRA = "bebas ongkir extra"

    private const val BUSINESS_UNIT = "businessUnit"
    private const val BU_HOME_AND_BROWSE = "Home & Browse"
    private const val CURRENT_SITE = "currentSite"
    private const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val USER_ID = "userId"

    private fun getTracker(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }

    private fun convertRecommendationItemToDataClickObject(item: RecommendationItem,
                                                           list: String,
                                                           position: String,
                                                           internalRef: String): Any {
        return DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                FIELD_PRODUCT_LIST, list
        ),
                FIELD_PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        FIELD_PRODUCT_NAME, item.name,
                        FIELD_PRODUCT_ID, item.productId,
                        FIELD_PRODUCT_PRICE, item.priceInt.toString(),
                        FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                        FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                        FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                        FIELD_PRODUCT_LIST, list,
                        FIELD_PRODUCT_POSITION, position,
                        FIELD_ATTRIBUTE, VALUE_EMPTY,
                        FIELD_DIMENSION_61, item.dimension61,
                        FIELD_DIMENSION_83, getBebasOngkirValue(item),
                        FIELD_DIMENSION_90, internalRef
                )
        )
        )
    }

    private fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem,
                                                                list: String,
                                                                position: String,
                                                                internalRef: String): Any {
        return DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId,
                FIELD_PRODUCT_PRICE, item.priceInt.toString(),
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_LIST, list,
                FIELD_PRODUCT_POSITION, position,
                FIELD_DIMENSION_83, getBebasOngkirValue(item),
                FIELD_DIMENSION_90, internalRef,
                FIELD_DIMENSION_61, item.dimension61
        )
    }

    private fun getBebasOngkirValue(item: RecommendationItem): String{
        val hasFulfillment = item.labelGroupList.hasLabelGroupFulfillment()
        return if(item.isFreeOngkirActive && hasFulfillment) VALUE_BEBAS_ONGKIR_EXTRA
        else if(item.isFreeOngkirActive && !hasFulfillment) VALUE_BEBAS_ONGKIR
        else VALUE_NONE_OTHER
    }

    fun eventImpression(
            trackingQueue: TrackingQueue,
            recommendationItem: RecommendationItem,
            position: String,
            ref: String,
            internalRef: String
    ) {
        trackingQueue.putEETracking(
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_VIEW,
                        EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                        EVENT_ACTION, ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
                        EVENT_LABEL, VALUE_EMPTY,
                        ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, VALUE_IDR,
                        IMPRESSION, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(
                                recommendationItem,
                                String.format(
                                        LIST_PRODUCT_RECOMMENDATION,
                                        recommendationItem.recommendationType,
                                        ref,
                                        if(recommendationItem.isTopAds) PRODUCT_TOP_ADS else ""
                                ), position, internalRef)
                )
                )
                ) as HashMap<String, Any>?
        )
    }

    fun eventImpressionNonLogin(
            trackingQueue: TrackingQueue,
            recommendationItem: RecommendationItem,
            position: String,
            ref: String,
            internalRef: String
    ) {
        trackingQueue.putEETracking(
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_VIEW,
                        EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                        EVENT_ACTION, ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN,
                        EVENT_LABEL, VALUE_EMPTY,
                        ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, VALUE_IDR,
                        IMPRESSION, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(
                                recommendationItem,
                                String.format(
                                        LIST_PRODUCT_RECOMMENDATION_NON_LOGIN,
                                        recommendationItem.recommendationType,
                                        ref,
                                        if(recommendationItem.isTopAds) PRODUCT_TOP_ADS else ""
                                ), position, internalRef)
                )
                )
                ) as HashMap<String, Any>?
        )
    }

    fun eventClick(
            recommendationItem: RecommendationItem,
            position: String,
            ref: String,
            internalRef: String
    ) {
        val data =
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_CLICK,
                        EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                        EVENT_ACTION, ACTION_CLICK_PRODUCT_RECOMMENDATION,
                        EVENT_LABEL, VALUE_EMPTY,
                        ECOMMERCE, DataLayer.mapOf(
                        CLICK, convertRecommendationItemToDataClickObject(
                        recommendationItem,
                        String.format(
                                LIST_PRODUCT_RECOMMENDATION,
                                recommendationItem.recommendationType,
                                ref,
                                if (recommendationItem.isTopAds) PRODUCT_TOP_ADS else ""
                        ),
                        position,
                        internalRef
                )
                )
                )

        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventClickNonLogin(
            recommendationItem: RecommendationItem,
            position: String,
            ref: String,
            internalRef: String
    ) {
        val data =
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_CLICK,
                        EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                        EVENT_ACTION, ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN,
                        EVENT_LABEL, VALUE_EMPTY,
                        ECOMMERCE, DataLayer.mapOf(
                            CLICK, convertRecommendationItemToDataClickObject(
                                recommendationItem,
                                String.format(
                                        LIST_PRODUCT_RECOMMENDATION_NON_LOGIN,
                                        recommendationItem.recommendationType,
                                        ref,
                                        if(recommendationItem.isTopAds) PRODUCT_TOP_ADS else ""
                                ),
                                position,
                                internalRef
                            )
                        )
                )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventClickWishlist(isAddWishlist: Boolean){
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                EVENT_ACTION, String.format(EVENT_WISHLIST_RECOMMENDATION, if(isAddWishlist) "add" else "remove"),
                EVENT_LABEL, VALUE_EMPTY
        )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventClickWishlistNonLogin(){
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                EVENT_ACTION, EVENT_WISHLIST_RECOMMENDATION_NON_LOGIN,
                EVENT_LABEL, VALUE_EMPTY
        )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventClickBackButton() {
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                EVENT_ACTION, EVENT_CLICK_BACK_BUTTON,
                EVENT_LABEL, VALUE_EMPTY
        )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventUserClickQuickFilterChip(userId: String, parameter: String) {
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                EVENT_ACTION, EVENT_ACTION_CLICK_ANNOTATION_CHIP,
                EVENT_LABEL, parameter,
                BUSINESS_UNIT, BU_HOME_AND_BROWSE,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, userId
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventUserClickFullFilterChip(userId: String, param: String){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                EVENT_ACTION, EVENT_ACTION_CLICK_FULL_FILTER_CHIP,
                EVENT_LABEL, param,
                BUSINESS_UNIT, BU_HOME_AND_BROWSE,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, userId
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventUserClickShowProduct(userId: String, param: String){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                EVENT_ACTION, EVENT_ACTION_CLICK_SHOW_PRODUCT,
                EVENT_LABEL, param,
                BUSINESS_UNIT, BU_HOME_AND_BROWSE,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, userId
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }
}