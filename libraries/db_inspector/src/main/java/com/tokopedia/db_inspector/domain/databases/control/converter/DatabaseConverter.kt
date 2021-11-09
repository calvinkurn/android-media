package com.tokopedia.db_inspector.domain.databases.control.converter

import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.databases.models.Operation
import com.tokopedia.db_inspector.domain.shared.models.parameters.DatabaseParameters

internal class DatabaseConverter: Converters.Database {
    override suspend fun get(parameters: DatabaseParameters.Get): Operation =
        Operation(argument = parameters.argument)
}