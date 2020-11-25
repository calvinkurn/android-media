package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by jegul on 29/05/20
 */
data class SearchSuggestionUiModel(
        val queriedText: String,
        val suggestedId: String,
        val suggestedText: String,
        val spannedSuggestion: CharSequence
)