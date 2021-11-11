package com.tokopedia.db_inspector.domain.databases.usecases

import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.db_inspector.domain.shared.models.Statements
import com.tokopedia.db_inspector.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.db_inspector.domain.shared.models.parameters.DatabaseParameters
import com.tokopedia.db_inspector.domain.shared.models.parameters.PragmaParameters
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

internal class GetDatabasesUseCase @Inject constructor(
    private val databaseRepository: Repositories.Database,
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
): UseCase<List<DatabaseDescriptor>>() {

    fun getDatabases(onSuccess: (List<DatabaseDescriptor>) -> Unit, onError: (Throwable) -> Unit, input: DatabaseParameters.Get) {
        this.useCaseRequestParams = RequestParams().apply {
            putObject("input", input)
        }
        this.execute({
            onSuccess(it)
        }, {
            onError(it)
        }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): List<DatabaseDescriptor> {
        val input = useCaseRequestParams.getObject("input") as DatabaseParameters.Get
        return databaseRepository.getPage(input)
            .filter { databaseDescriptor -> input.argument?.let { databaseDescriptor.name.contains(it) } ?: true }
            .map {
                val connection = connectionRepository.open(ConnectionParameters(databasePath = it.absolutePath))
                val version = pragmaRepository.getUserVersion(
                    PragmaParameters.Version(
                        databasePath = it.absolutePath,
                        connection = connection,
                        statement = Statements.Pragma.userVersion()
                    )
                ).cells.firstOrNull()?.text.orEmpty()
                connectionRepository.close(ConnectionParameters(databasePath = it.absolutePath))

                it.copy(version = version)
            }

    }

}