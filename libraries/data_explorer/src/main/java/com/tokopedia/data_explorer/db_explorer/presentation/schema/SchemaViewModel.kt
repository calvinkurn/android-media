package com.tokopedia.data_explorer.db_explorer.presentation.schema

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.data_explorer.db_explorer.domain.schema.usecases.GetTablesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Statements
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ContentParameters
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class SchemaViewModel @Inject constructor(
    private val getTablesUseCase: GetTablesUseCase
) : BaseViewModel(Dispatchers.Main) {
    val tableListLiveData = MutableLiveData<List<Cell>>()
    val errorLiveData = MutableLiveData<Throwable>()

    fun getTables(
        databasePath: String,
        query: String? = null
    ) {
        getTablesUseCase.getTables(
            ::onTablesFetched,
            ::onTablesError,
            ContentParameters(
                databasePath = databasePath,
                statement = Statements.Schema.tables(query)
            )
        )
    }

    private fun onTablesFetched(page: Page) {
        val filteredTables = page.cells.filterNot { IGNORED_TABLES.contains(it.text) }
        tableListLiveData.postValue(filteredTables)
    }

    private fun onTablesError(throwable: Throwable) {
        errorLiveData.postValue(throwable)
    }

    companion object {
        val IGNORED_TABLES = listOf("android_metadata", "sqlite_sequence")
    }
}