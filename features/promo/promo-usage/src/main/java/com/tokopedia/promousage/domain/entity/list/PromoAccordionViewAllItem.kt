package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class PromoAccordionViewAllItem(
    val headerId: String = "",
    val hiddenPromoCount: Int = 0,
    val totalPromoCount: Int = 0,
    val isExpanded: Boolean = false,
    val isVisible: Boolean = false
) : DelegateAdapterItem {

    override val id: String
        get() = "view_all_$headerId"
}
