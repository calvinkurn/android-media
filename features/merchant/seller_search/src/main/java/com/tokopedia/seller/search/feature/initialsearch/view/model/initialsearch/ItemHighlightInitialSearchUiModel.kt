package com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch

import androidx.compose.runtime.Stable

@Stable
class ItemHighlightInitialSearchUiModel(
    val id: String? = "",
    val title: String? = "",
    val desc: String? = "",
    val imageUrl: String? = "",
    val appUrl: String? = ""
)
