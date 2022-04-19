package com.tokopedia.autocompletecomponent.suggestion.domain

import rx.Observable

interface SuggestionRepository {
    fun hitSuggestionUrlTracker(url: String): Observable<Void?>
}