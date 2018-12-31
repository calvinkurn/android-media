package com.tokopedia.cachemanager

import android.content.Context
import android.os.Bundle
import java.lang.reflect.Type

/**
 * Use this to store saveInstanceState data to cache, avoiding TransactionTooLargeException in Nougat+
 * The different between PersistentCacheManager is
 * 1. The generatedId depends on savedInstanceState.
 * 2. It uses the separate table (to make the query faster)
 * 3. The data in the table will be removed after the get.
 *
 * HOW TO PUT VALUE
 * =================
 * To store data to cache:
 * override fun onCreate(savedInstanceState: Bundle?) {
 *      val cacheManager = SaveInstanceCacheManager(this, savedInstanceState)
 *      ...
 * }
 *
 * override fun onSaveInstanceState(outState: Bundle?) {
 *      super.onSaveInstanceState(outState)
 *      savedInstanceManager.onSave(outState)
 *      cacheManager.put("KEY_TO_PUT", model_to_put, TimeUnit.DAYS.toMillis(1))     // duration is optional, default is 1 day
 * }
 *
 * HOW TO GET VALUE
 * =================
 * val model = cacheManager.get("KEY_TO_PUT", TestModel::class.java, defaultModel)  // default model is optional
 * it is one use only. Once it is consumed, the second get will return null.
 *
 * HOW TO PASS VALUE (ONE USE ONLY)
 * ================================
 * It can be used to pass the value to another activity, if the data is big
 * [In Activity1]
 * val cacheManager = SaveInstanceCacheManager(this, true).apply {
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
 *      cacheManager = SaveInstanceCacheManager(this, savedInstanceState)
 *      val manager = if (savedInstanceState == null) SaveInstanceCacheManager(this, objectId) else cacheManager
 *      model = manager.get("KEY_TO_PUT", Model::class.java)
 * }
 * override fun onSaveInstanceState(outState: Bundle?) {
 *      super.onSaveInstanceState(outState)
 *      savedInstanceManager.onSave(outState)
 *      cacheManager.put("KEY_TO_PUT", model, TimeUnit.DAYS.toMillis(1))
 * }
 */
class SaveInstanceCacheManager : CacheManager {

    override fun createRepository(context: Context) =
            CacheManager.saveInstanceMap

    constructor(context: Context, savedInstanceState: Bundle?) : super(context) {
        if (savedInstanceState == null) {
            this.id = generateUniqueRandomNumber()
        } else {
            this.id = savedInstanceState.getString(KEY_ID)
        }
    }

    constructor(context: Context, id: String? = null) : super(context, id)

    constructor(context: Context, generateObjectId: Boolean) : super(context, generateObjectId)

    fun onSave(outState: Bundle?) {
        if (outState == null) {
            return
        }
        if (!outState.containsKey(KEY_ID)) {
            outState.putString(KEY_ID, id)
        }
    }

    override fun <T> get(customId: String, type: Type, defaultValue: T?): T? {
        try {
            val value = cacheRepository.get(id + customId) as T
            cacheRepository.remove(id + customId)
            return value
        } catch (e: Throwable) {
            return defaultValue
        }
    }
}
