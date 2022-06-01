package com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters

import com.tokopedia.data_explorer.db_explorer.domain.shared.base.BaseParameters

internal data class ConnectionParameters(
    val databasePath: String
) : BaseParameters