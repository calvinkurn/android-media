package com.tokopedia.promousage.domain.entity

sealed class PromoItemState {

    object Loading : PromoItemState()

    object Normal : PromoItemState()

    object Selected : PromoItemState()

    object Disabled : PromoItemState()

    object Ineligible : PromoItemState()
}
