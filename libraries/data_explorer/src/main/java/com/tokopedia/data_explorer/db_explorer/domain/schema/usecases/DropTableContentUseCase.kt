package com.tokopedia.data_explorer.db_explorer.domain.schema.usecases

import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ContentParameters
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

internal class DropTableContentUseCase @Inject constructor(
    private val schemaRepository: Repositories.Schema,
    private val connectionRepository: Repositories.Connection,
): UseCase<Page>() {

    private lateinit var input: ContentParameters

    fun dropTable(onSuccess: (Page) -> Unit, onError: (Throwable) -> Unit, contentParameters: ContentParameters) {
       this.input = contentParameters
        this.execute({
            onSuccess(it)
        }, {
            onError(it)
        })
    }

    override suspend fun executeOnBackground(): Page {
        val connection = connectionRepository.open(ConnectionParameters(databasePath = input.databasePath))

        val results = schemaRepository.dropByName(input.copy(connection = connection))
        connectionRepository.close(ConnectionParameters(databasePath = input.databasePath))
        return results
    }
}