package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.ADD_TO_CART
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.PRODUCT_VIEW
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.SELECT_CONTENT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CATEGORY_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CATEGORY_PDP
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENCY_CODE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_40
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_45
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.IDR
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.IMPRESSIONS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEMS
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_BRAND
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_CATEGORY
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_LIST
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_NAME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.ITEM_VARIANT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.KEY_INDEX
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.PRICE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.PRODUCT_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.QUANTITY
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.SHOP_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.SHOP_NAME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.SHOP_TYPE
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.TRACKER_ID
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant.USERID
import com.tokopedia.trackingoptimizer.TrackingQueue

object ViewToViewBottomSheetTracker : BaseTrackerConst() {
    private const val TOPADS = "topads"
    private const val NONTOPADS = "nontopads"
    private const val ITEM_LIST_TEMPLATE = "/product - v2v widget - rekomendasi untuk anda - %s - product %s"

    private const val IMPRESSION_ACTION = "impression on product bottom sheet v2v widget"
    private const val IMPRESSION_TRACKER_ID = "40440"

    private const val CLICK_ACTION = "click on product bottom sheet v2v widget"
    private const val CLICK_TRACKER_ID = "40442"

    private const val ATC_ACTION = "click keranjang on product bottom sheet v2v widget"
    private const val ATC_TRACKER_ID = "40443"

    fun eventImpressProduct(
        product: RecommendationItem,
        headerTitle: String,
        position: Int,
        userId: String,
        anchorProductId: String,
        trackingQueue: TrackingQueue?
    ) {
        val productList = arrayListOf(product.asDataLayer(position + 1))

        val dataLayer = DataLayer.mapOf(
            Event.KEY, PRODUCT_VIEW,
            Category.KEY, CATEGORY_PDP,
            Action.KEY, IMPRESSION_ACTION,
            Label.KEY, headerTitle,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TRACKER_ID, IMPRESSION_TRACKER_ID,
            ITEM_LIST, product.asItemList(),
            Ecommerce.KEY,
            DataLayer.mapOf(
                CURRENCY_CODE,
                IDR,
                IMPRESSIONS,
                productList
            ),
            PRODUCT_ID, anchorProductId,
            USERID, userId
        ) as HashMap<String, Any>
        trackingQueue?.putEETracking(dataLayer)
    }

    fun eventProductClick(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        position: Int,
        anchorProductId: String
    ) {
        val itemBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_ACTION)
            putString(EVENT_CATEGORY, CATEGORY_PDP)
            putString(EVENT_LABEL, headerTitle)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
            putString(CURRENT_SITE, CURRENT_SITE_MP)
            putString(TRACKER_ID, CLICK_TRACKER_ID)
            putString(ITEM_LIST, product.asItemList())

            val bundlePromotion = product.asBundle(position + 1)
            val list = arrayListOf(bundlePromotion)
            putParcelableArrayList(ITEMS, list)

            putString(PRODUCT_ID, anchorProductId)
            putString(USERID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, itemBundle)
    }

    fun eventAddToCart(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        anchorProductId: String
    ) {
        val itemBundle = Bundle().apply {
            putString(EVENT, ADD_TO_CART)
            putString(EVENT_ACTION, ATC_ACTION)
            putString(EVENT_CATEGORY, CATEGORY_PDP)
            putString(EVENT_LABEL, headerTitle)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
            putString(CURRENT_SITE, CURRENT_SITE_MP)
            putString(TRACKER_ID, ATC_TRACKER_ID)

            val bundlePromotion = product.asBundle(product.position + 1, isAtc = true)
            val list = arrayListOf(bundlePromotion)
            putParcelableArrayList(ITEMS, list)

            putString(PRODUCT_ID, anchorProductId)
            putString(USERID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ADD_TO_CART, itemBundle)
    }

    private fun RecommendationItem.asItemList(): String {
        val isTopAds = if (isTopAds) TOPADS else NONTOPADS
        return ITEM_LIST_TEMPLATE.format(recommendationType, isTopAds)
    }

    private fun RecommendationItem.asBundle(position: Int, isAtc: Boolean = false): Bundle {
        return Bundle().apply {
            putString(DIMENSION_40, asItemList())
            if (isAtc) {
                putString(CATEGORY_ID, departmentId.toString())
                putString(DIMENSION_45, cartId)
                putString(QUANTITY, quantity.toString())
                putString(SHOP_ID, shopId.toString())
                putString(SHOP_NAME, shopName)
                putString(SHOP_TYPE, shopType)
            } else {
                putString(KEY_INDEX, position.toString())
            }
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, categoryBreadcrumbs)
            putString(ITEM_ID, productId.toString())
            putString(ITEM_NAME, name)
            putString(ITEM_VARIANT, "")
            putString(PRICE, convertRupiahToInt(price).toFloat().toString())
        }
    }

    private fun RecommendationItem.asDataLayer(position: Int): Map<String, Any> {
        return hashMapOf(
            DIMENSION_40 to asItemList(),
            ITEM_BRAND to "",
            ITEM_CATEGORY to categoryBreadcrumbs,
            ITEM_ID to productId.toString(),
            ITEM_NAME to name,
            ITEM_VARIANT to "",
            PRICE to convertRupiahToInt(price).toFloat().toString(),
            KEY_INDEX to position.toString()
        )
    }
}
