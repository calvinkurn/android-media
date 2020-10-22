package com.tokopedia.logger.viewer

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.*
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class LogcatActivityViewModel : ViewModel(), CoroutineScope {

    val liveData = MutableLiveData<List<LogcatInfo>?>()
    val saveToFileLiveData = MutableLiveData<String?>()
    private var job: Job = SupervisorJob()
    private var scope = CoroutineScope(job + Dispatchers.IO)
    private val childJobs = ArrayList<Job>()

    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext

    fun searchText(list: List<LogcatInfo>, text: String?) {
        clearActiveJobs()

        val childJob = scope.launch {
            try {
                if (!text.isNullOrEmpty()) {
                    val mLogData = mutableListOf<LogcatInfo>().apply {
                        addAll(list)
                    }

                    if (text.trim().isEmpty()) liveData.postValue(null)

                    val list = mLogData.filter { info ->
                        info.log?.contains(text) ?: false || info.tag?.contains(text) ?: false
                    }
                    if (list.isEmpty()) {
                        liveData.postValue(null)
                    } else {
                        liveData.postValue(list)
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                liveData.postValue(null)
            }
        }
        childJobs.add(childJob)
    }

    fun saveToFile(list: List<LogcatInfo>, packageName: String) {
        scope.launch {
            var writer: BufferedWriter? = null

            try {
                val data = mutableListOf<LogcatInfo>().apply {
                    addAll(list)
                }
                val directory = File(Environment.getExternalStorageDirectory(), "Logcat" + File.separator + packageName)
                if (!directory.isDirectory) {
                    directory.delete()
                }
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val file = File(directory, SimpleDateFormat("yyyyMMdd_kkmmss", Locale.getDefault()).format(Date()) + ".txt")
                if (!file.isFile) {
                    file.delete()
                }
                if (!file.exists()) {
                    file.createNewFile()
                }
                writer = BufferedWriter(OutputStreamWriter(FileOutputStream(file, false), Charset.forName("UTF-8")))
                for (info in data) {
                    writer.write("""
    ${info.toString().replace("\n", "\r\n")}
    
    
    """.trimIndent())
                }
                writer.flush()
                saveToFileLiveData.postValue("Successfully saved：" + file.path)
            } catch (ex: Exception) {
                Timber.e(ex)
                saveToFileLiveData.postValue(null)
            } finally {
                if (writer != null) {
                    try {
                        writer.close()
                    } catch (ignored: IOException) {
                    }
                }
            }
        }

    }

    private fun clearActiveJobs() {
        for (j in childJobs) {
            if (j.isActive) {
                j.cancel()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearActiveJobs()
    }
}