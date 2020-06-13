package com.tkpd.remoteresourcerequest.utils

import android.content.Context
import androidx.annotation.RawRes
import com.tkpd.remoteresourcerequest.R
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.type.RequestedResourceType
import com.tkpd.remoteresourcerequest.utils.JsonArrayListHelper.Companion.getResourceNameList
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class DeferredWorkerHelper(val context: Context) {

    private var resourceDB: ResourceDB = ResourceDB.getDatabase(context)

    internal fun getPendingDeferredResourceURLs(
        @RawRes resourceId: Int
    ): List<RequestedResourceType> {
        val pendingList = arrayListOf<RequestedResourceType>()
        val downloadedList = resourceDB.resourceEntryDao.getAllDownloadedResourceURLEntry()
        val rawResList = getDeferredResourceFromFile(resourceId, getResourceNameList())
        rawResList.forEach {
            if (!downloadedList.contains(it.remoteFileName))
                pendingList.add(it)
        }
        return pendingList
    }

    internal fun checkAppVersionAndManageDB(appVersion: String) {
        val existingAppVersion = resourceDB.resourceEntryDao.getAppVersion() ?: return
        if (appVersion != existingAppVersion) {
            resourceDB.resourceEntryDao.deleteEntries()
        }
    }

    @Throws(JSONException::class)
    private fun getDeferredResourceFromFile(
        @RawRes resourceId: Int,
        diffFileList: ArrayList<String>
    ): ArrayList<RequestedResourceType> {
        val resUrl = arrayListOf<RequestedResourceType>()
        val json: String?
        try {
            val inputStream: InputStream = context.resources.openRawResource(resourceId)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
            val jsonObject = JSONObject(json)
            for (arrayName in diffFileList) {
                try {
                    val tempList = jsonObject.getJSONArray(arrayName)
                    for (element in 0 until tempList.length()) {
                        val url = tempList[element] as String
                        val fileTypeObject =
                            JsonArrayListHelper.getResourceTypeObject(arrayName, url)
                        resUrl.add(fileTypeObject)
                    }
                } catch (exception: JSONException) {
                    // this requested key doesn't exist in JSON file so throwing exception.
                    throw JSONException(
                        context.getString(R.string.msg_json_exception).format(arrayName)
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return resUrl
    }

}
