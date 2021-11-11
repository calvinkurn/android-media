package com.tokopedia.db_inspector.domain.connection.control.mappers

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.db_inspector.domain.Mappers
import com.tokopedia.db_inspector.domain.connection.models.DatabaseConnection

internal class ConnectionMapper: Mappers.Connection {
    override suspend fun invoke(model: SQLiteDatabase): DatabaseConnection =
       DatabaseConnection(model)
}