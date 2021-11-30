package com.tokopedia.data_explorer.domain.databases.control.converter

import com.tokopedia.data_explorer.domain.Converters
import com.tokopedia.data_explorer.domain.databases.models.Operation
import com.tokopedia.data_explorer.domain.shared.models.parameters.DatabaseParameters

internal class DatabaseConverter: Converters.Database {
    override suspend fun get(parameters: DatabaseParameters.Get): Operation =
        Operation(argument = parameters.argument)
}