package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType

data class ItemPenaltyEmptyStateUiModel(
    @ShopPenaltyPageType val pageType: String
): BasePenaltyPage {

    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
