package com.tokopedia.data_explorer.db_explorer.domain.databases.control

import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.Mappers

internal data class DatabaseControl(
    override val mapper: Mappers.Database,
    override val converter: Converters.Database
) : Control.Database