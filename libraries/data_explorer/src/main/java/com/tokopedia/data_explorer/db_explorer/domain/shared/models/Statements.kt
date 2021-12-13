package com.tokopedia.data_explorer.db_explorer.domain.shared.models

import com.tokopedia.data_explorer.db_explorer.data.Data.Constants.Limits.PAGE_SIZE
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.pragma

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

        fun count(name: String?) =
            "SELECT COUNT(*) FROM $name"

        fun table(name: String?, page: Int?) : String {
            val offsetString = page?.let {
                "${(it-1)*PAGE_SIZE},"
            }
            return "SELECT * FROM $name LIMIT ${offsetString ?: ""}$PAGE_SIZE"
        }
    }
}