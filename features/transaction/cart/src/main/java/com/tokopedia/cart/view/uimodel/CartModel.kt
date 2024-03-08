package com.tokopedia.cart.view.uimodel

import com.tokopedia.cart.data.model.response.promo.CartPromoTicker
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.domain.model.updatecart.UpdateAndGetLastApplyData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata

data class CartModel(
    var wishlists: List<CartWishlistItemHolderData>? = null,
    var recommendationWidgetMetadata: RecommendationWidgetMetadata = RecommendationWidgetMetadata(),
    var isLastApplyResponseStillValid: Boolean = true,
    var lastValidateUseRequest: ValidateUsePromoRequest? = null,
    var lastValidateUseResponse: ValidateUsePromoRevampUiModel? = null,
    var lastUpdateCartAndGetLastApplyResponse: UpdateAndGetLastApplyData? = null,
    var cartListData: CartData? = null,
    var showChoosePromoWidget: Boolean = false,
    var promoTicker: CartPromoTicker = CartPromoTicker(),
    var totalQtyWithAddon: Int = 0,
    var hasPerformChecklistChange: Boolean = false,
    var lca: LocalCacheModel? = null,
    var recommendationPage: Int = 1,
    var cartTopAdsHeadlineData: CartTopAdsHeadlineData? = null,
    var cartLoadingHolderData: CartLoadingHolderData? = null,
    var shipmentSellerCashbackModel: ShipmentSellerCashbackModel? = null,
    var lastCartShopGroupTickerCartString: String = "",
    var toBeDeletedBundleGroupId: String = "",
    var latestCartTotalAmount: Double = 0.0,
    var lastOfferId: String = "",

    var availableCartItemImpressionList: MutableSet<CartItemHolderData> = mutableSetOf(),
    var discountAmount: Long = 0L,

    // adapter
    var tmpCollapsedUnavailableShop: DisabledCollapsedHolderData? = null,
    var firstCartSectionHeaderPosition: Int = -1,
    var tmpAllUnavailableShop: MutableList<Any>? = null
)
