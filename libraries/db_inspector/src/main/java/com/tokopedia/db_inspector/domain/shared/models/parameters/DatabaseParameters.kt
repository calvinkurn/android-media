package com.tokopedia.db_inspector.domain.shared.models.parameters

import com.tokopedia.db_inspector.domain.shared.base.BaseParameters

internal sealed class DatabaseParameters: BaseParameters {
    data class Get(val argument: String?) : DatabaseParameters()
}