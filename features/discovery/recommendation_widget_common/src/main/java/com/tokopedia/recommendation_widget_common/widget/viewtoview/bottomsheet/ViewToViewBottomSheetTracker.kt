package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.ADD_TO_CART
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.EVENT_ADD_TO_CART
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.PRODUCT_CLICK
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.PRODUCT_VIEW
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.SELECT_CONTENT
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Action.VIEW_ITEM_LIST
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.BUSINESS_UNIT_HOME
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CATEGORY_ID
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CATEGORY_PDP
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.CURRENT_SITE_MP
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_40
import com.tokopedia.recommendation_widget_common.RecommendationTrackingConstants.Tracking.DIMENSION_45
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
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant.USERID

object ViewToViewBottomSheetTracker {
    fun eventBottomSheetImpress(
        widget: RecommendationWidget,
        headerTitle: String,
        userId: String,
        anchorProductId: String,
    ) {

        val itemBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM_LIST)
            putString(EVENT_ACTION, "impression on product bottom sheet v2v widget")
            putString(EVENT_CATEGORY, CATEGORY_PDP)
            putString(EVENT_LABEL, headerTitle)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
            putString(CURRENT_SITE, CURRENT_SITE_MP)
            putString(TRACKER_ID, "40440")
            putString(ITEM_LIST, widget.asItemList())

            //promotion
            val bundlePromotions = widget.recommendationItemList.mapIndexed { index, product ->
                product.asBundle(index + 1)
            }
            val list = ArrayList(bundlePromotions)
            putParcelableArrayList(ITEMS, list)

            putString(PRODUCT_ID, anchorProductId)
            putString(USERID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PRODUCT_VIEW, itemBundle)
    }

    private fun RecommendationWidget.asItemList(): String {
        val firstRecommendationItem = recommendationItemList.firstOrNull()
        val isTopAds = if (firstRecommendationItem?.isTopAds == true) "topads" else "nontopads"
        val recommendationType = firstRecommendationItem?.recommendationType ?: ""
        return "/product - v2v widget - rekomendasi untuk anda - $recommendationType - product $isTopAds"
    }

    fun eventProductClick(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        position: Int,
        anchorProductId: String,
    ) {
        val itemBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, "click on product bottom sheet v2v widget")
            putString(EVENT_CATEGORY, CATEGORY_PDP)
            putString(EVENT_LABEL, headerTitle)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
            putString(CURRENT_SITE, CURRENT_SITE_MP)
            putString(TRACKER_ID, "40442")
            putString(ITEM_LIST, product.asItemList())

            //promotion
            val bundlePromotion = product.asBundle(position + 1)
            val list = arrayListOf(bundlePromotion)
            putParcelableArrayList(ITEMS, list)

            putString(PRODUCT_ID, anchorProductId)
            putString(USERID, userId)

        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PRODUCT_CLICK, itemBundle)
    }

    fun eventAddToCart(
        product: RecommendationItem,
        headerTitle: String,
        userId: String,
        anchorProductId: String,
    ) {
        val itemBundle = Bundle().apply {
            putString(EVENT, ADD_TO_CART)
            putString(EVENT_ACTION, "click keranjang on product bottom sheet v2v widget")
            putString(EVENT_CATEGORY, CATEGORY_PDP)
            putString(EVENT_LABEL, headerTitle)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_HOME)
            putString(CURRENT_SITE, CURRENT_SITE_MP)
            putString(TRACKER_ID, "40443")
            putString(ITEM_LIST, product.asItemList())

            //promotion
            val bundlePromotion = product.asBundle(product.position + 1)
            val list = arrayListOf(bundlePromotion)
            putParcelableArrayList(ITEMS, list)

            putString(PRODUCT_ID, anchorProductId)
            putString(USERID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, itemBundle)
    }

    private fun RecommendationItem.asItemList(): String {
        val isTopAds = if (isTopAds) "topads" else "nontopads"
        return "/product - v2v widget - rekomendasi untuk anda - $recommendationType - product $isTopAds"
    }

    private fun RecommendationItem.asBundle(position: Int): Bundle {
        return Bundle().apply {
            putInt(CATEGORY_ID, departmentId)
            putString(DIMENSION_40, asItemList())
            putString(DIMENSION_45, cartId)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, categoryBreadcrumbs)
            putLong(ITEM_ID, productId)
            putString(ITEM_NAME, name)
            putString(ITEM_VARIANT, "")
            putString(PRICE, "%.1f".format(priceInt.toDouble()))
            putInt(QUANTITY, quantity)
            putInt(SHOP_ID, shopId)
            putString(SHOP_NAME, shopName)
            putString(SHOP_TYPE, shopType)
            putInt(KEY_INDEX, position)
        }
    }
}
