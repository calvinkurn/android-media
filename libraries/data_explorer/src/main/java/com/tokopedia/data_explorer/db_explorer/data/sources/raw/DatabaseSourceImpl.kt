package com.tokopedia.data_explorer.db_explorer.data.sources.raw

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.Operation
import com.tokopedia.data_explorer.db_explorer.presentation.Constants
import java.io.File
import java.io.IOException
import javax.inject.Inject

internal class DatabaseSourceImpl @Inject constructor(
    @ApplicationContext val context: Context
) : Sources.Raw {

    // allowedExtension = listOf("sql", "db")
    override suspend fun getDatabases(operation: Operation): List<File> {
        val ignoredExtensions = listOf("-shm", "-wal", "-journal")
        val databases: MutableSet<File> = context.databaseList()
            .filter { name -> ignoredExtensions.none { extension -> name.endsWith(extension) } }
            .map { path -> context.getDatabasePath(path) }
            .toMutableSet()
        return databases.toList()
    }

    override suspend fun removeDatabase(operation: Operation): List<File> {
        val isDeleteSuccessful = context.deleteDatabase("${operation.databaseDescriptor?.name.orEmpty()}." +
                operation.databaseDescriptor?.extension.orEmpty())
        if (isDeleteSuccessful) {
            return listOf(File(operation.databaseDescriptor?.absolutePath.orEmpty()))
        } else throw IOException(Constants.ErrorMessages.DELETION_ERROR)
    }
    override suspend fun copyDatabase(operation: Operation): List<File> {
        var counter = 1
        var fileName = "${operation.databaseDescriptor?.parentPath.orEmpty()}/" +
                "${operation.databaseDescriptor?.name.orEmpty()}_$counter." +
                operation.databaseDescriptor?.extension.orEmpty()
        var targetFile = File(fileName)
        while (targetFile.exists()) {
            fileName = "${operation.databaseDescriptor?.parentPath.orEmpty()}/" +
                    "${operation.databaseDescriptor?.name.orEmpty()}_$counter." +
                    operation.databaseDescriptor?.extension.orEmpty()

            targetFile = File(fileName)
            counter++
        }

        val newFile = File(operation.databaseDescriptor?.absolutePath.orEmpty())
            .copyTo(target = targetFile, overwrite = true)
        if (newFile.exists()) return listOf(newFile)
        else throw IOException(Constants.ErrorMessages.COPY_ERROR)
    }
}