package com.tokopedia.db_inspector.presentation.content

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.db_inspector.domain.pragma.usecases.GetTableInfoUseCase
import com.tokopedia.db_inspector.domain.shared.models.Cell
import com.tokopedia.db_inspector.domain.shared.models.Page
import com.tokopedia.db_inspector.domain.shared.models.Statements
import com.tokopedia.db_inspector.domain.shared.models.parameters.PragmaParameters
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class ContentViewModel @Inject constructor(
    private val getTableInfoUseCase: GetTableInfoUseCase
) : BaseViewModel(Dispatchers.Main) {
    val tableListLiveData = MutableLiveData<List<Cell>>()
    val errorLiveData = MutableLiveData<Throwable>()

    fun getTableInfo(
        databasePath: String,
        schemaName: String
    ) {
        getTableInfoUseCase.getTableInfo(
            ::onTablesFetched,
            ::onTablesError,
            PragmaParameters.Pragma(
                databasePath = databasePath,
                statement = Statements.Pragma.tableInfo(schemaName)
            )
        )
    }

    private fun onTablesFetched(page: Page) {
        tableListLiveData.postValue(page.cells)
    }

    private fun onTablesError(throwable: Throwable) {
        errorLiveData.postValue(throwable)
    }

}