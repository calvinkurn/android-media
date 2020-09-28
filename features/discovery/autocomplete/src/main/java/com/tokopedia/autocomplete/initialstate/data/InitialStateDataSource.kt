package com.tokopedia.autocomplete.initialstate.data

import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.InitialStateMapper
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