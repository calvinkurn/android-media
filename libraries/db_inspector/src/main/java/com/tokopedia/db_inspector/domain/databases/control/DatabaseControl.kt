package com.tokopedia.db_inspector.domain.databases.control

import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.Mappers

internal data class DatabaseControl(
    override val mapper: Mappers.Database,
    override val converter: Converters.Database
) : Control.Database