package com.tokopedia.promousage.domain.entity

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

sealed class PromoPageState {

    object Initial : PromoPageState()

    object Loading : PromoPageState()

    data class Success(
        val tickerInfo: PromoPageTickerInfo,
        val items: List<DelegateAdapterItem>,
        val savingInfo: PromoSavingInfo = PromoSavingInfo()
    ) : PromoPageState()

    object Error : PromoPageState()
}


