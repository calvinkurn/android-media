package com.tokopedia.picker.common.cache

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import javax.inject.Inject

interface EditorAddTextCacheManager {
    fun get(): String
    fun set(textDetail: String)
}

class EditorAddTextCacheManagerImpl @Inject constructor(
    @ApplicationContext context: Context
) : EditorAddTextCacheManager {

    private val localCacheHandler = LocalCacheHandler(context, PREF_NAME_CACHE_ADD_LOGO)

    override fun get(): String {
        return localCacheHandler.getString(KEY_LOCAL_LOGO, "")
    }

    override fun set(textDetail: String) {
        localCacheHandler.putString(KEY_LOCAL_LOGO, textDetail)
        localCacheHandler.applyEditor()
    }

    companion object {
        private const val PREF_NAME_CACHE_ADD_LOGO = "cache_add_text_editor"
        private const val KEY_LOCAL_LOGO = "key_add_text_detail"
    }

}
