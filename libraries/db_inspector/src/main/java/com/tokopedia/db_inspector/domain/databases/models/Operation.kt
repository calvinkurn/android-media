package com.tokopedia.db_inspector.domain.databases.models

internal data class Operation(
    val databaseDescriptor: DatabaseDescriptor? = null,
    val argument: String? = null
)
