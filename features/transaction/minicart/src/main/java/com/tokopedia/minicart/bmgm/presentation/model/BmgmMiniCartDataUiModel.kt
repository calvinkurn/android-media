package com.tokopedia.minicart.bmgm.presentation.model

import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import com.tokopedia.utils.currency.CurrencyFormatUtil

/**
 * Created by @ilhamsuaib on 14/08/23.
 */

data class BmgmMiniCartDataUiModel(
    val offerId: Long = BmgmCommonDataModel.NON_DISCOUNT_TIER_ID,
    val offerName: String = "",
    val offerMessage: String = "",
    val hasReachMaxDiscount: Boolean = false,
    val totalDiscount: Double = 0.0,
    val priceBeforeBenefit: Double = 0.0,
    val finalPrice: Double = 0.0,
    val tiersApplied: List<BmgmMiniCartVisitable.TierUiModel> = emptyList()
) {
    fun getPriceBeforeDiscountStr(): String {
        return CurrencyFormatUtil.convertPriceValueToIdrFormat(priceBeforeBenefit, false)
    }

    fun getPriceAfterDiscountStr(): String {
        return CurrencyFormatUtil.convertPriceValueToIdrFormat(finalPrice, false)
    }
}