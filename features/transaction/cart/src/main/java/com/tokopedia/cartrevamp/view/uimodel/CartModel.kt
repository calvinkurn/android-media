package com.tokopedia.cartrevamp.view.uimodel

import com.tokopedia.cartrevamp.data.model.response.promo.CartPromoTicker
import com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cartrevamp.domain.model.cartlist.SummaryTransactionUiModel
import com.tokopedia.cartrevamp.domain.model.updatecart.UpdateAndGetLastApplyData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel

data class CartModel(
    val wishlists: List<CartWishlistItemHolderData>? = null,
    val recentViewList: List<CartRecentViewItemHolderData>? = null,
    var isLastApplyResponseStillValid: Boolean = true,
    var lastValidateUseRequest: ValidateUsePromoRequest? = null,
    var lastValidateUseResponse: ValidateUsePromoRevampUiModel? = null,
    var lastUpdateCartAndGetLastApplyResponse: UpdateAndGetLastApplyData? = null,
    var cartListData: CartData? = null,
    var promoSummaryUiModel: PromoSummaryData? = null,
    var summaryTransactionUiModel: SummaryTransactionUiModel? = null,
    var summariesAddOnUiModel: HashMap<Int, String> = hashMapOf(),
    var showChoosePromoWidget: Boolean = false,
    var promoTicker: CartPromoTicker = CartPromoTicker(),
    var totalQtyWithAddon: Int = 0,
    var hasPerformChecklistChange: Boolean = false,
    var lca: LocalCacheModel? = null,
    var recommendationPage: Int = 1,
    var cartTopAdsHeadlineData: CartTopAdsHeadlineData? = null,
    var cartLoadingHolderData: CartLoadingHolderData? = null,
    var shipmentSellerCashbackModel: ShipmentSellerCashbackModel? = null,

    // adapter
    var tmpCollapsedUnavailableShop: MutableList<Any>? = null,
    var firstCartSectionHeaderPosition: Int = -1
)
