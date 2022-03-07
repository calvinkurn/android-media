package com.tokopedia.autocompletecomponent.initialstate.domain.refreshinitialstate

import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.utils.rawToObject
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.autocompletecomponent.test.R
import rx.Observable
import rx.schedulers.Schedulers

class RefreshInitialStateUseCaseTest: UseCase<List<InitialStateData>>(
    Schedulers.immediate(),
    Schedulers.immediate(),
) {

    override fun createObservable(p0: RequestParams?): Observable<List<InitialStateData>> =
        Observable.fromCallable {
            rawToObject<InitialStateUniverse>(R.raw.refresh_initial_state_response)
                .data
        }

}