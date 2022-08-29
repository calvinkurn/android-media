package com.tokopedia.data_explorer.db_explorer.domain.databases.usecases

import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.DatabaseParameters
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

internal class CopyDatabasesUseCase @Inject constructor(
    private val databaseRepository: Repositories.Database
) : UseCase<List<DatabaseDescriptor>>() {

    private lateinit var input: DatabaseParameters.Command

    fun copyDatabases(
        onSuccess: (List<DatabaseDescriptor>) -> Unit,
        onError: (Throwable) -> Unit,
        input: DatabaseParameters.Command
    ) {
        this.input = input
        this.execute({
            onSuccess(it)
        }, {
            onError(it)
        })
    }

    override suspend fun executeOnBackground(): List<DatabaseDescriptor> =
        databaseRepository.copy(input)

}