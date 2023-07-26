package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class PromoAccordionViewAllItem(
    override val id: String = "promo_view_all",
    val promoCount: Int = 0
) : DelegateAdapterItem
