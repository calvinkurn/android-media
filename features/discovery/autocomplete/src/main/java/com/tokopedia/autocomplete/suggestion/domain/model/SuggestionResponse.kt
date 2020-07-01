package com.tokopedia.autocomplete.suggestion.domain.model

import com.google.gson.annotations.SerializedName

data class SuggestionResponse(
        @SerializedName("universe_suggestion")
        val suggestionUniverse: SuggestionUniverse = SuggestionUniverse()
)