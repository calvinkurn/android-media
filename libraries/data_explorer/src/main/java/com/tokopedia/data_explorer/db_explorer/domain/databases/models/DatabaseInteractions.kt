package com.tokopedia.data_explorer.db_explorer.domain.databases.models

internal data class DatabaseInteractions(
    val onDelete: (DatabaseDescriptor) -> Unit,
    val onCopy: (DatabaseDescriptor) -> Unit,
    val onShare: (DatabaseDescriptor) -> Unit,
)