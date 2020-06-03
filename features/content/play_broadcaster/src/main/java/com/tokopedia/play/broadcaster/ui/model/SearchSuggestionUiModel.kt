package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by jegul on 29/05/20
 */
data class SearchSuggestionUiModel(
        val queriedText: String,
        val suggestionText: String,
        val spannedSuggestion: CharSequence
)