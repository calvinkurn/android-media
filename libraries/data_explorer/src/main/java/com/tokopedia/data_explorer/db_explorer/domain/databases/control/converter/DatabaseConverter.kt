package com.tokopedia.data_explorer.db_explorer.domain.databases.control.converter

import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.Operation
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.DatabaseParameters

internal class DatabaseConverter: Converters.Database {
    override suspend fun get(parameters: DatabaseParameters.Get): Operation =
        Operation(argument = parameters.argument)

    override suspend fun command(parameters: DatabaseParameters.Command): Operation =
        Operation(databaseDescriptor = parameters.databaseDescriptor)
}