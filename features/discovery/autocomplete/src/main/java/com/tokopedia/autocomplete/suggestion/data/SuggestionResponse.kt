package com.tokopedia.autocomplete.suggestion.data

import com.google.gson.annotations.SerializedName

data class SuggestionResponse(
        @SerializedName("universe_suggestion")
        val suggestionUniverse: SuggestionUniverse = SuggestionUniverse()
)