package com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch

import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers

class DeleteRecentSearchUseCaseTest: UseCase<Boolean>(
    Schedulers.immediate(),
    Schedulers.immediate(),
) {

    override fun createObservable(p0: RequestParams?): Observable<Boolean> =
        Observable.fromCallable { true }
}