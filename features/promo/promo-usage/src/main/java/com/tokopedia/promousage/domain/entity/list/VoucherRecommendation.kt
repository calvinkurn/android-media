package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.domain.entity.Promo
import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class VoucherRecommendation(
    val title: String,
    val promos: List<Promo>
) : DelegateAdapterItem {
    override fun id() = promos
}
