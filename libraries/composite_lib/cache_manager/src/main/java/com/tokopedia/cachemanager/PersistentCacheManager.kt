package com.tokopedia.cachemanager

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.cachemanager.repository.PersistentCacheRepository

/**
 * Use this to store persistent data to cache (replacing the old GlobalCacheManager)
 *
 * ========================
 * init at application level:
 * PersistentCacheManager.init(this);
 *
 * HOW TO PUT GENERAL VALUE
 * ========================
 * To store data to cache:
 * PersistentCacheManager.instance.put("KEY_TO_PUT", model_to_put, TimeUnit.DAYS.toMillis(1))          // duration is optional, default is 1 day
 *
 * HOW TO GET VALUE
 * =================
 * val model = PersistentCacheManager.instance.get("KEY_TO_PUT", TestModel::class.java, defaultModel)  // default model is optional
 *
 * HOW TO PASS VALUE (PERSISTENT)
 * ==============================
 * Use SaveInstanceCacheManager instead, because the value will automatically deleted when we get th value.
 *
 * HOW TO LISTEN TO CHANGED VALUES
 * PersistentCacheManager.get(context).getFlow("KEY_DB",
 *   MyObject::class.java,
 *   MyObject())
 * or
 * PersistentCacheManager.get(context).getFlow("KEY_DB") if the object is string
 */
class PersistentCacheManager(context: Context) : CacheManager(context) {

    constructor(context: Context, id: String? = null) : this(context) {
        this.id = id
    }

    companion object {
        /**
         * instance for persistentCacheManager with id null
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: PersistentCacheManager

        @JvmStatic
        fun init(context: Context):PersistentCacheManager {
            return PersistentCacheManager(context).also {
                instance = it
            }
        }
    }

    init {
        if (id == null) {
            instance = this
        }
    }

    override fun createRepository(context: Context): PersistentCacheRepository {
        return PersistentCacheRepository.create(context.applicationContext)
    }

}
