package com.tokopedia.db_inspector.presentation.databases

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.db_inspector.domain.databases.usecases.GetDatabasesUseCase
import com.tokopedia.db_inspector.domain.shared.models.parameters.DatabaseParameters
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class DatabaseViewModel @Inject constructor(
    private val getDatabaseUseCase: GetDatabasesUseCase
): BaseViewModel(Dispatchers.Main) {
    val databaseListLiveData = MutableLiveData<List<DatabaseDescriptor>>()
    val errorLiveData = MutableLiveData<Throwable>()

    fun browseDatabases(query: String? = null) {
        getDatabaseUseCase.getDatabases(
            ::onDatabaseListFetched,
            ::onDatabaseListError,
            DatabaseParameters.Get(query)
        )
    }

    private fun onDatabaseListFetched(list: List<DatabaseDescriptor>) {
        databaseListLiveData.postValue(list)
    }

    private fun onDatabaseListError(throwable: Throwable) {
        errorLiveData.postValue(throwable)
    }

}