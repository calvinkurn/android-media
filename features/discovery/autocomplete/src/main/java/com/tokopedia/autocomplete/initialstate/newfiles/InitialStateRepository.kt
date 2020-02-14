package com.tokopedia.autocomplete.initialstate.newfiles

import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import rx.Observable
import java.util.HashMap

interface InitialStateRepository {
    fun getInitialStateData(parameters: HashMap<String, Any>): Observable<List<InitialStateData>>

    fun deleteRecentSearch(parameters: HashMap<String, Any>): Observable<Response<Void>>

    fun refreshPopularSearch(parameters: HashMap<String, Any>): Observable<List<InitialStateItem>>

    fun getInitialStateDataFlow(parameters: HashMap<String, Any>): Flow<List<InitialStateData>>
}