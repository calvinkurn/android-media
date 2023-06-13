package com.tokopedia.media.picker.utils.internal

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

interface ResourceManager {
    fun string(id: Int): String
}

class ResourceManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceManager {

    override fun string(id: Int): String {
        return context.getString(id)
    }
}
