package com.tokopedia.autocomplete.initialstate

import retrofit2.Response
import rx.Observable
import java.util.*

class InitialStateRepositoryImpl(private val initialStateDataSource: InitialStateDataSource) : InitialStateRepository {

    override fun getInitialStateData(parameters: HashMap<String, Any>): Observable<List<InitialStateData>> {
        return initialStateDataSource.getInitialState(parameters)
    }

    override fun deleteRecentSearch(parameters: HashMap<String, Any>): Observable<Response<Void>> {
        return initialStateDataSource.deleteRecentSearch(parameters)
    }

    override fun refreshPopularSearch(parameters: HashMap<String, Any>): Observable<List<InitialStateItem>> {
        return initialStateDataSource.refreshPopularSearch(parameters)
    }
}