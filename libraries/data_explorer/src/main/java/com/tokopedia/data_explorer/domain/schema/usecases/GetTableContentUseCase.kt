package com.tokopedia.data_explorer.domain.schema.usecases

import com.tokopedia.data_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.data_explorer.domain.shared.models.parameters.ContentParameters
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

internal class GetTableContentUseCase @Inject constructor(
    private val schemaRepository: Repositories.Schema,
    private val connectionRepository: Repositories.Connection,
): UseCase<Page>() {

    private lateinit var contentParameters: ContentParameters

    fun getTable(onSuccess: (Page) -> Unit, onError: (Throwable) -> Unit, contentParameters: ContentParameters) {
       this.contentParameters = contentParameters
        this.execute({
            onSuccess(it)
        }, {
            onError(it)
        })
    }

    override suspend fun executeOnBackground(): Page {
        val input = this.contentParameters
        val connection = connectionRepository.open(ConnectionParameters(databasePath = input.databasePath))

        val results = schemaRepository.getByName(input.copy(connection = connection))
        connectionRepository.close(ConnectionParameters(databasePath = input.databasePath))
        return results
    }
}