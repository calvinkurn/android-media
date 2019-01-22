package com.tokopedia.cachemanager

import android.content.Context
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.cachemanager.repository.ICacheRepository
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit

abstract class CacheManager(val context: Context) {

    var id: String? = ""
    val cacheRepository: ICacheRepository by lazy {
        createRepository(context)
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

    abstract fun createRepository(context: Context): ICacheRepository

    fun generateUniqueRandomNumber() = System.currentTimeMillis().toString() + Random().nextInt(RANDOM_RANGE)

    fun <T> get(customId: String, type: Type, defaultValue: T? = null): T? {
        try {
            val jsonString: String? = cacheRepository.get(id + customId)
            if (jsonString.isNullOrEmpty()) {
                return defaultValue
            } else {
                return GsonSingleton.instance.fromJson(jsonString, type)
            }
        } catch (e: Throwable) {
            return defaultValue
        }
    }

    fun getString(customId: String, defaultString: String): String? {
        try {
            val value: String? = cacheRepository.get(id + customId)
            if (value.isNullOrEmpty()) {
                return defaultString
            } else {
                return value
            }
        } catch (e: Throwable) {
            return defaultString
        }
    }

    @JvmOverloads
    fun put(customId: String, objectToPut: Any?, cacheDuration: Long = defaultExpiredDuration) {
        if (objectToPut == null) {
            return
        }
        try {
            cacheRepository.put(id + customId,
                    GsonSingleton.instance.toJson(objectToPut),
                    cacheDuration)
        } catch (e: Throwable) {
            return
        }
    }

    @JvmOverloads
    fun put(customId: String, objectToPut: String?, cacheDuration: Long = defaultExpiredDuration) {
        if (objectToPut == null) {
            return
        }
        try {
            cacheRepository.put(id + customId,
                    objectToPut,
                    cacheDuration)
        } catch (e: Throwable) {
            return
        }
    }

    fun deleteExpiredRecords() {
        cacheRepository.deleteExpiredRecords()
    }

    fun delete() {
        cacheRepository.delete()
    }

    companion object {
        const val KEY_ID = "KEY_ID"
        const val RANDOM_RANGE = 100
        val defaultExpiredDuration by lazy {
            TimeUnit.DAYS.toMillis(1)
        }
    }
}
