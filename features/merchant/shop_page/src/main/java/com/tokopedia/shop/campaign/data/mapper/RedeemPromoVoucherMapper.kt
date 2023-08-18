package com.tokopedia.shop.campaign.data.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.campaign.data.response.RedeemPromoVoucherResponse
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import javax.inject.Inject

class RedeemPromoVoucherMapper @Inject constructor() {

    fun map(response: RedeemPromoVoucherResponse): RedeemPromoVoucherResult {
        val redeemResult = response.hachikoRedeem
        val redeemMessage = redeemResult.redeemMessage
        val voucherCode = redeemResult.coupons.firstOrNull()?.code.orEmpty()
        val voucherPromoId = redeemResult.coupons.firstOrNull()?.promoId?.toLong().orZero()

        return RedeemPromoVoucherResult(redeemMessage, voucherCode, voucherPromoId)
    }

}
