package com.tokopedia.appdownloadmanager_common.domain.service

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

object GetDownloadVersionList {

    suspend inline fun <reified T> getApiResponse(apiUrl: String, typeToken: Type): T? {
        return withContext(Dispatchers.IO) {
            val urlConnection = URL(apiUrl).openConnection() as? HttpURLConnection
            val response = try {
                urlConnection?.useCaches = false
                val inputStream = urlConnection?.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                stringBuilder.toString()
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            } finally {
                urlConnection?.disconnect()
            }

            response?.let {
                Gson().fromJson(it, typeToken)
            }
        }
    }
}
