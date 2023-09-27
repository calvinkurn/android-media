package com.tokopedia.promousage.view.viewmodel

import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.util.composite.DelegateAdapterItem

sealed class PromoPageUiState {

    object Initial : PromoPageUiState()

    data class Success(
        val tickerInfo: PromoPageTickerInfo = PromoPageTickerInfo(),
        val items: List<DelegateAdapterItem> = emptyList(),
        val savingInfo: PromoSavingInfo = PromoSavingInfo(),
        val isCalculating: Boolean = false,
        val isReload: Boolean = false
    ) : PromoPageUiState()

    data class Error(
        val exception: Throwable
    ) : PromoPageUiState()
}
