package com.tokopedia.db_inspector.domain.connection.models

import android.database.sqlite.SQLiteDatabase

internal data class DatabaseConnection(
    val database: SQLiteDatabase
)