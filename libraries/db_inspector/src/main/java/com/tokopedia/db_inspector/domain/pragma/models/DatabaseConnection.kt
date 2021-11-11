package com.tokopedia.db_inspector.domain.pragma.models

import android.database.sqlite.SQLiteDatabase

internal data class DatabaseConnection(
    val database: SQLiteDatabase
)