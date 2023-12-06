package com.tokopedia.cachemanager

import android.content.Context
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.cachemanager.repository.ICacheRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    fun generateUniqueRandomNumber() =
        System.currentTimeMillis().toString() + Random().nextInt(RANDOM_RANGE)

    @JvmOverloads
    fun <T> get(customId: String, type: Type, defaultValue: T? = null): T? {
        return try {
            val jsonString: String? = cacheRepository.get(id + customId)
            if (jsonString.isNullOrEmpty()) {
                defaultValue
            } else {
                GsonSingleton.instance.fromJson(jsonString, type)
            }
        } catch (e: Throwable) {
            defaultValue
        }
    }

    @JvmOverloads
    fun <T> getFlow(customId: String, type: Type, defaultValue: T? = null): Flow<T?> {
        return cacheRepository.getFlow(id + customId).map {
            val jsonString: String? = it
            if (jsonString.isNullOrEmpty()) {
                defaultValue
            } else {
                GsonSingleton.instance.fromJson(jsonString, type)
            }
        }
    }

    fun getFlow(customId: String): Flow<String?> {
        return cacheRepository.getFlow(id + customId)
    }

    @JvmOverloads
    fun getString(customId: String, defaultString: String? = ""): String? {
        return try {
            val value: String? = cacheRepository.get(id + customId)
            if (value.isNullOrEmpty()) {
                defaultString
            } else {
                value
            }
        } catch (e: Throwable) {
            defaultString
        }
    }

    @JvmOverloads
    fun put(customId: String, objectToPut: Any?, cacheDuration: Long = defaultExpiredDuration) {
        if (objectToPut == null) {
            return
        }
        try {
            cacheRepository.put(
                id + customId,
                GsonSingleton.instance.toJson(objectToPut),
                cacheDuration
            )
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
            cacheRepository.put(
                id + customId,
                objectToPut,
                cacheDuration
            )
        } catch (e: Throwable) {
            return
        }
    }

    fun isExpired(key: String) = cacheRepository.get(key)?.isEmpty() ?: true

    fun deleteExpiredRecords() {
        cacheRepository.deleteExpiredRecords()
    }

    fun delete() {
        cacheRepository.delete()
    }

    fun delete(key: String) {
        cacheRepository.delete(key)
    }

    companion object {
        const val KEY_ID = "KEY_ID"
        const val RANDOM_RANGE = 100
        val defaultExpiredDuration by lazy {
            TimeUnit.DAYS.toMillis(1)
        }
    }
}
