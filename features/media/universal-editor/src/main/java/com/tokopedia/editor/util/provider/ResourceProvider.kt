package com.tokopedia.editor.util.provider

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

interface ResourceProvider {

    fun getString(id: Int): String
}

class ResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {

    override fun getString(id: Int): String {
        return context.getString(id)
    }
}
