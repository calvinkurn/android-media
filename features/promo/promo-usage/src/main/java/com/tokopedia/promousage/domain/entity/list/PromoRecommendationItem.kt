package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.domain.entity.Promo
import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class PromoRecommendationItem(
    override val id: String = "",
    val title: String = "",
    val codes: List<String> = emptyList(),
    val message: String = "",
    val messageSelected: String = "",
    val backgroundUrl: String = "",
    val animationUrl: String = "",
    val promos: List<Promo> = emptyList()
) : DelegateAdapterItem
