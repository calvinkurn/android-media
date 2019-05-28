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
 */
class PersistentCacheManager(context: Context) : CacheManager(context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: PersistentCacheManager

        @JvmStatic
        fun init(context: Context) {
            PersistentCacheManager(context)
        }
    }

    init {
        instance = this
    }

    override fun createRepository(context: Context) =
        PersistentCacheRepository(context.applicationContext)

    constructor(context: Context, id: String? = null) : this(context) {
        this.id = id
    }
}
