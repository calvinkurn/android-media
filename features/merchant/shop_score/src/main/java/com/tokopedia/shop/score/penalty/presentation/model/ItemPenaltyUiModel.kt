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
    val productName: String? = null,
    @ColorRes val colorPenalty: Int? = null,
    var isSelected: Boolean = false,
    val isOldPage: Boolean = true
) : BasePenaltyPage {
    override fun type(typeFactory: PenaltyPageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
