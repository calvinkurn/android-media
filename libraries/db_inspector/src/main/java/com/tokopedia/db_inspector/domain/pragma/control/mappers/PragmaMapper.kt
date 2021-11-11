package com.tokopedia.db_inspector.domain.pragma.control.mappers

import com.tokopedia.db_inspector.data.models.cursor.output.QueryResult
import com.tokopedia.db_inspector.domain.Mappers
import com.tokopedia.db_inspector.domain.shared.models.Cell
import com.tokopedia.db_inspector.domain.shared.models.Page

internal class PragmaMapper : Mappers.Pragma {
    override suspend fun invoke(model: QueryResult): Page =
        Page(
            cells = model.rows.map { row ->
                row.fields.map { field ->
                    Cell(field.text)
                }
            }.flatten(),
            nextPage = model.nextPage
        )
}