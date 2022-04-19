package com.tokopedia.autocompletecomponent.initialstate.domain.refreshinitialstate

import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

open class RefreshInitialStateUseCase(
    protected val initialStateRepository: InitialStateRepository
) : UseCase<List<InitialStateData>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<InitialStateData>> {
        return initialStateRepository.getInitialStateData(requestParams.parameters)
    }
}