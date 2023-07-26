package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class PromoInputItem(
    override val id: String = "promo_code"
) : DelegateAdapterItem
