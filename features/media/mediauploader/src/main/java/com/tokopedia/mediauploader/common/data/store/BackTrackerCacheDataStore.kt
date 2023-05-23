package com.tokopedia.mediauploader.common.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.mediauploader.common.VideoMetaDataExtractor
import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

interface BackTrackerCacheDataStore {
    fun key(sourceId: String, originalPath: String): String

    suspend fun get(key: String): UploaderTracker?
    suspend fun set(key: String, cache: UploaderTracker.() -> Unit)

    suspend fun setOriginalVideoInfo(key: String, file: File)
    suspend fun setStartUploadTime(key: String, time: Long)
    suspend fun setEndUploadTime(key: String, time: Long)
}

class BackTrackerCacheDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val metaDataExtractor: VideoMetaDataExtractor,
    private val gson: Gson
) : BackTrackerCacheDataStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(NAME)

    override suspend fun setOriginalVideoInfo(key: String, file: File) {
        set(key) {
            originalVideoPath = file.path
            videoOriginalSize = file.length().toString()
            originalVideoMetadata = metaDataExtractor.extract(file.path)
        }
    }

    override suspend fun setStartUploadTime(key: String, time: Long) {
        set(key) {
            startUploadTime = time
        }
    }

    override suspend fun setEndUploadTime(key: String, time: Long) {
        set(key) {
            endUploadTime = time
        }
    }

    override fun key(sourceId: String, originalPath: String): String {
        return "$sourceId$originalPath"
    }

    override suspend fun get(key: String): UploaderTracker? {
        return context.dataStore.data
            .map {
                val content = it[stringPreferencesKey(key)] ?: ""
                gson.fromJson(content, UploaderTracker::class.java)
            }.firstOrNull()
    }

    override suspend fun set(key: String, cache: UploaderTracker.() -> Unit) {
        context.dataStore.edit {
            val element = get(key)?.apply(cache) ?: default(cache)
            val json = gson.toJson(element)

            it[stringPreferencesKey(key)] = json

            if (GlobalConfig.isAllowDebuggingTools()) {
                println("MEDIA-UPLOADER: (store) $json")
            }
        }
    }

    private fun default(cache: UploaderTracker.() -> Unit): UploaderTracker {
        return UploaderTracker().apply(cache)
    }

    companion object {
        private const val NAME = "tracker_cache"
    }
}
