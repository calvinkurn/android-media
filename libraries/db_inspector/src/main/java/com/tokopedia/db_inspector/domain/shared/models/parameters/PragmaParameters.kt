package com.tokopedia.db_inspector.domain.shared.models.parameters

import com.tokopedia.db_inspector.domain.connection.models.DatabaseConnection
import com.tokopedia.db_inspector.domain.shared.base.BaseParameters

internal sealed class PragmaParameters : BaseParameters {

    data class Version(
        val databasePath: String,
        val connection: DatabaseConnection? = null,
        val statement: String
    ): PragmaParameters()


}