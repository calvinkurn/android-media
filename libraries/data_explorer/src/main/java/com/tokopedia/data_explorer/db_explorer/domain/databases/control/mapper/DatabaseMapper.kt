package com.tokopedia.data_explorer.db_explorer.domain.databases.control.mapper

import com.tokopedia.data_explorer.db_explorer.domain.Mappers
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import java.io.File

internal class DatabaseMapper: Mappers.Database {
    override suspend fun invoke(model: File): DatabaseDescriptor =
        DatabaseDescriptor(
            exists = model.exists(),
            parentPath = model.parentFile?.absolutePath.orEmpty(),
            name = model.nameWithoutExtension,
            extension = model.extension
        )
}