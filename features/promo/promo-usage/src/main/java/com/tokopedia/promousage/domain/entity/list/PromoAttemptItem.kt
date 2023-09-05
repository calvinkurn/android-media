package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class PromoAttemptItem(
    override val id: String = "promo_attempt",
    val label: String = "",
    val attemptedPromoCode: String = "",
    val errorMessage: String = "",

    val hasOtherSection: Boolean = false,
    val hasAttemptedPromo: Boolean = false
) : DelegateAdapterItem
