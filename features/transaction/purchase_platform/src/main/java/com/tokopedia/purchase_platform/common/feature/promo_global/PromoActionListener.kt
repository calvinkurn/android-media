package com.tokopedia.purchase_platform.common.feature.promo_global

import com.tokopedia.promocheckout.common.view.model.PromoStackingData

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

interface PromoActionListener {

    fun onCartPromoUseVoucherGlobalPromoClicked(cartPromoGlobal: PromoStackingData, position: Int)

    fun onCartPromoCancelVoucherPromoGlobalClicked(cartPromoGlobal: PromoStackingData, position: Int)

    fun onPromoGlobalTrackingCancelled(cartPromoGlobal: PromoStackingData, position: Int)

    fun onClickDetailPromoGlobal(dataGlobal: PromoStackingData, position: Int)

    fun onPromoGlobalTrackingImpression(cartPromoGlobal: PromoStackingData)

}