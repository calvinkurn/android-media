package com.tokopedia.data_explorer.db_explorer.domain.schema.control.converter

import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Query
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ContentParameters

internal class ContentConverter: Converters.Content {
    override suspend fun invoke(parameters: ContentParameters): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.connection?.database,
            statement = parameters.statement
        )
}