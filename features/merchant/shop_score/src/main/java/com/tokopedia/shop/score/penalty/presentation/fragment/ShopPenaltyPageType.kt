package com.tokopedia.shop.score.penalty.presentation.fragment

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(ShopPenaltyPageType.ONGOING, ShopPenaltyPageType.HISTORY, ShopPenaltyPageType.NOT_YET_DEDUCTED)
annotation class ShopPenaltyPageType {
    companion object {
        const val ONGOING = "ongoing"
        const val HISTORY = "history"
        const val NOT_YET_DEDUCTED = "not_yet_deducted"
    }
}
