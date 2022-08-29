package com.tokopedia.data_explorer.db_explorer.domain.connection.models

import android.database.sqlite.SQLiteDatabase

internal data class DatabaseConnection(
    val database: SQLiteDatabase
)