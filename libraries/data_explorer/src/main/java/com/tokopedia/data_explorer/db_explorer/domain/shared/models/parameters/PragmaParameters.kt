package com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters

import com.tokopedia.data_explorer.db_explorer.domain.connection.models.DatabaseConnection
import com.tokopedia.data_explorer.db_explorer.domain.shared.base.BaseParameters

internal sealed class PragmaParameters : BaseParameters {

    data class Version(
        val databasePath: String,
        val connection: DatabaseConnection? = null,
        val statement: String
    ): PragmaParameters()

    data class Pragma(
        val databasePath: String,
        val connection: DatabaseConnection? = null,
        val statement: String,
        var page: Int? = 1,
        val pageSize: Int = 100
    ) : PragmaParameters()

}