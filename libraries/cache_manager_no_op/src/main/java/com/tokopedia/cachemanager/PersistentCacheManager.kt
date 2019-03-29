package com.tokopedia.cachemanager

import android.content.Context

/**
 * Use this to store persistent data to cache (replacing the old GlobalCacheManager)
 *
 * HOW TO PUT GENERAL VALUE
 * ========================
 * To store data to cache:
 * val cacheManager = PersistentCacheManager(this)
 * cacheManager.put("KEY_TO_PUT", model_to_put, TimeUnit.DAYS.toMillis(1))          // duration is optional, default is 1 day
 *
 * HOW TO GET VALUE
 * =================
 * val model = cacheManager.get("KEY_TO_PUT", TestModel::class.java, defaultModel)  // default model is optional
 *
 * HOW TO PASS VALUE (PERSISTENT)
 * ==============================
 * It can be used to pass the value to another activity, if the data is big
 * [In Activity1]
 * val cacheManager = PersistentCacheManager(this, true).apply {
 *      put("KEY_TO_PUT_1", modelToPut)
 *      put("KEY_TO_PUT_2", modelToPut2)
 * }
 * startActivity(Intent(this, TargetActivity::class.java).apply {
 *      putExtra(PASS_OBJECT_ID, cacheManager.id)
 * })
 *
 * [In target Activity]
 * override fun onCreate(savedInstanceState: Bundle?) {
 *      val objectId = intent.getStringExtra(PASS_OBJECT_ID)
 *      val cacheManager = PersistentCacheManager(this, objectId)
 *      ...
 * }
 *
 */
class PersistentCacheManager : CacheManager {

    override fun createRepository(context: Context) =
        CacheManager.persistentMap

    constructor(context: Context, id: String? = null) : super(context, id)

    constructor(context: Context, generateObjectId: Boolean) : super(context, generateObjectId)
}
