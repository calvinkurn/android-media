package com.tokopedia.db_inspector.domain.schema.control.mapper

import com.tokopedia.db_inspector.data.models.cursor.output.QueryResult
import com.tokopedia.db_inspector.domain.Mappers
import com.tokopedia.db_inspector.domain.shared.models.Cell
import com.tokopedia.db_inspector.domain.shared.models.Page

internal class ContentMapper: Mappers.Content {
    override suspend fun invoke(model: QueryResult): Page =
        Page(
            cells = model.rows.map { row ->
                row.fields.toList().map { field -> Cell(field.text) }
            }.flatten()
        )
}