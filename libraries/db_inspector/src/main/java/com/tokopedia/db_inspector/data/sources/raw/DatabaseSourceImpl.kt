package com.tokopedia.db_inspector.data.sources.raw

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.domain.databases.models.Operation
import java.io.File
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

//    override suspend fun removeDatabase(operation: Operation): List<File> {}
//    override suspend fun renameDatabase(operation: Operation): List<File> {}
//    override suspend fun copyDatabase(operation: Operation): List<File> {}

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