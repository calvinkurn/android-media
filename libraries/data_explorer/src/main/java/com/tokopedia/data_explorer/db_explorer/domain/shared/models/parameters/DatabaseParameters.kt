package com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters

import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.shared.base.BaseParameters

internal sealed class DatabaseParameters: BaseParameters {

    data class Get(val argument: String?) : DatabaseParameters()

    data class Command(val databaseDescriptor: DatabaseDescriptor) : DatabaseParameters()
}