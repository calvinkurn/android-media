package com.tokopedia.data_explorer.domain.connection.control.converters

import com.tokopedia.data_explorer.domain.Converters
import com.tokopedia.data_explorer.domain.shared.models.parameters.ConnectionParameters

internal class ConnectionConverter: Converters.Connection {
    override suspend fun invoke(parameters: ConnectionParameters): String =
        parameters.databasePath
}