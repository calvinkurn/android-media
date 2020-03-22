package com.tokopedia.autocomplete.suggestion

import rx.Observable
import java.util.HashMap

interface SuggestionRepository {
    fun getSuggestionResponse(parameters: HashMap<String, Any>): Observable<SuggestionData>
    fun hitSuggestionUrlTracker(url: String): Observable<Void?>
}