package com.tokopedia.cachemanager

import android.content.Context
import java.lang.reflect.Type
import java.util.*

abstract class CacheManager(val context: Context) {

    var id: String? = ""
    val cacheRepository: HashMap<String, Any?> by lazy {
        createRepository(context)
    }

    constructor(context: Context, generateObjectId: Boolean) : this(context) {
        if (generateObjectId) {
            this.id = generateUniqueRandomNumber()
        }
    }

    constructor(context: Context, id: String? = null) : this(context) {
        this.id = id
    }

    abstract fun createRepository(context: Context): HashMap<String, Any?>

    fun generateUniqueRandomNumber() = System.currentTimeMillis().toString() + Random().nextInt(RANDOM_RANGE)

    open fun <T> get(customId: String, type: Type, defaultValue: T? = null): T? {
        try {
            return cacheRepository.get(id + customId) as T
        } catch (e: Throwable) {
            return defaultValue
        }
    }

    fun getString(customId: String, defaultString: String): String? {
        try {
            return cacheRepository.get(id + customId) as String
        } catch (e: Throwable) {
            return defaultString
        }
    }

    @JvmOverloads
    fun put(customId: String, objectToPut: Any?, cacheDuration: Long = 0) {
        if (objectToPut == null) {
            return
        }
        try {
            cacheRepository.put(id + customId,
                    objectToPut)
        } catch (e: Throwable) {
            return
        }
    }

    @JvmOverloads
    fun put(customId: String, objectToPut: String?, cacheDuration: Long = 0) {
        if (objectToPut == null) {
            return
        }
        try {
            cacheRepository.put(id + customId,
                    objectToPut)
        } catch (e: Throwable) {
            return
        }
    }

    fun deleteExpiredRecords() {
        //no-op
    }

    fun delete() {
        //no-op
    }

    companion object {
        const val KEY_ID = "KEY_ID"
        const val RANDOM_RANGE = 100

        val persistentMap = HashMap<String, Any?>()
        val saveInstanceMap = HashMap<String, Any?>()
    }
}
