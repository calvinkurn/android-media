package com.tokopedia.db_inspector.presentation.schema

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.db_inspector.domain.schema.usecases.GetTablesUseCase
import com.tokopedia.db_inspector.domain.shared.models.Cell
import com.tokopedia.db_inspector.domain.shared.models.Page
import com.tokopedia.db_inspector.domain.shared.models.Statements
import com.tokopedia.db_inspector.domain.shared.models.parameters.ContentParameters
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
        tableListLiveData.postValue(page.cells)
    }

    private fun onTablesError(throwable: Throwable) {
        errorLiveData.postValue(throwable)
    }

}