package com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters

import com.tokopedia.data_explorer.db_explorer.domain.connection.models.DatabaseConnection
import com.tokopedia.data_explorer.db_explorer.domain.shared.base.BaseParameters

internal data class ContentParameters(
    val databasePath: String = "",
    val connection: DatabaseConnection? = null,
    val statement: String,
): BaseParameters