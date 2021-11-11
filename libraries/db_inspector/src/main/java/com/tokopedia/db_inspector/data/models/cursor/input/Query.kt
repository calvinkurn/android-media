package com.tokopedia.db_inspector.data.models.cursor.input

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.db_inspector.data.Data

internal data class Query(
    val databasePath: String = "",
    val database: SQLiteDatabase? = null,
    val statement: String,
    val order: Order = Order.ASCENDING,
    val pageSize: Int = Data.Constants.Limits.PAGE_SIZE,
    val page: Int? = Data.Constants.Limits.INITIAL_PAGE
)