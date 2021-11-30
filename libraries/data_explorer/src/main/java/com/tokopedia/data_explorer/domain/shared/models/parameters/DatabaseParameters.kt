package com.tokopedia.data_explorer.domain.shared.models.parameters

import com.tokopedia.data_explorer.domain.shared.base.BaseParameters

internal sealed class DatabaseParameters: BaseParameters {
    data class Get(val argument: String?) : DatabaseParameters()
}