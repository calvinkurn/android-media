package com.tokopedia.autocomplete.initialstate.newfiles

import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import rx.Observable
import java.util.HashMap

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

    override fun getInitialStateDataFlow(parameters: HashMap<String, Any>): Flow<List<InitialStateData>> = flow {
        emit(initialStateDataSource.getInitialStateFlow(parameters).data)
    }
}