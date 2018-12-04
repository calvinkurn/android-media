package com.tokopedia.cachemanager

import android.content.Context
import com.tokopedia.cachemanager.datasource.CacheDataSource
import com.tokopedia.cachemanager.db.CacheDatabase
import com.tokopedia.cachemanager.db.CacheDeletion
import com.tokopedia.cachemanager.db.dao.CacheDatabaseDao
import com.tokopedia.cachemanager.db.model.CacheDbModel
import com.tokopedia.cachemanager.gson.GsonSingleton
import kotlinx.coroutines.experimental.*
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit

abstract class CacheManager<U : CacheDbModel, T : CacheDatabaseDao<U>>(val context: Context) {

    var id: String? = ""
    val cacheDataSource: CacheDataSource<U, T> by lazy {
        createDataSource(context)
    }

    /**
     * Generally, no need generate Id, so put false
     * Only put generateId = true, if we want to generate to ID, so the id can be passed to another activity
     */
    constructor(context: Context, generateObjectId: Boolean) : this(context) {
        if (generateObjectId) {
            this.id = generateUniqueRandomNumber()
        }
    }

    /**
     * Generally, no need to put the id.
     * Only put the Id, if it is passed from another activity/view
     */
    constructor(context: Context, id: String? = null) : this(context) {
        this.id = id
    }

    abstract fun createDataSource(context: Context): CacheDataSource<U, T>

    fun generateUniqueRandomNumber() = System.currentTimeMillis().toString() + Random().nextInt(RANDOM_RANGE)

    fun <T> get(customId: String, type: Type, defaultValue: T? = null): T? {
        try {
            val model = cacheDataSource.get(id + customId) ?: (return defaultValue)
            val jsonString: String? = model.value
            if (jsonString.isNullOrEmpty()) {
                return defaultValue
            } else {
                return GsonSingleton.getInstance().fromJson(jsonString, type)
            }
        } catch (e: Throwable) {
            return defaultValue
        }
    }

    fun getString(customId: String, defaultString: String): String? {
        try {
            val model = cacheDataSource.get(id + customId) ?: (return defaultString)
            val value: String? = model.value
            if (value.isNullOrEmpty()) {
                return defaultString
            } else {
                return value
            }
        } catch (e: Throwable) {
            return defaultString
        }
    }

    fun put(customId: String, objectToPut: Any?, cacheDuration: Long = defaultExpiredDuration) {
        if (objectToPut == null) {
            return
        }
        try {
            cacheDataSource.put(id + customId,
                    GsonSingleton.getInstance().toJson(objectToPut),
                    cacheDuration)
        } catch (e: Throwable) {
            return
        }
    }

    fun put(customId: String, objectToPut: String?, cacheDuration: Long = defaultExpiredDuration) {
        if (objectToPut == null) {
            return
        }
        try {
            cacheDataSource.put(id + customId,
                    objectToPut,
                    cacheDuration)
        } catch (e: Throwable) {
            return
        }
    }

    companion object {
        const val KEY_ID = "KEY_ID"
        const val RANDOM_RANGE = 100
        val defaultExpiredDuration by lazy {
            TimeUnit.DAYS.toMillis(1)
        }

        @JvmStatic
        fun deletePersistentExpiration(context: Context) {
            if (CacheDeletion.isPersistentNeedDeletion()
                    && !CacheDeletion.isPersistentJobActive) {
                CacheDeletion.isPersistentJobActive = true
                val handler = CoroutineExceptionHandler { _, ex ->
                    // no op
                }
                GlobalScope.launch(Dispatchers.Default + CacheDeletion.persistentExpireDeletionJob + handler) {
                    CacheDatabase.getInstance(context).getPersistentCacheDao()
                            .deleteExpiredRecords(System.currentTimeMillis())
                    CacheDeletion.setPersistentLastDelete()
                    CacheDeletion.isPersistentJobActive = false
                }
            }
        }

        @JvmStatic
        fun deleteSaveInstanceExpiration(context: Context) {
            if (CacheDeletion.isSaveInstanceNeedDeletion()
                    && !CacheDeletion.isSaveInstanceJobActive) {
                CacheDeletion.isSaveInstanceJobActive = true
                val handler = CoroutineExceptionHandler { _, ex ->
                    // no op
                }
                GlobalScope.launch(Dispatchers.Default + CacheDeletion.saveInstanceExpireDeletionJob + handler) {
                    CacheDatabase.getInstance(context).getSaveInstanceCacheDao()
                            .deleteExpiredRecords(System.currentTimeMillis())
                    CacheDeletion.setSaveInstanceLastDelete()
                    CacheDeletion.isSaveInstanceJobActive = false
                }
            }
        }

        @JvmStatic
        fun deleteSaveInstanceCache(context: Context) {
            val job = Job()
            val handler = CoroutineExceptionHandler { _, ex ->
                // no op
            }
            GlobalScope.launch(Dispatchers.Default + job + handler) {
                CacheDatabase.getInstance(context).getSaveInstanceCacheDao()
                        .deleteTable()
            }
        }
    }
}