package com.tokopedia.shop.score.penalty.presentation.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory

data class ItemPenaltyUiModel(
    val statusPenalty: String = "",
    @StringRes val descStatusPenalty: Int? = null,
    val endDate: String = "",
    val endDateDetail: String = "",
    val startDate: String = "",
    val typePenalty: String = "",
    val invoicePenalty: String = "",
    val prefixDatePenalty: String = "",
    val reasonPenalty: String = "",
    val deductionPoint: String = "",
    @ColorRes val colorPenalty: Int? = null,
    var isSelected: Boolean = false
) : BasePenaltyPage {
    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}