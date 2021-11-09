package com.tokopedia.db_inspector.domain

import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.db_inspector.domain.shared.base.BaseMapper
import java.io.File

internal interface Mappers {

    interface Database : BaseMapper<File, DatabaseDescriptor>
}
