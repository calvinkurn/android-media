package com.tokopedia.data_explorer.presentation.content

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.data_explorer.data.Data
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
    val resultRowLiveData = MutableLiveData<Boolean>()

    var totalResults: Int = 0
    var currentPage: Int = 1

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

    fun getTableRowsCount(databasePath: String, schemaName: String) {
        getTableContentUseCase.getTable(
            {
                totalResults = it.cells.getOrNull(0)?.text?.toInt() ?: 0
                resultRowLiveData.postValue(true)
            },
            { resultRowLiveData.postValue(false) },
            ContentParameters(
                databasePath = databasePath,
                statement = Statements.Schema.count(schemaName)
            )
        )
    }

    fun getTableContent(databasePath: String, schemaName: String, page: Int? = 1) {
        if (totalResults != 0 && validatePageRequest(page)) {
            currentPage = page ?: 1
            getTableContentUseCase.getTable(
                ::onTableContentFetched,
                ::onTablesError,
                ContentParameters(
                    databasePath = databasePath,
                    statement = Statements.Schema.table(schemaName, page)
                )
            )
        } else {
            onTablesError(IllegalStateException("Incorrect Request"))
        }
    }

    private fun validatePageRequest(page: Int?): Boolean {
        val requestedResults = (page ?: 1) * Data.Constants.Limits.PAGE_SIZE
        return requestedResults - totalResults <= Data.Constants.Limits.PAGE_SIZE
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