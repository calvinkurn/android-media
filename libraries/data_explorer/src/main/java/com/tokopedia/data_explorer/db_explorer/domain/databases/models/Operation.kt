package com.tokopedia.data_explorer.db_explorer.domain.databases.models

internal data class Operation(
    val databaseDescriptor: DatabaseDescriptor? = null,
    val argument: String? = null
)

internal data class DatabaseDescriptor(
    val exists: Boolean,
    val parentPath: String,
    val name: String,
    val extension: String = "",
    val version: String = "",
    val isDeletable: Boolean = false
) {
    val absolutePath: String
        get() = if (extension.isEmpty()) "$parentPath/$name" else "$parentPath/$name.$extension"
}

