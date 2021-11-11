package com.tokopedia.db_inspector.domain.pragma.control

import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.Mappers

internal class PragmaControl(
    override val mapper: Mappers.Pragma,
    override val converter: Converters.Pragma
) : Control.Pragma