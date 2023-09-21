package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class PromoTncItem(
    override val id: String = "promo_tnc",
    val selectedPromoCodes: List<String> = emptyList(),
    val selectedPromoCodesWithTitle: List<String> = emptyList()
) : DelegateAdapterItem
