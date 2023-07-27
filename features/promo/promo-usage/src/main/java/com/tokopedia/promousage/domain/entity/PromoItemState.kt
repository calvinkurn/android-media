package com.tokopedia.promousage.domain.entity

sealed class PromoItemState {

    object Loading : PromoItemState()

    data class Normal(val cardDetail: PromoItemCardDetail) : PromoItemState()

    data class Selected(val cardDetail: PromoItemCardDetail) : PromoItemState()

    data class Disabled(val message: String) : PromoItemState()

    data class Ineligible(val message: String) : PromoItemState()
}
