package com.tokopedia.data_explorer.db_explorer.data.sources.raw

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.Operation
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

internal class DatabaseSourceImpl @Inject constructor(
    @ApplicationContext val context: Context
) : Sources.Raw {
    override suspend fun getDatabases(operation: Operation): List<File> {
        //val allowedExtension = listOf("sql", "db")
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
        } else throw IOException("Deletion cannot be performed")
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
        else throw IOException("Copy operation cannot be performed")
    }

    //    override suspend fun renameDatabase(operation: Operation): List<File> {}


    private fun getFiles(filesDir: File): List<File> {
        val files: MutableList<File> = ArrayList()
        findFiles(filesDir, files)
        return files
    }

    private fun findFiles(file: File, fileList: MutableList<File>) {
        if (file.isFile && file.canRead()) {
            fileList.add(file)
        } else if (file.isDirectory) {
            for (fileInDir in file.listFiles().orEmpty()) {
                findFiles(fileInDir, fileList)
            }
        }
    }
}