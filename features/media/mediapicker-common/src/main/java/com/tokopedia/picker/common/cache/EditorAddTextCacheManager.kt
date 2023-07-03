package com.tokopedia.picker.common.cache

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import javax.inject.Inject

interface EditorAddTextCacheManager {
    fun get(): String
    fun set(textDetail: String)
    fun setTipsState()
    fun getTipsState(): Boolean
}

class EditorAddTextCacheManagerImpl @Inject constructor(
    @ApplicationContext context: Context
) : EditorAddTextCacheManager,
    LocalCacheHandler(context, PREF_NAME_CACHE_ADD_TEXT) {
    override fun get(): String {
        return getString(KEY_LOCAL_TEXT, "")
    }

    override fun set(textDetail: String) {
        putString(KEY_LOCAL_TEXT, textDetail)
        applyEditor()
    }

    override fun setTipsState() {
        putBoolean(KEY_LOCAL_TEXT_TIPS, true)
        applyEditor()
    }

    // true = tips already showed, no need to show it again
    override fun getTipsState(): Boolean {
        return getBoolean(KEY_LOCAL_TEXT_TIPS, false)
    }

    companion object {
        private const val PREF_NAME_CACHE_ADD_TEXT = "cache_add_text_editor"
        private const val KEY_LOCAL_TEXT = "key_add_text_detail"
        private const val KEY_LOCAL_TEXT_TIPS = "key_add_text_tips"
    }

}
