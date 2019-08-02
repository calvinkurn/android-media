package com.tokopedia.checkout.view.common

import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion
import com.tokopedia.promocheckout.common.view.model.PromoStackingData

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

interface PromoActionListener {

    fun onCartPromoSuggestionButtonCloseClicked(cartPromoSuggestion: CartPromoSuggestion, position: Int)

    fun onCartPromoUseVoucherGlobalPromoClicked(cartPromoGlobal: PromoStackingData, position: Int)

    fun onCartPromoCancelVoucherPromoGlobalClicked(cartPromoGlobal: PromoStackingData, position: Int)

    fun onCartPromoGlobalTrackingCancelled(cartPromoGlobal: PromoStackingData, position: Int)

    fun onClickDetailPromoGlobal(dataGlobal: PromoStackingData, position: Int)

    fun onCartPromoGlobalTrackingImpression(cartPromoGlobal: PromoStackingData, position: Int)

}