package com.tkpd.remoteresourcerequest.task

import android.content.Context
import androidx.annotation.RawRes
import com.tkpd.remoteresourcerequest.database.ResourceDB
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class DeferredWorkerHelper(
        val context: Context,
        private val resourceDownloadManager: ResourceDownloadManager
) {
    private var resourceDB: ResourceDB = ResourceDB.getDatabase(context)

    internal fun getPendingDeferredResourceURLs(
            @RawRes resourceId: Int
    ): List<String> {
        val pendingList = arrayListOf<String>()
        val downloadedList = resourceDB.resourceEntryDao.getAllDownloadedResourceURLEntry()
        val rawResList = getDeferredResourceFromFile(resourceId)
        rawResList.forEach {
            if (!downloadedList.contains(resourceDownloadManager.getResourceUrl(it)))
                pendingList.add(it)
        }
        return pendingList
    }

    private fun getDeferredResourceFromFile(@RawRes resourceId: Int): ArrayList<String> {
        val resUrl = arrayListOf<String>()
        val json: String?
        try {
            val inputStream: InputStream = context.resources.openRawResource(resourceId)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
            val jsonObject = JSONObject(json)
            val multiDpi = jsonObject.getJSONArray("multiDpi")
            for (element in 0 until multiDpi.length()) {
                val url = multiDpi[element] as String
                resUrl.add(url)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return resUrl
    }

}