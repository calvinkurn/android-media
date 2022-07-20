package com.tokopedia.data_explorer.db_explorer.domain.connection

import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.connection.models.DatabaseConnection
import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ConnectionParameters
import javax.inject.Inject

internal class ConnectionRepository @Inject constructor(
    val sources: Sources.Memory,
    private val control: Control.Connection
): Repositories.Connection {
    override suspend fun open(input: ConnectionParameters): DatabaseConnection =
        control.mapper(sources.openConnection(control.converter(input)))

    override suspend fun close(input: ConnectionParameters) {
        sources.closeConnection(control.converter(input))
    }
}