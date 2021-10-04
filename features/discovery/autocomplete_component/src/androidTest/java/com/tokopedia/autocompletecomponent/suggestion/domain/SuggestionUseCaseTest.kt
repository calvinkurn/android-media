package com.tokopedia.autocompletecomponent.suggestion.domain

import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocompletecomponent.test.R
import com.tokopedia.autocompletecomponent.utils.rawToObject
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers

class SuggestionUseCaseTest: UseCase<SuggestionUniverse>(
    Schedulers.immediate(),
    Schedulers.immediate(),
) {
    override fun createObservable(p0: RequestParams?): Observable<SuggestionUniverse> =
        Observable.fromCallable {
            rawToObject<SuggestionTestResponse>(R.raw.suggestion_response_1)
                .suggestionUniverse
        }
}