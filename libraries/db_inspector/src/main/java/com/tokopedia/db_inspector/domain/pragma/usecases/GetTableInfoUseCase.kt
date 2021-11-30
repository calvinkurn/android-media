package com.tokopedia.db_inspector.domain.pragma.usecases

import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.shared.models.Page
import com.tokopedia.db_inspector.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.db_inspector.domain.shared.models.parameters.PragmaParameters
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
        return tableInfo

    }
}