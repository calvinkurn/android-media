package com.tokopedia.db_inspector.domain.shared.models

import com.tokopedia.db_inspector.domain.shared.models.parameters.pragma

internal object Statements {

    object Pragma {

        fun userVersion() =
            pragma {
                name("user_version")
            }
    }
}