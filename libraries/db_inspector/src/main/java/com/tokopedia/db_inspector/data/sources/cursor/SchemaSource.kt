package com.tokopedia.db_inspector.data.sources.cursor

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.data.models.cursor.input.Query
import com.tokopedia.db_inspector.data.models.cursor.output.QueryResult
import com.tokopedia.db_inspector.data.sources.cursor.shared.CursorSource

internal class SchemaSource: Sources.Local.Schema, CursorSource() {

    override suspend fun getTables(query: Query): QueryResult = collectRows(query)

}