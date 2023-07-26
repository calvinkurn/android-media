package com.tokopedia.promousage.domain.entity

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

sealed class PromoPageState {

    object InitialShimmer : PromoPageState()

    object Loading : PromoPageState()

    object Empty : PromoPageState()

    data class Success(
        val tickerInfo: PromoPageTickerInfo,
        val sections: List<DelegateAdapterItem>
    ) : PromoPageState()

    object Error : PromoPageState()
}


