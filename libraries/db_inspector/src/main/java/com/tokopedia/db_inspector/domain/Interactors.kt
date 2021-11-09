package com.tokopedia.db_inspector.domain

import com.tokopedia.db_inspector.domain.databases.models.Operation
import com.tokopedia.db_inspector.domain.shared.base.BaseInteractor
import java.io.File

internal interface Interactors {

    interface GetDatabases : BaseInteractor<Operation, List<File>>

}