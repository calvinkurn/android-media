package com.tokopedia.data_explorer.domain.shared.models

internal data class DataBaseController(
    val databaseName: String? = null,
    val databasePath: String? = null,
    val schemaName: String? = null
) {
    private val hasDatabaseData = databaseName.isNullOrBlank().not() && databasePath.isNullOrBlank().not()

    val hasSchemaData = hasDatabaseData && schemaName.isNullOrBlank().not()
}