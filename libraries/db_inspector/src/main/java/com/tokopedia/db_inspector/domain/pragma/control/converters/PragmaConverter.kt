package com.tokopedia.db_inspector.domain.pragma.control.converters

import com.tokopedia.db_inspector.data.models.cursor.input.Query
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.shared.models.parameters.PragmaParameters

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
}