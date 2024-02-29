package com.tokopedia.buy_more_get_more.olp.presentation.listener

import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel

interface OfferingInfoListener {
    fun onTncClicked()
    fun onShopNameClicked(shopId: Long)
    fun onTierClicked(
        selectedTier: OfferInfoForBuyerUiModel.Offering.Tier,
        offerInfo: OfferInfoForBuyerUiModel
    )
}
