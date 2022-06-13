package com.tokopedia.mediauploader.common.internal

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.data.entity.UploaderPolicy
import com.tokopedia.url.Env
import javax.inject.Inject

class SourcePolicyManager @Inject constructor(
    @ApplicationContext val context: Context
) : LocalCacheHandler(context, NAME_PREFERENCE_SOURCE_POLICY) {

    fun set(policy: SourcePolicy) {
        val content = Gson().toJson(policy)
        putString(KEY_SOURCE_POLICY, content)

        applyEditor()
    }

    fun policy(): SourcePolicy {
        val json = getString(KEY_SOURCE_POLICY, "")
        return Gson().fromJson(json, SourcePolicy::class.java)
    }

    fun clear() {
        clearCache()
    }

    companion object {
        private const val NAME_PREFERENCE_SOURCE_POLICY = "source_policy"
        private const val KEY_SOURCE_POLICY = "key_policy"
    }

}