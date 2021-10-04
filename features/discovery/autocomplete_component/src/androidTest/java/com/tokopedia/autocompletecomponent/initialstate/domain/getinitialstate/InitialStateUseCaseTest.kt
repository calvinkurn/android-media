package com.tokopedia.autocompletecomponent.initialstate.domain.getinitialstate

import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.test.R
import com.tokopedia.autocompletecomponent.utils.rawToObject
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers

class InitialStateUseCaseTest: UseCase<InitialStateUniverse>(
    Schedulers.immediate(),
    Schedulers.immediate(),
) {

    override fun createObservable(p0: RequestParams?): Observable<InitialStateUniverse> =
        Observable.fromCallable {
            rawToObject<InitialStateTestResponse>(R.raw.initial_state_response_1)
                .initialStateUniverse
        }
}