package com.tokopedia.db_inspector.domain.shared.models.parameters

import com.tokopedia.db_inspector.domain.connection.models.DatabaseConnection
import com.tokopedia.db_inspector.domain.shared.base.BaseParameters

internal data class ContentParameters(
    val databasePath: String = "",
    val connection: DatabaseConnection? = null,
    val statement: String,
): BaseParameters