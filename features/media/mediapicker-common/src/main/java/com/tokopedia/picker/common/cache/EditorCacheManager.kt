package com.tokopedia.picker.common.cache

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.picker.common.EditorParam
import javax.inject.Inject

interface EditorCacheManager {
    fun get(): EditorParam
    fun set(param: EditorParam)
}

class EditorParamCacheManager @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) : LocalCacheHandler(context, PREF_CACHE_EDITOR), EditorCacheManager {

    override fun get(): EditorParam {
        val param = getString(KEY_EDITOR_PARAM) ?: return EditorParam()
        return gson.fromJson(param, EditorParam::class.java)
    }

    override fun set(param: EditorParam) {
        val result = gson.toJson(param)
        putString(KEY_EDITOR_PARAM, result)
        applyEditor()
    }

    companion object {
        private const val PREF_CACHE_EDITOR = "cache_media_editor"
        private const val KEY_EDITOR_PARAM = "key_editor_param"
    }
}
