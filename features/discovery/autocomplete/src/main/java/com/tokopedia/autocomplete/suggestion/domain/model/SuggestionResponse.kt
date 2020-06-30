package com.tokopedia.autocomplete.suggestion.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse

data class SuggestionResponse(
        @SerializedName("universe_suggestion")
        val suggestionUniverse: SuggestionUniverse = SuggestionUniverse()
)