package com.tokopedia.data_explorer.db_explorer.domain.connection.control.mappers

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.data_explorer.db_explorer.domain.Mappers
import com.tokopedia.data_explorer.db_explorer.domain.connection.models.DatabaseConnection

internal class ConnectionMapper: Mappers.Connection {
    override suspend fun invoke(model: SQLiteDatabase): DatabaseConnection =
       DatabaseConnection(model)
}