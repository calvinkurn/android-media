package com.tokopedia.autocomplete.initialstate

import rx.Observable
import java.util.*

interface InitialStateRepository {
    fun getInitialStateData(parameters: HashMap<String, Any>): Observable<List<InitialStateData>>
}