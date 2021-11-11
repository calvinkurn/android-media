package com.tokopedia.db_inspector.domain

import com.tokopedia.db_inspector.domain.databases.models.Operation
import com.tokopedia.db_inspector.domain.shared.base.BaseConverter
import com.tokopedia.db_inspector.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.db_inspector.domain.shared.models.parameters.DatabaseParameters

internal interface Converters {

    interface Database : BaseConverter<DatabaseParameters, Operation> {

        suspend infix fun get(parameters: DatabaseParameters.Get): Operation
    }

    interface Connection : BaseConverter<ConnectionParameters, String>


}
