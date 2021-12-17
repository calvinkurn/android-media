package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionResponse(
        @SerializedName("universe_suggestion")
        @Expose
        val suggestionUniverse: SuggestionUniverse = SuggestionUniverse()
)