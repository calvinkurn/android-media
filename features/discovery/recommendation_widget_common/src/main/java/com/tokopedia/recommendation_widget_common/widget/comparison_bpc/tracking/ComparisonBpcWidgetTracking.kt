package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DEFAULT_VALUE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_40
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.IMPRESSIONS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_BRAND
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_CATEGORY
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_NAME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_VARIANT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.KEY_INDEX
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.PRICE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.VALUE_NONE_OTHER
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlin.collections.HashMap

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
    private const val WIDGET_TYPE_BPC = "comparison bpc"

    fun putImpressionToQueue(
        trackingQueue: TrackingQueue,
        androidPageName: String = "",
        userId: String = "",
        recommendationItem: RecommendationItem,
        anchorProductId: String,
        widgetTitle: String
    ) {
        val isLogin = userId.isNotBlank()
        val data = DataLayer.mapOf(
            Event.KEY, Event.PRODUCT_VIEW,
            Category.KEY, androidPageName,
            Action.KEY, EVENT_ACTION_IMPRESSION.plus(if (isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN),
            Label.KEY, "$anchorProductId - $widgetTitle",
            UserId.KEY, userId,
            ItemList.KEY,
            EVENT_LIST.format(
                recommendationItem.pageName,
                if (isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN,
                recommendationItem.recommendationType,
                DEFAULT_VALUE,
                WIDGET_TYPE_BPC,
                anchorProductId
            ),
            Ecommerce.KEY,
            DataLayer.mapOf(
                IMPRESSIONS,
                DataLayer.listOf(
                    mapRecommendationItemToDataImpressionObject(
                        recommendationItem,
                        EVENT_LIST.format(
                            recommendationItem.pageName,
                            if (isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN,
                            recommendationItem.recommendationType,
                            if (recommendationItem.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
                            WIDGET_TYPE_BPC,
                            anchorProductId
                        )
                    )
                )
            ),
            BusinessUnit.KEY, BUSINESS_UNIT_HOME,
            CurrentSite.KEY, CURRENT_SITE_MP,
            TrackerId.KEY, if (isLogin) TRACKER_ID_IMPRESSION_LOGIN else TRACKER_ID_IMPRESSION_NON_LOGIN
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    private fun mapRecommendationItemToDataImpressionObject(item: RecommendationItem, list: String): Map<String, Any> {
        return DataLayer.mapOf(
            DIMENSION_40, list,
            ITEM_NAME, item.name,
            ITEM_ID, item.productId.toString(),
            ITEM_BRAND, VALUE_NONE_OTHER,
            PRICE, item.priceInt.toString(),
            ITEM_VARIANT, VALUE_NONE_OTHER,
            ITEM_CATEGORY, item.categoryBreadcrumbs,
            KEY_INDEX, (item.position + 1).toString()
        )
    }

    fun sendClick(
        androidPageName: String,
        userId: String,
        recommendationItem: RecommendationItem,
        anchorProductId: String
    ) {
        val isLogin = userId.isNotBlank()
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Category.KEY, androidPageName)
            putString(Action.KEY, EVENT_ACTION_CLICK.plus(if (isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN))
            putString(Label.KEY, anchorProductId)
            putString(UserId.KEY, userId)
            putString(
                ItemList.KEY,
                EVENT_LIST.format(
                    recommendationItem.pageName,
                    if (isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN,
                    recommendationItem.recommendationType,
                    DEFAULT_VALUE,
                    WIDGET_TYPE_BPC,
                    anchorProductId
                )
            )
            putParcelableArrayList(
                Items.KEY,
                arrayListOf(
                    mapRecommendationItemToDataClickBundle(
                        recommendationItem,
                        EVENT_LIST.format(
                            recommendationItem.pageName,
                            if (isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN,
                            recommendationItem.recommendationType,
                            if (recommendationItem.isTopAds) VALUE_IS_TOPADS else DEFAULT_VALUE,
                            WIDGET_TYPE_BPC,
                            anchorProductId
                        )
                    )
                )
            )
            putString(BusinessUnit.KEY, BUSINESS_UNIT_HOME)
            putString(CurrentSite.KEY, CURRENT_SITE_MP)
            putString(TrackerId.KEY, if (isLogin) TRACKER_ID_CLICK_LOGIN else TRACKER_ID_CLICK_NON_LOGIN)
        }
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    private fun mapRecommendationItemToDataClickBundle(item: RecommendationItem, list: String): Bundle {
        return Bundle().apply {
            putString(DIMENSION_40, list)
            putString(ITEM_NAME, item.name)
            putString(ITEM_ID, item.productId.toString())
            putString(ITEM_BRAND, VALUE_NONE_OTHER)
            putFloat(PRICE, item.priceInt.toFloat())
            putString(ITEM_VARIANT, VALUE_NONE_OTHER)
            putString(ITEM_CATEGORY, item.categoryBreadcrumbs)
            putInt(KEY_INDEX, item.position + 1)
        }
    }

    fun sendClickSeeAll(androidPageName: String, userId: String, productAnchorId: String) {
        val isLogin = userId.isNotBlank()
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = androidPageName,
            eventAction = EVENT_ACTION_CLICK_SEE_ALL.plus(if (isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN),
            eventLabel = productAnchorId
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, if (isLogin) TRACKER_ID_CLICK_SEE_ALL_LOGIN else TRACKER_ID_CLICK_SEE_ALL_NON_LOGIN)
        getTracker().sendGeneralEvent(trackerBuilder.build())
    }
}
