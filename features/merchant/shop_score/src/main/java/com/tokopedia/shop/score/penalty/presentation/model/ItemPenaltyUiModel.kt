package com.tokopedia.shop.score.penalty.presentation.model

import androidx.annotation.ColorRes
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory

data class ItemPenaltyUiModel(val statusPenalty: String = "",
                              val endDate: String = "",
                              val endDateDetail: String = "",
                              val startDate: String = "",
                              val typePenalty: String = "",
                              val descPenalty: String = "",
                              val deductionPoint: Int = 0,
                              @ColorRes val colorPenalty: Int? = null
) : BasePenaltyPage {
    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}