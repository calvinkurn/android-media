package com.tokopedia.picker.common.cache

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import javax.inject.Inject

interface EditorAddLogoCacheManager {
    fun get(): String
    fun set(path: String)
}

class EditorAddLogoCacheManagerImpl @Inject constructor(
    @ApplicationContext context: Context
) : EditorAddLogoCacheManager,
    LocalCacheHandler(context, PREF_NAME_CACHE_ADD_LOGO) {
    override fun get(): String {
        return getString(KEY_LOCAL_LOGO, "")
    }

    override fun set(path: String) {
        putString(KEY_LOCAL_LOGO, path)
        applyEditor()
    }

    companion object {
        private const val PREF_NAME_CACHE_ADD_LOGO = "cache_add_logo_editor"
        private const val KEY_LOCAL_LOGO = "local_logo_path"
    }

}
