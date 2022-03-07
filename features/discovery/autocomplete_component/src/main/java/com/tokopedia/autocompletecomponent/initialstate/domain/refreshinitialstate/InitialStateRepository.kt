package com.tokopedia.autocompletecomponent.initialstate.domain.refreshinitialstate

import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import rx.Observable
import java.util.*

interface InitialStateRepository {
    fun getInitialStateData(parameters: HashMap<String, Any>): Observable<List<InitialStateData>>
}