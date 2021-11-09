package com.tokopedia.db_inspector.domain.databases.control.mapper

import com.tokopedia.db_inspector.domain.Mappers
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
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