package com.tokopedia.seller.search.feature.initialsearch.view.model.compose

import androidx.compose.ui.text.TextRange
import com.tokopedia.kotlin.extensions.view.EMPTY

data class GlobalSearchUiState(
    val searchBarPlaceholder: String = String.EMPTY,
    val searchBarKeyword: String = String.EMPTY,
    val selection: TextRange = TextRange(searchBarKeyword.length)
)
