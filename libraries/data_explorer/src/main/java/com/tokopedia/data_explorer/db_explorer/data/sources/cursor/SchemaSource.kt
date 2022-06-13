package com.tokopedia.data_explorer.db_explorer.data.sources.cursor

import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Query
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.QueryResult
import com.tokopedia.data_explorer.db_explorer.data.sources.cursor.shared.CursorSource

internal class SchemaSource: Sources.Local.Schema, CursorSource() {

    override suspend fun getTables(query: Query): QueryResult = collectRows(query)

    override suspend fun getTableByName(query: Query): QueryResult = collectRows(query)

    override suspend fun dropTableContentByName(query: Query): QueryResult = collectRows(query)
}