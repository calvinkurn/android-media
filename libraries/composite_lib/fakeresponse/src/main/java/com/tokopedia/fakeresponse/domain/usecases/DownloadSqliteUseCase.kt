package com.tokopedia.fakeresponse.domain.usecases

import android.content.Context
import android.os.Build
import com.tokopedia.fakeresponse.App
import com.tokopedia.fakeresponse.FileUtil
import com.tokopedia.fakeresponse.domain.repository.RemoteSqliteRepository

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

            val abi2 = Build.CPU_ABI
            val v = RemoteSqliteRepository.AbiEndpoints.map[abi2]
            if (v.isNullOrEmpty()) {
                return arrayOf(Build.CPU_ABI)
            }
            return arrayOf(abi2)
        }
        return Build.SUPPORTED_ABIS
    }

}