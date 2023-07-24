package com.tokopedia.promousage.domain.entity

data class PromoRecommendationSection(
    @PromoSectionId val id: String = SECTION_RECOMMENDATION,
    val title: String = "",
    val codes: List<String> = emptyList(),
    val message: String = "",
    val messageSelected: String = "",
    val backgroundUrl: String = "",
    val animationUrl: String = "",
    val promos: List<Promo> = emptyList()
) : PromoSection()
