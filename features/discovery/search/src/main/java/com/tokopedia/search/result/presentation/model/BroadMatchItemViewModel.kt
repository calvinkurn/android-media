package com.tokopedia.search.result.presentation.model

data class BroadMatchItemViewModel(
        val id: String = "",
        val name: String = "",
        val price: Int = 0,
        val imageUrl: String = "",
        val rating: Int = 0,
        val countReview: Int = 0,
        val url: String = "",
        val applink: String = "",
        val priceString: String = ""
)