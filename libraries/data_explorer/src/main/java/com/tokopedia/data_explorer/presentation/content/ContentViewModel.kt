package com.tokopedia.data_explorer.presentation.content

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.data_explorer.domain.pragma.usecases.GetTableInfoUseCase
import com.tokopedia.data_explorer.domain.schema.usecases.GetTableContentUseCase
import com.tokopedia.data_explorer.domain.shared.models.Cell
import com.tokopedia.data_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.domain.shared.models.Statements
import com.tokopedia.data_explorer.domain.shared.models.parameters.ContentParameters
import com.tokopedia.data_explorer.domain.shared.models.parameters.PragmaParameters
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class ContentViewModel @Inject constructor(
    private val getTableInfoUseCase: GetTableInfoUseCase,
    private val getTableContentUseCase: GetTableContentUseCase,
) : BaseViewModel(Dispatchers.Main) {
    val columnHeaderLiveData = MutableLiveData<List<Cell>>()
    val contentLiveData = MutableLiveData<List<Cell>>()
    val errorLiveData = MutableLiveData<Throwable>()

    fun getTableInfo(
        databasePath: String,
        schemaName: String
    ) {
        getTableInfoUseCase.getTableInfo(
            ::onColumnHeaderFetched,
            ::onTablesError,
            PragmaParameters.Pragma(
                databasePath = databasePath,
                statement = Statements.Pragma.tableInfo(schemaName)
            )
        )
    }

    fun getTableContent(databasePath: String, schemaName: String) {
        getTableContentUseCase.getTable(
            ::onTableContentFetched,
            ::onTablesError,
            ContentParameters(
                databasePath = databasePath,
                statement = Statements.Schema.table(schemaName)
            )
        )
    }

    private fun onTableContentFetched(page: Page) {
        val mergedList= columnHeaderLiveData.value?.plus(page.cells)
        if (mergedList.isNullOrEmpty())
            contentLiveData.postValue(page.cells)
        else contentLiveData.postValue(mergedList)
    }

    private fun onColumnHeaderFetched(page: Page) {
        columnHeaderLiveData.postValue(page.cells)
    }

    private fun onTablesError(throwable: Throwable) {
        errorLiveData.postValue(throwable)
    }

}