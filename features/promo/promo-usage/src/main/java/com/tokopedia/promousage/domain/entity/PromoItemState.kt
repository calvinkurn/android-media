package com.tokopedia.promousage.domain.entity

sealed class PromoItemState {

    object Loading : PromoItemState()

    object Normal : PromoItemState()

    object Selected : PromoItemState()

    data class Disabled(val message: String) : PromoItemState()

    data class Ineligible(val message: String) : PromoItemState()
}
