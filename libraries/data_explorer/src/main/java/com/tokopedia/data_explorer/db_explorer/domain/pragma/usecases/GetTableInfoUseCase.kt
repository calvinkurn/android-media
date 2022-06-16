package com.tokopedia.data_explorer.db_explorer.domain.pragma.usecases

import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.PragmaParameters
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

internal class GetTableInfoUseCase @Inject constructor(
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
): UseCase<Page>() {

    private lateinit var pragmaParameters: PragmaParameters.Pragma

    fun getTableInfo(onSuccess: (Page) -> Unit, onError: (Throwable) -> Unit, pragmaParameters: PragmaParameters.Pragma) {
        this.pragmaParameters = pragmaParameters
        this.execute({
            onSuccess(it)
        }, { onError (it)
        })
    }

    override suspend fun executeOnBackground(): Page {
        val connection = connectionRepository.open(ConnectionParameters(pragmaParameters.databasePath))
        val tableInfo = pragmaRepository.getTableInfo(pragmaParameters.copy(connection = connection))
        return tableInfo.copy(
            cells = tableInfo.cells.filterIndexed { index, _ ->
                index % 6 == 1
            }
        )
    }
    /* Pragma table info result --> total 6 items hence index % 6 == 1
    *   cid	Integer	Column index
        name	Text	Column name
        type	Text	Column type, as given
        notnull	Integer	Has a NOT NULL constraint
        dflt_value	Text	DEFAULT value
        pk	Integer	Is part of the PRIMARY KEY
    */
}