package com.tokopedia.data_explorer.db_explorer.data.sources.cursor.shared

import android.database.Cursor
import android.util.Log
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Query
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.Field
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.FieldType
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.QueryResult
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.Row

internal open class CursorSource {

    fun collectRows(query: Query): QueryResult =
        if (query.database?.isOpen == true) {
            runQuery(query)?.use { cursor ->
                cursor.moveToFirst()
                val rows = iterateRowsInTable(cursor)
                QueryResult(
                    rows = rows
                )
            } ?: throw IllegalStateException("Cannot obtain a raw query cursor.")
        } else throw IllegalStateException("Cannot perform a query using a closed database connection.")

    internal fun runQuery(query: Query): Cursor? =
        query.database?.rawQuery(
            query.statement.also {
                Log.i("DB_QUERY", it)
            },
            null
        )

    private fun iterateRowsInTable(cursor: Cursor): List<Row> =
        (0 until cursor.count).map { row ->
            Row(
                position = row,
                fields = iterateFieldsInRow(cursor)
            ).also { cursor.moveToNext() }
        }


    private fun iterateFieldsInRow(cursor: Cursor): List<Field> =
        (0 until cursor.columnCount).map { column ->
            when (val type = FieldType(cursor.getType(column))) {
                FieldType.NULL -> Field(
                    type = type,
                    text = FieldType.NULL.name.lowercase()
                )
                FieldType.STRING -> Field(
                    type = type,
                    text = cursor.getString(column) ?: FieldType.NULL.name.lowercase()
                )
                FieldType.INTEGER -> Field(
                    type = type,
                    text = cursor.getLong(column).toString()
                )
                FieldType.FLOAT -> Field(
                    type = type,
                    text = cursor.getFloat(column).toString()
                )
            }
        }

}