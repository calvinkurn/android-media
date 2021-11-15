package com.tokopedia.db_inspector.domain.schema.control.converter

import com.tokopedia.db_inspector.data.models.cursor.input.Query
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.shared.models.parameters.ContentParameters

internal class ContentConverter: Converters.Content {
    override suspend fun invoke(parameters: ContentParameters): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.connection?.database,
            statement = parameters.statement
        )
}