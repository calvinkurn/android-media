package com.tokopedia.data_explorer.db_explorer.domain.shared.models

import com.tokopedia.data_explorer.db_explorer.data.Data.Constants.Limits.PAGE_SIZE
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Order
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
        fun tables(query: String?): String {
            val searchParam: String =  query?.let { "AND name like \"%$it%\""} ?: ""
            return "SELECT name FROM sqlite_master WHERE type = 'table' $searchParam ORDER BY name"
        }

        fun count(name: String?, whereClause: String = "") =
            "SELECT COUNT(*) FROM $name $whereClause".trim()

        fun searchByCol(query: String, columnName: String) =
            "WHERE $columnName like \"%$query%\""

        fun table(name: String?, orderByColumns: String?, sort: Order, page: Int?, whereClause: String = ""): String {
            val orderBySql = if (orderByColumns.isNullOrEmpty()) {
                ""
            } else {
                "ORDER BY $orderByColumns ${sort.rawValue}"
            }

            val offsetString = page?.let { "${(it - 1) * PAGE_SIZE}," }
            val limit = "LIMIT ${offsetString ?: ""}$PAGE_SIZE"

            return "SELECT * FROM $name $whereClause $orderBySql $limit"
        }

        fun dropContent(name: String?) = "DELETE from $name"
    }
}