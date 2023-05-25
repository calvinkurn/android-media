package com.tokopedia.shop.score.penalty.presentation.model

import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory

data class ItemPenaltyInfoNotificationUiModel(
    val notificationCount: Int,
    val shouldShowDot: Boolean
): BasePenaltyPage {

    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }

}
