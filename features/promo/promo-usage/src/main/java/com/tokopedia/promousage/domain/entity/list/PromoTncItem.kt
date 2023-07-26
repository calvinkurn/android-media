package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class PromoTncItem(
    override val id: String = "promo_tnc"
) : DelegateAdapterItem
