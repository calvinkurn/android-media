package com.tokopedia.data_explorer.db_explorer.domain.pragma.control.converters

import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Query
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.PragmaParameters

internal class PragmaConverter: Converters.Pragma {

    override suspend fun invoke(parameters: PragmaParameters): Query {
        return super.invoke(parameters)
    }

    override suspend fun version(parameters: PragmaParameters.Version): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.connection?.database,
            statement = parameters.statement
        )

    override suspend fun pragma(parameters: PragmaParameters.Pragma): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.connection?.database,
            statement = parameters.statement,
            pageSize = parameters.pageSize,
            page = parameters.page
        )

}