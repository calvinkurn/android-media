package com.tokopedia.promousage.domain.entity

sealed class PromoState {

    object Loading : PromoState()

    object Normal : PromoState()

    object Selected : PromoState()

    object Disabled : PromoState()

    object Ineligible : PromoState()
}
