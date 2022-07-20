package com.tokopedia.data_explorer.db_explorer.domain.shared.models

internal data class DataBaseController(
    val databaseName: String = "",
    val databasePath: String = "",
    val schemaName: String = ""
) {
    private val hasDatabaseData = databaseName.isBlank().not() && databasePath.isBlank().not()

    val hasSchemaData = hasDatabaseData && schemaName.isBlank().not()
}