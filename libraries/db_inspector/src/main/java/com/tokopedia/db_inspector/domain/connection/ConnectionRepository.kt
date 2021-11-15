package com.tokopedia.db_inspector.domain.connection

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.connection.models.DatabaseConnection
import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.shared.models.parameters.ConnectionParameters
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