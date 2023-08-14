package com.tokopedia.promousage.view.viewmodel

import com.tokopedia.promousage.domain.entity.PromoAttemptedError
import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.util.composite.DelegateAdapterItem

sealed class PromoPageUiState {

    object Initial : PromoPageUiState()

    data class Success(
        val tickerInfo: PromoPageTickerInfo,
        val items: List<DelegateAdapterItem>,
        val savingInfo: PromoSavingInfo = PromoSavingInfo(),
        val promoAttemptedError: PromoAttemptedError = PromoAttemptedError()
    ) : PromoPageUiState()

    data class Error(
        val exception: Throwable
    ) : PromoPageUiState()
}


