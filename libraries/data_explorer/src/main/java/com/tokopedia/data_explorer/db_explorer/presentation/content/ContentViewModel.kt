package com.tokopedia.data_explorer.db_explorer.presentation.content

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.data_explorer.db_explorer.data.Data
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Order
import com.tokopedia.data_explorer.db_explorer.domain.pragma.usecases.GetTableInfoUseCase
import com.tokopedia.data_explorer.db_explorer.domain.schema.usecases.DropTableContentUseCase
import com.tokopedia.data_explorer.db_explorer.domain.schema.usecases.GetTableContentUseCase
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Statements
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ContentParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.PragmaParameters
import com.tokopedia.data_explorer.db_explorer.extensions.InvalidPageRequestException
import com.tokopedia.data_explorer.db_explorer.presentation.Constants
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class ContentViewModel @Inject constructor(
    private val getTableInfoUseCase: GetTableInfoUseCase,
    private val getTableContentUseCase: GetTableContentUseCase,
    private val dropTableContentUseCase: DropTableContentUseCase
) : BaseViewModel(Dispatchers.Main) {

    val columnHeaderLiveData = MutableLiveData<List<Cell>>()
    val contentLiveData = MutableLiveData<List<Cell>>()
    val errorLiveData = MutableLiveData<Throwable>()
    val resultRowLiveData = MutableLiveData<Boolean>()
    @VisibleForTesting
    var columnHeaderList = listOf<Cell>()

    var totalResults: Int = 0
    var currentPage: Int = 1
    lateinit var databasePath: String

    fun getTableInfo(schemaName: String) {
        getTableInfoUseCase.getTableInfo(
            ::onColumnHeaderFetched,
            ::onTablesError,
            PragmaParameters.Pragma(
                databasePath = databasePath,
                statement = Statements.Pragma.tableInfo(schemaName)
            )
        )
    }

    fun getTableRowsCount(schemaName: String) {
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

    fun getTableContent(schemaName: String, orderBy: String?, sort: Order) {
        if (totalResults != 0 && validatePageRequest(currentPage)) {
            getTableContentUseCase.getTable(
                ::onTableContentFetched,
                ::onTablesError,
                ContentParameters(
                    databasePath = databasePath,
                    statement = Statements.Schema.table(schemaName, orderBy, sort, currentPage)
                )
            )
        } else {
            if (totalResults != 0) {
                --currentPage
                onTablesError(InvalidPageRequestException(Constants.ErrorMessages.INVALID_PAGE_REQUEST))
            } else onTablesError(NullPointerException(Constants.ErrorMessages.NO_CONTENT))
        }
    }

    private fun validatePageRequest(page: Int?): Boolean {
        val requestedResults = (page ?: 1) * Data.Constants.Limits.PAGE_SIZE
        return totalResults - requestedResults > -Data.Constants.Limits.PAGE_SIZE
    }

    fun dropTable(schemaName: String) {
        dropTableContentUseCase.dropTable({
            onTablesError(NullPointerException(Constants.ErrorMessages.DELETION_SUCCESS))
        }, {
            onTablesError(it)
        },
        ContentParameters(
            databasePath = databasePath,
            statement = Statements.Schema.dropContent(schemaName)
        ))
    }

    private fun onTableContentFetched(page: Page) {
        val mergedList= columnHeaderList.plus(page.cells)
        if (mergedList.isNullOrEmpty())
            contentLiveData.postValue(page.cells)
        else contentLiveData.postValue(mergedList)
    }

    private fun onColumnHeaderFetched(page: Page) {
        columnHeaderList = page.cells
        columnHeaderLiveData.postValue(page.cells)
    }

    private fun onTablesError(throwable: Throwable) {
        errorLiveData.postValue(throwable)
    }

    fun updateHeader(cell: Cell) {
        columnHeaderList = columnHeaderList.map {
            if (it.text == cell.text)
                cell.copy(active = true)
            else it.copy(active = false)
        }
    }

}