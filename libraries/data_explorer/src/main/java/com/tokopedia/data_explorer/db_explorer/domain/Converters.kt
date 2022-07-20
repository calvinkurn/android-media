package com.tokopedia.data_explorer.db_explorer.domain

import com.tokopedia.data_explorer.db_explorer.domain.databases.models.Operation
import com.tokopedia.data_explorer.db_explorer.domain.shared.base.BaseConverter
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ContentParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.DatabaseParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.PragmaParameters
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Query as DbQuery

internal interface Converters {

    interface Database : BaseConverter<DatabaseParameters, Operation> {

        suspend infix fun get(parameters: DatabaseParameters.Get): Operation
        suspend infix fun command(parameters: DatabaseParameters.Command): Operation
    }

    interface Connection : BaseConverter<ConnectionParameters, String>

    interface Content : BaseConverter<ContentParameters, DbQuery>


    interface Pragma : BaseConverter<PragmaParameters, DbQuery> {

        suspend infix fun version(parameters: PragmaParameters.Version): DbQuery
        suspend infix fun pragma(parameters: PragmaParameters.Pragma): DbQuery

    }


}
