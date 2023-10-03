package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class PromoAccordionHeaderItem(
    override val id: String = "",
    val title: String = "",
    val totalPromoCount: Int = 0,
    val isExpanded: Boolean = false
) : DelegateAdapterItem
