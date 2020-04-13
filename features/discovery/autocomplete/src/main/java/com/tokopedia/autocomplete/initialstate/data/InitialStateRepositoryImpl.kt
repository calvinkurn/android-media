package com.tokopedia.autocomplete.initialstate.data

import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import retrofit2.Response
import rx.Observable
import java.util.*

class InitialStateRepositoryImpl(private val initialStateDataSource: InitialStateDataSource) : InitialStateRepository {

    override fun getInitialStateData(parameters: HashMap<String, Any>): Observable<List<InitialStateData>> {
        return initialStateDataSource.getInitialState(parameters)
    }

    override fun deleteRecentSearch(parameters: HashMap<String, Any>): Observable<Boolean> {
        return initialStateDataSource.deleteRecentSearch(parameters)
    }
}