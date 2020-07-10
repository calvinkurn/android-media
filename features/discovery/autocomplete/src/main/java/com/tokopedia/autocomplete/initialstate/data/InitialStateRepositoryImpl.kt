package com.tokopedia.autocomplete.initialstate.data

import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import rx.Observable
import java.util.*

class InitialStateRepositoryImpl(private val initialStateDataSource: InitialStateDataSource) : InitialStateRepository {

    override fun getInitialStateData(parameters: HashMap<String, Any>): Observable<List<InitialStateData>> {
        return initialStateDataSource.getInitialState(parameters)
    }
}