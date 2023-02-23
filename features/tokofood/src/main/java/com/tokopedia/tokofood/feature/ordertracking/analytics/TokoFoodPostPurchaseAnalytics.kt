package com.tokopedia.tokofood.feature.ordertracking.analytics

import android.os.Bundle
import com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.MerchantDataUiModel
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
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_ORDER_DETAIL_PAGE,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.VIEW_TOKOFOOD_ORDER_DETAIL_PAGE,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.IS_LOGGED_IN_STATUS to userSession.isLoggedIn.toString(),
            TokoFoodAnalyticsConstants.SCREEN_NAME to TokoFoodAnalyticsConstants.TOKOFOOD_ORDER_DETAIL_PAGE,
            TokoFoodAnalyticsConstants.SHOP_ID to shopId,
            TokoFoodAnalyticsConstants.USER_ID to userSession.userId
        )
        tracking.sendScreenAuthenticated(TokoFoodAnalyticsConstants.TOKOFOOD_ORDER_DETAIL_PAGE, mapData)
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

    fun clickChatIcon(orderStatus: String, orderId: String, channelId: String, source: String, role: String, unReadChatCounter: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_CHAT_FROM_ORDER_DETAIL,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOCHAT_ORDER_DETAIL,
            TrackAppUtils.EVENT_LABEL to "$orderStatus - $orderId - $channelId - $source - $role $unReadChatCounter",
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.COMMUNICATION,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.TRACKER_ID to TokoFoodAnalyticsConstants.TRACKER_ID_39065
        )
        tracking.sendGeneralEvent(mapData)
    }

    fun clickBuyAgainButton(orderId: String, merchantData: MerchantDataUiModel, foodItems: List<FoodItemUiModel>) {
        val bundleList = arrayListOf<Bundle>()
        foodItems.forEach {
            val itemBundle = Bundle().apply {
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_CATEGORY_ID,
                    it.categoryId
                )
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_DIMENSION_45,
                    it.cartId
                )
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_BRAND,
                    String.EMPTY
                )
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_CATEGORY,
                    it.categoryName
                )
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_ID,
                    it.itemId
                )
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_NAME,
                    it.foodName
                )
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_ITEM_VARIANT,
                    it.addOnVariantList.joinToString(separator = ",") { it.displayName }
                )
                putString(AddToCartExternalAnalytics.EE_PARAM_PRICE, it.priceStr)
                putString(AddToCartExternalAnalytics.EE_PARAM_QUANTITY, it.quantity)
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_SHOP_ID,
                    merchantData.merchantId
                )
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_SHOP_NAME,
                    merchantData.merchantName
                )
                putString(
                    AddToCartExternalAnalytics.EE_PARAM_SHOP_TYPE,
                    ""
                )
            }
            bundleList.add(itemBundle)
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
                AddToCartExternalAnalytics.EE_VALUE_ITEMS,
                bundleList
            )
            putString(TokoFoodAnalyticsConstants.SHOP_ID, merchantData.merchantId)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
        }

        tracking.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.ADD_TO_CART, eventDataLayer)
    }
}
