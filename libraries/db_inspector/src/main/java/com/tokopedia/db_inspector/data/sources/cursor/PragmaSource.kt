package com.tokopedia.db_inspector.data.sources.cursor

import android.database.Cursor
import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.data.models.cursor.input.Query
import com.tokopedia.db_inspector.data.models.cursor.output.Field
import com.tokopedia.db_inspector.data.models.cursor.output.FieldType
import com.tokopedia.db_inspector.data.models.cursor.output.QueryResult
import com.tokopedia.db_inspector.data.models.cursor.output.Row
import timber.log.Timber

internal class PragmaSource : Sources.Pragma {
    override suspend fun getUserVersion(query: Query): QueryResult {
        if (query.database?.isOpen == true) {
            runQuery(query)?.use { cursor ->
                val result = when(cursor.moveToFirst()) {
                    true -> cursor.getString(0)
                    false -> ""
                }
                return QueryResult(
                    rows = listOf(
                        Row(
                            position = 0,
                            fields = listOf(
                                Field(
                                    type = FieldType.STRING,
                                    text = result
                                )
                            )
                        )
                    )
                )
            } ?: throw IllegalStateException("Cannot obtain a raw query cursor.")
        } else throw IllegalStateException("Cannot perform a query using a closed database connection.")
        return QueryResult(listOf())
    }

    internal fun runQuery(query: Query): Cursor? =
        query.database?.rawQuery(
            query.statement.also {
                Timber.i(it)
            },
            null
        )

}