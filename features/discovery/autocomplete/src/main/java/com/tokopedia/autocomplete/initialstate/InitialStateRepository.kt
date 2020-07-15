package com.tokopedia.autocomplete.initialstate

import retrofit2.Response
import rx.Observable
import java.util.*

interface InitialStateRepository {
    fun getInitialStateData(parameters: HashMap<String, Any>): Observable<List<InitialStateData>>

    fun deleteRecentSearch(parameters: HashMap<String, Any>): Observable<Boolean>
}