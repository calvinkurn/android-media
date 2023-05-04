package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlin.collections.HashMap
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DEFAULT_VALUE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_40
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.IMPRESSIONS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS_DIMENSION_84
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS_DIMENSION_90
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS_PRODUCT_BRAND
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS_PRODUCT_CATEGORY
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS_PRODUCT_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS_PRODUCT_NAME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS_PRODUCT_PRICE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS_PRODUCT_VARIANT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.KEY_INDEX
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.PRODUCT_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_NONE_OTHER
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst
import com.tokopedia.track.builder.BaseTrackerBuilder

/**
 * Created by Frenzel
 */
object ComparisonBpcWidgetTracking : BaseTrackerConst() {

    private const val VALUE_IS_TOPADS = "- product topads"
    private const val VALUE_NON_LOGIN = " - non login"
    private const val COMPARISON_WIDGET = "comparison widget"
    private const val EVENT_ACTION_IMPRESSION = "impression $COMPARISON_WIDGET"
    private const val EVENT_ACTION_CLICK = "click $COMPARISON_WIDGET"
    private const val EVENT_ACTION_CLICK_SEE_ALL = "click $COMPARISON_WIDGET - lihat produk lainnya"
    private const val EVENT_LIST = "/product - %s%s - rekomendasi untuk anda - %s%s - %s - %s"
    private const val TRACKER_ID_IMPRESSION_LOGIN = "43012"
    private const val TRACKER_ID_IMPRESSION_NON_LOGIN = "43015"
    private const val TRACKER_ID_CLICK_LOGIN = "43013"
    private const val TRACKER_ID_CLICK_NON_LOGIN = "43016"
    private const val TRACKER_ID_CLICK_SEE_ALL_LOGIN = "43014"
    private const val TRACKER_ID_CLICK_SEE_ALL_NON_LOGIN = "43017"

    fun putImpressionToQueue(
        trackingQueue: TrackingQueue,
        androidPageName: String = "",
        userId: String = "",
        recommendationItem: RecommendationItem,
        anchorProductId: String,
    ) {
        val isLogin = userId.isNotBlank()
        val data = DataLayer.mapOf(
            Event.KEY, Event.PRODUCT_VIEW,
            Category.KEY, androidPageName,
            Action.KEY, EVENT_ACTION_IMPRESSION.plus(if(isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN),
            Label.KEY, anchorProductId, //need confirmation
            UserId.KEY, userId,
            PRODUCT_ID, anchorProductId, //need confirmation
            ItemList.KEY, EVENT_LIST.format(
                recommendationItem.pageName,
                if(isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN,
                recommendationItem.recommendationType,
                DEFAULT_VALUE,
                RecommendationTypeConst.TYPE_COMPARISON_BPC_WIDGET,
                anchorProductId
            ),
            Ecommerce.KEY, DataLayer.mapOf(
                IMPRESSIONS, DataLayer.listOf(
                    mapRecommendationItemToDataImpressionObject(recommendationItem,
                    EVENT_LIST.format(
                        recommendationItem.pageName,
                        if(isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN,
                        recommendationItem.recommendationType,
                        if(recommendationItem.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
                        RecommendationTypeConst.TYPE_COMPARISON_BPC_WIDGET,
                        anchorProductId
                    )
                    )
                )
            ),
            BusinessUnit.KEY, BUSINESS_UNIT_HOME,
            CurrentSite.KEY, CURRENT_SITE_MP,
            TrackerId.KEY, if(isLogin) TRACKER_ID_IMPRESSION_LOGIN else TRACKER_ID_IMPRESSION_NON_LOGIN
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    private fun mapRecommendationItemToDataImpressionObject(item: RecommendationItem, list: String): Map<String, Any>  {
        return DataLayer.mapOf(
            DIMENSION_40, list,
            ITEMS_DIMENSION_84, DEFAULT_VALUE,
            ITEMS_DIMENSION_90, DEFAULT_VALUE, //need confirmation
            ITEMS_PRODUCT_NAME, item.name,
            ITEMS_PRODUCT_ID, item.productId.toString(),
            ITEMS_PRODUCT_BRAND, VALUE_NONE_OTHER,
            ITEMS_PRODUCT_PRICE, item.priceInt.toString(),
            ITEMS_PRODUCT_VARIANT, VALUE_NONE_OTHER,
            ITEMS_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
            KEY_INDEX, (item.position+1).toString()
        )
    }

    fun sendClick(
        androidPageName: String,
        userId: String,
        recommendationItem: RecommendationItem,
        anchorProductId: String,
    ) {
        val isLogin = userId.isNotBlank()
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Category.KEY, androidPageName)
            putString(Action.KEY, EVENT_ACTION_CLICK.plus(if(isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN))
            putString(Label.KEY, anchorProductId) //need confirmation
            putString(UserId.KEY, userId)
            putString(PRODUCT_ID, anchorProductId)
            putString(ItemList.KEY, EVENT_LIST.format(
                recommendationItem.pageName,
                if(isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN,
                recommendationItem.recommendationType,
                DEFAULT_VALUE,
                RecommendationTypeConst.TYPE_COMPARISON_BPC_WIDGET,
                anchorProductId
            ))
            putParcelableArrayList(Items.KEY, arrayListOf(mapRecommendationItemToDataClickBundle(recommendationItem, EVENT_LIST.format(
                recommendationItem.pageName,
                if(isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN,
                recommendationItem.recommendationType,
                if(recommendationItem.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
                RecommendationTypeConst.TYPE_COMPARISON_BPC_WIDGET,
                anchorProductId
            ))))
            putString(BusinessUnit.KEY, BUSINESS_UNIT_HOME)
            putString(CurrentSite.KEY, CURRENT_SITE_MP)
            putString(TrackerId.KEY, if(isLogin) TRACKER_ID_CLICK_LOGIN else TRACKER_ID_CLICK_NON_LOGIN)
        }
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    private fun mapRecommendationItemToDataClickBundle(item: RecommendationItem, list: String): Bundle  {
        return Bundle().apply {
            putString(DIMENSION_40, list)
            putString(ITEMS_DIMENSION_84, DEFAULT_VALUE)
            putString(ITEMS_DIMENSION_90, DEFAULT_VALUE) //need confirmation
            putString(ITEMS_PRODUCT_NAME, item.name)
            putString(ITEMS_PRODUCT_ID, item.productId.toString())
            putString(ITEMS_PRODUCT_BRAND, VALUE_NONE_OTHER)
            putFloat(ITEMS_PRODUCT_PRICE, item.priceInt.toFloat())
            putString(ITEMS_PRODUCT_VARIANT, VALUE_NONE_OTHER)
            putString(ITEMS_PRODUCT_CATEGORY, item.categoryBreadcrumbs)
            putInt(KEY_INDEX, item.position+1)
        }
    }

    fun sendClickSeeAll(androidPageName: String, userId: String) {
        val isLogin = userId.isNotBlank()
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_CLICK_SEE_ALL.plus(if(isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN),
            eventLabel = Label.NONE
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, if(isLogin) TRACKER_ID_CLICK_SEE_ALL_LOGIN else TRACKER_ID_CLICK_SEE_ALL_NON_LOGIN)
        getTracker().sendGeneralEvent(trackerBuilder.build())
    }
}
