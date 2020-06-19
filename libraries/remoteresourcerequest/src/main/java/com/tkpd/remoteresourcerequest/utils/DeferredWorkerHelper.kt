package com.tkpd.remoteresourcerequest.utils

import android.content.Context
import androidx.annotation.RawRes
import com.tkpd.remoteresourcerequest.R
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.type.RequestedResourceType
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class DeferredWorkerHelper(val context: Context) {
    private var resourceDB: ResourceDB = ResourceDB.getDatabase(context)

    internal fun getPendingDeferredResourceURLs(
            @RawRes resourceId: Int
    ): List<RequestedResourceType> {
        val pendingList = arrayListOf<RequestedResourceType>()
        val downloadedList = resourceDB.resourceEntryDao.getAllResourceEntry()
        val rawResList = getDeferredResourceFromFile(resourceId)

        rawResList.forEach { type ->
            var shouldAdd = true
            for (entry in downloadedList) {
                if (entry.url.contains(type.remoteFileName)) {
                    shouldAdd = false

                    /**
                     * Need to check version saved in database for this [type]. If it is unmatched
                     * then we purge corresponding entry from database and let the download start
                     * from fresh. Also we need to delete db entries which are not used anywhere.
                     */
                    if (entry.appVersion != type.resourceVersion) {
                        resourceDB.resourceEntryDao.deleteEntry(type.remoteFileName)
                        shouldAdd = true
                    } else if (!type.isUsedAnywhere) {
                        resourceDB.resourceEntryDao.deleteEntry(type.remoteFileName)
                        shouldAdd = false
                    }
                    break
                } else shouldAdd = type.isUsedAnywhere
            }
            if (shouldAdd) {
                pendingList.add(type)

            }
        }
        return pendingList
    }

    internal fun getDeferredResourceFromFile(
            @RawRes resourceId: Int
    ): ArrayList<RequestedResourceType> {
        val resUrl = arrayListOf<RequestedResourceType>()

        var reader: BufferedReader? = null
        var inputStream: InputStream? = null
        try {
            inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)

            reader = BufferedReader(inputStreamReader)
            reader.readLine()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val singleCSVEntry = line
                if (!singleCSVEntry.isNullOrEmpty()) {
                    val fileTypeObject =
                            CSVArrayListHelper.getResourceTypeObject(singleCSVEntry)

                    resUrl.add(fileTypeObject)
                }

            }
        } catch (e: IOException) {
            throw IOException(
                    context.getString(R.string.rem_res_req_msg_unable_to_read_file)
            )
        } finally {
            inputStream?.close()
            reader?.close()
        }
        return resUrl
    }

}
