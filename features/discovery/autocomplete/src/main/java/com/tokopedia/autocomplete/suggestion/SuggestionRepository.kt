package com.tokopedia.autocomplete.suggestion

import rx.Observable

interface SuggestionRepository {
    fun hitSuggestionUrlTracker(url: String): Observable<Void?>
}