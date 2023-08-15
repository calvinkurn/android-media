package com.tokopedia.minicart.bmgm.presentation.model

/**
 * Created by @ilhamsuaib on 14/08/23.
 */

data class BmgmMiniCartDataUiModel(
    val offerId: Long = 0L,
    val offerName: String = "",
    val offerMessage: String = "",
    val hasReachMaxDiscount: Boolean = false,
    val totalDiscount: Double = 0.0,
    val priceBeforeBenefit: Double = 0.0,
    val finalPrice: Double = 0.0,
    val showMiniCartFooter: Boolean = false,
    val tiersApplied: List<BmgmMiniCartVisitable.TierUiModel> = emptyList()
)