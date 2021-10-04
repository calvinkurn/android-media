package com.tokopedia.autocompletecomponent.initialstate.data

import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.domain.refreshinitialstate.InitialStateRepository
import rx.Observable
import java.util.*

class InitialStateRepositoryImpl(private val initialStateDataSource: InitialStateDataSource) : InitialStateRepository {

    override fun getInitialStateData(parameters: HashMap<String, Any>): Observable<List<InitialStateData>> {
        return initialStateDataSource.getInitialState(parameters)
    }
}