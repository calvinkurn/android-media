package com.tokopedia.db_inspector.domain.shared.models

import com.tokopedia.db_inspector.domain.shared.models.parameters.pragma

internal object Statements {

    object Pragma {

        fun userVersion() =
            pragma {
                name("user_version")
            }
        fun tableInfo(name: String) =
            pragma {
                name("table_info")
                value(name)
            }
    }

    object Schema {
        fun tables(query: String?) =
            "SELECT name FROM sqlite_master WHERE type = 'table'"
    }
}