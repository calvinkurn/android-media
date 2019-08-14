package com.tokopedia.purchase_platform.common.feature.promo

import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData
import com.tokopedia.promocheckout.common.view.model.PromoStackingData

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

interface PromoActionListener {

    fun onCartPromoSuggestionButtonCloseClicked(cartPromoSuggestionHolderData: CartPromoSuggestionHolderData, position: Int)

    fun onCartPromoUseVoucherGlobalPromoClicked(cartPromoGlobal: PromoStackingData, position: Int)

    fun onCartPromoCancelVoucherPromoGlobalClicked(cartPromoGlobal: PromoStackingData, position: Int)

    fun onCartPromoGlobalTrackingCancelled(cartPromoGlobal: PromoStackingData, position: Int)

    fun onClickDetailPromoGlobal(dataGlobal: PromoStackingData, position: Int)

    fun onCartPromoGlobalTrackingImpression(cartPromoGlobal: PromoStackingData, position: Int)

}