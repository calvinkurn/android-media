package com.tokopedia.autocompletecomponent.initialstate.data

import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import rx.Observable

class InitialStateDataSource(
        private val initialStateApi: InitialStateApi,
        private val initialStateMapper: InitialStateMapper
) {
    fun getInitialState(param: HashMap<String, Any>): Observable<List<InitialStateData>> {
        return initialStateApi.getInitialState(param)
                .map(initialStateMapper)
    }
}