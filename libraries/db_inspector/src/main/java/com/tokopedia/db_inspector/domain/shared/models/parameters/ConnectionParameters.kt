package com.tokopedia.db_inspector.domain.shared.models.parameters

import com.tokopedia.db_inspector.domain.shared.base.BaseParameters

internal data class ConnectionParameters(
    val databasePath: String
) : BaseParameters