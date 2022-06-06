package com.tokopedia.data_explorer.db_explorer.presentation.databases

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.CopyDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.GetDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.RemoveDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.DatabaseParameters
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import javax.inject.Inject

internal class DatabaseViewModel @Inject constructor(
    private val getDatabaseUseCase: GetDatabasesUseCase,
    private val removeDatabaseUseCase: RemoveDatabasesUseCase,
    private val copyDatabasesUseCase: CopyDatabasesUseCase,
) : BaseViewModel(Dispatchers.Main) {
    val databaseListLiveData = MutableLiveData<List<DatabaseDescriptor>>()
    val actionPerformedLiveData = MutableLiveData<Result<String>>()
    val errorLiveData = MutableLiveData<Throwable>()

    fun browseDatabases(query: String? = null) {
        getDatabaseUseCase.getDatabases(
            ::onDatabaseListFetched,
            ::onDatabaseListError,
            DatabaseParameters.Get(query)
        )
    }

    fun removeDatabase(databaseDescriptor: DatabaseDescriptor) {
        removeDatabaseUseCase.removeDatabases(
            {
                if (it.isNotEmpty()) {
                    actionPerformedLiveData.postValue(Success("Database removed successfully"))
                    browseDatabases()
                } else actionPerformedLiveData.postValue(Fail(IOException("Deletion cannot be performed")))
            },
            { actionPerformedLiveData.postValue(Fail(it)) },
            DatabaseParameters.Command(databaseDescriptor = databaseDescriptor)
        )
    }

    fun copyDatabase(databaseDescriptor: DatabaseDescriptor) {
        copyDatabasesUseCase.copyDatabases(
            {
                if (it.isNotEmpty()) {
                    actionPerformedLiveData.postValue(Success("Database copied successfully"))
                    browseDatabases()
                } else
                    actionPerformedLiveData.postValue(Fail(IOException("Copy cannot be performed")))
            },
            {
                actionPerformedLiveData.postValue(Fail(it))
            },
            DatabaseParameters.Command(databaseDescriptor = databaseDescriptor)
        )
    }

    private fun onDatabaseListFetched(list: List<DatabaseDescriptor>) {
        databaseListLiveData.postValue(list)
    }

    private fun onDatabaseListError(throwable: Throwable) {
        errorLiveData.postValue(throwable)
    }

}