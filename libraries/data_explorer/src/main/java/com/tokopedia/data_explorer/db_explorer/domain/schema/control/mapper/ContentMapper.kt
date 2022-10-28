package com.tokopedia.data_explorer.db_explorer.domain.schema.control.mapper

import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.QueryResult
import com.tokopedia.data_explorer.db_explorer.domain.Mappers
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page

internal class ContentMapper: Mappers.Content {
    override suspend fun invoke(model: QueryResult): Page =
        Page(
            cells = model.rows.map { row ->
                row.fields.toList().map { field -> Cell(field.text) }
            }.flatten()
        )
}