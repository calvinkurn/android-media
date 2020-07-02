package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch

data class ItemSellerSearchUiModel(
        val id: String? = "",
        val title: String? = "",
        val desc: String? = "",
        val imageUrl: String? = "",
        val appUrl: String? = "",
        val url: String? = "",
        val keyword: String? = "",
        val section: String? = ""
)