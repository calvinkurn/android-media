package com.tokopedia.mediauploader.common.internal

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import javax.inject.Inject

interface SourcePolicyManager {
    fun set(policy: SourcePolicy)
    fun get(): SourcePolicy?
}

class SourcePolicyManagerImpl @Inject constructor(
    @ApplicationContext val context: Context
) : SourcePolicyManager, LocalCacheHandler(context, NAME_PREFERENCE_SOURCE_POLICY) {

    private val gson by lazy {
        Gson()
    }

    override fun set(policy: SourcePolicy) {
        val content = gson.toJson(policy)
        putString(KEY_SOURCE_POLICY, content)

        applyEditor()
    }

    override fun get(): SourcePolicy? {
        val json = getString(KEY_SOURCE_POLICY)?: return null
        return gson.fromJson(json, SourcePolicy::class.java)
    }

    companion object {
        private const val NAME_PREFERENCE_SOURCE_POLICY = "source_policy"
        private const val KEY_SOURCE_POLICY = "key_policy"
    }

}