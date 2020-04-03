package com.tokopedia.autocomplete.suggestion

import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SuggestionTrackerUseCase(
        private val suggestionRepository: SuggestionRepository
) : UseCase<Void?>() {

    companion object {

        const val URL_TRACKER = "url_tracker"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<Void?> {
        val urlTracker = requestParams?.getString(URL_TRACKER, "") ?: ""

        return suggestionRepository.hitSuggestionUrlTracker(urlTracker)
    }
}