package com.tokopedia.autocompletecomponent.suggestion.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse

data class SuggestionTestResponse(
    @SerializedName("universe_suggestion_test")
    @Expose
    val suggestionUniverse: SuggestionUniverse
)