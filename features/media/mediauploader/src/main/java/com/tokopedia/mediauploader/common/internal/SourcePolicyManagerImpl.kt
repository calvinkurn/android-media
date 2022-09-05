package com.tokopedia.mediauploader.common.internal

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

interface SourcePolicyManager {
    fun set(policy: SourcePolicy)
    fun get(): SourcePolicy
}

class SourcePolicyManagerImpl @Inject constructor(
    @ApplicationContext val context: Context,
    @ApplicationScope val gson: Gson,
) : SourcePolicyManager {

    private val sourcePolicy = SourcePolicy()
    private val mutex = Mutex()

    override fun get(): SourcePolicy {
        return synchronized(mutex) {
            sourcePolicy
        }
    }

    override fun set(policy: SourcePolicy) {
        return synchronized(mutex) {
            gson.toJson(policy)
        }
    }

}