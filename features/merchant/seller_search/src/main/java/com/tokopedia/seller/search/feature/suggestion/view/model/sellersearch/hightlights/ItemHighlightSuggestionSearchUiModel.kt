package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights

import androidx.compose.runtime.Stable

@Stable
class ItemHighlightSuggestionSearchUiModel(
    val id: String? = "",
    val title: String? = "",
    val desc: String? = "",
    val imageUrl: String? = "",
    val appUrl: String? = ""
)
