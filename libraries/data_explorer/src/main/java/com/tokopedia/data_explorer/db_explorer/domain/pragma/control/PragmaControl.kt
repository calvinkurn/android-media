package com.tokopedia.data_explorer.db_explorer.domain.pragma.control

import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.Mappers

internal class PragmaControl(
    override val mapper: Mappers.Pragma,
    override val converter: Converters.Pragma
) : Control.Pragma