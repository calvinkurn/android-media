package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class PromoAccordionViewAllItem(
    override val id: String = "promo_accordion_view_all",
    val headerId: String = "",
    val hiddenPromoCount: Int = 0,
    val isExpanded: Boolean = false,
    val isVisible: Boolean = false
) : DelegateAdapterItem
