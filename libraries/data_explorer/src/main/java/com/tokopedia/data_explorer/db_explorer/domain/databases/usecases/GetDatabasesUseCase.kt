package com.tokopedia.data_explorer.db_explorer.domain.databases.usecases

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Statements
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.DatabaseParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.PragmaParameters
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

internal class GetDatabasesUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val databaseRepository: Repositories.Database,
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
): UseCase<List<DatabaseDescriptor>>() {

    private lateinit var input: DatabaseParameters.Get

    fun getDatabases(onSuccess: (List<DatabaseDescriptor>) -> Unit, onError: (Throwable) -> Unit, input: DatabaseParameters.Get) {
        this.input = input
        this.execute({
            onSuccess(it)
        }, {
            onError(it)
        })
    }

    override suspend fun executeOnBackground(): List<DatabaseDescriptor> {
        val allowedDbList: ArrayList<String> = arrayListOf()
        val array = context.resources.getStringArray(R.array.data_explorer_white_listed_database)
        allowedDbList.addAll(array)
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
                val isDeletable = allowedDbList.contains(it.name)
                connectionRepository.close(ConnectionParameters(databasePath = it.absolutePath))
                it.copy(version = version, isDeletable = isDeletable)
            }

    }

}