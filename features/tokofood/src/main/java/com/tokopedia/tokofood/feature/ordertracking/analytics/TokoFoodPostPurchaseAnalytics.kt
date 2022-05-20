package com.tokopedia.tokofood.feature.ordertracking.analytics

import android.os.Bundle
import com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Post Purchase
 * https://mynakama.tokopedia.com/datatracker/requestdetail/3059
 */

class TokoFoodPostPurchaseAnalytics @Inject constructor(private val userSession: UserSessionInterface) {

    private val tracking: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    fun viewOrderDetailPage(shopId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.OPEN_SCREEN,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.IS_LOGGED_IN_STATUS to userSession.isLoggedIn.toString(),
            TokoFoodAnalyticsConstants.SCREEN_NAME to TokoFoodAnalyticsConstants.ORDER_DETAIL_PAGE,
            TokoFoodAnalyticsConstants.SHOP_ID to shopId,
            TokoFoodAnalyticsConstants.USER_ID to userSession.userId
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickCallDriverIcon(orderId: String, shopId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_CALL_ICON,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_ORDER_DETAIL_PAGE,
            TrackAppUtils.EVENT_LABEL to orderId,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.SHOP_ID to shopId,
            TokoFoodAnalyticsConstants.USER_ID to userSession.userId
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickCallDriverCtaInBottomSheet(orderId: String, shopId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_CALL_DRIVER,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_ORDER_DETAIL_PAGE,
            TrackAppUtils.EVENT_LABEL to orderId,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.SHOP_ID to shopId,
            TokoFoodAnalyticsConstants.USER_ID to userSession.userId
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickCallHelpInStickyButton(orderId: String, shopId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_PG,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_HELP,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_ORDER_DETAIL_PAGE,
            TrackAppUtils.EVENT_LABEL to orderId,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.SHOP_ID to shopId,
            TokoFoodAnalyticsConstants.USER_ID to userSession.userId
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickBuyAgainButton(orderId: String, shopId: String, foodItems: List<FoodItemUiModel>) {
        val itemBundle = Bundle().apply {
            putString(
                AddToCartExternalAnalytics.EE_PARAM_CATEGORY_ID,
                ""
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_DIMENSION_45,
//                setValueOrDefault(data.cartId)
                ""
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_BRAND,
                ""
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_CATEGORY,
                ""
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_ID,
//                setValueOrDefault(data.productId.toString())
                ""
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_NAME,
//                setValueOrDefault(product.name)
                ""
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_VARIANT,
                ""
            )
            putString(AddToCartExternalAnalytics.EE_PARAM_PRICE, "")
            putString(AddToCartExternalAnalytics.EE_PARAM_QUANTITY, "")
            putString(
                AddToCartExternalAnalytics.EE_PARAM_SHOP_ID,
                ""
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_SHOP_NAME,
                ""
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_SHOP_TYPE, ""
            )
        }
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.ADD_TO_CART)
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalyticsConstants.CLICK_BUY_AGAIN)
            putString(
                TrackAppUtils.EVENT_CATEGORY,
                TokoFoodAnalyticsConstants.TOKOFOOD_ORDER_DETAIL_PAGE
            )
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$orderId - ${foodItems.joinToString(prefix = ",") { it.foodName }}"
            )
            putString(
                TokoFoodAnalyticsConstants.BUSSINESS_UNIT,
                TokoFoodAnalyticsConstants.PHYSICAL_GOODS
            )
            putString(
                TokoFoodAnalyticsConstants.CURRENT_SITE,
                TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
            )
            putParcelableArrayList(
                AddToCartExternalAnalytics.EE_VALUE_ITEMS, arrayListOf(itemBundle)
            )
            putString(TokoFoodAnalyticsConstants.SHOP_ID, shopId)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            TokoFoodAnalyticsConstants.ADD_TO_CART, eventDataLayer
        )
    }
}