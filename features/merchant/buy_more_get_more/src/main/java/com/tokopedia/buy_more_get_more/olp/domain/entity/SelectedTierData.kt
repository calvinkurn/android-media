package com.tokopedia.buy_more_get_more.olp.domain.entity

import com.tokopedia.bmsm_widget.domain.entity.MainProduct
import com.tokopedia.bmsm_widget.domain.entity.TierGifts

data class SelectedTierData(
    val selectedTier: OfferInfoForBuyerUiModel.Offering.Tier,
    val offerInfo: OfferInfoForBuyerUiModel,
    val tierGifts: List<TierGifts>,
    val mainProducts: List<MainProduct>
)
