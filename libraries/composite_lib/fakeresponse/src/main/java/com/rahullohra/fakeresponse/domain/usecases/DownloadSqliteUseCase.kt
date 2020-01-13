package com.rahullohra.fakeresponse.domain.usecases

import android.content.Context
import android.os.Build
import com.rahullohra.fakeresponse.App
import com.rahullohra.fakeresponse.FileUtil
import com.rahullohra.fakeresponse.domain.repository.RemoteSqliteRepository

class DownloadSqliteUseCase(val repository: RemoteSqliteRepository) :
    BaseUseCase<RemoteSqliteRepository>(repository) {

    suspend fun getSqlite() {
        val isFilePresent = checkForExsistingSqliteFile()
        if (!isFilePresent) {
            val cpuType = getCpu().first()
            val byteArray = repository.getSqlite(cpuType)
            writeSqliteResponseToFile(byteArray)
        }
    }

    fun checkForExsistingSqliteFile(): Boolean {
        val context = App.INSTANCE
        val dir = context.getDir(FileUtil.GQL_FOLDER, Context.MODE_PRIVATE)
        if (dir.exists()) {
            val file = dir.listFiles()?.find { it.name == FileUtil.SQLITE_FILE }
            return file != null
        }
        return false
    }

    fun deleteSqlite() {
        FileUtil.deleteSqlite(App.INSTANCE)
    }


    fun writeSqliteResponseToFile(byteArray: ByteArray) {
        FileUtil.writeSqliteFile(byteArray, App.INSTANCE)
    }

    fun getCpu(): Array<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return arrayOf(Build.CPU_ABI2)
        }
        return Build.SUPPORTED_ABIS
    }

}