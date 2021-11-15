package com.tokopedia.db_inspector.domain.schema.usecases

import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.shared.models.Page
import com.tokopedia.db_inspector.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.db_inspector.domain.shared.models.parameters.ContentParameters
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

internal class GetTablesUseCase @Inject constructor(
    private val schemaRepository: Repositories.Schema,
    private val connectionRepository: Repositories.Connection,
): UseCase<Page>() {

    fun getTables(onSuccess: (Page) -> Unit, onError: (Throwable) -> Unit, contentParameters: ContentParameters) {
        this.useCaseRequestParams = RequestParams().apply {
            putObject("input", contentParameters)
        }
        this.execute({
            onSuccess(it)
        }, {
            onError(it)
        }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): Page {
        val input = useCaseRequestParams.getObject("input") as ContentParameters
        val connection = connectionRepository.open(ConnectionParameters(databasePath = input.databasePath))

        val results = schemaRepository.getPage(input.copy(connection = connection))
        connectionRepository.close(ConnectionParameters(databasePath = input.databasePath))
        return results
    }
}