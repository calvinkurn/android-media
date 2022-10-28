package com.tokopedia.tokopedianow.category.domain.model

data class CategorySharingModel(
    val categoryIdLvl2: String,
    val categoryIdLvl3: String,
    val title: String,
    val deeplinkParam: String,
    val url: String,
    val utmCampaignList: List<String>
)