package com.tokopedia.db_inspector.domain.connection.control.converters

import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.shared.models.parameters.ConnectionParameters

internal class ConnectionConverter: Converters.Connection {
    override suspend fun invoke(parameters: ConnectionParameters): String =
        parameters.databasePath
}