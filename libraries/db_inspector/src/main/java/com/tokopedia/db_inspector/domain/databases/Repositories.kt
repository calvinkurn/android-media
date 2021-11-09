package com.tokopedia.db_inspector.domain.databases

import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.db_inspector.domain.shared.base.BaseRepository
import com.tokopedia.db_inspector.domain.shared.models.parameters.DatabaseParameters

internal interface Repositories {

    interface Database: BaseRepository<DatabaseParameters.Get, List<DatabaseDescriptor>> {

    }
}