package com.tokopedia.editor.ui.main

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.picker.common.UniversalEditorParam
import javax.inject.Inject

interface EditorParamFetcher {
    fun get(): UniversalEditorParam
    fun set(param: UniversalEditorParam)
}

class EditorParamFetcherImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : LocalCacheHandler(context, PREF_NAME), EditorParamFetcher {

    override fun get(): UniversalEditorParam {
        val param = getString(KEY_CACHE) ?: return UniversalEditorParam()
        return gson.fromJson(param, UniversalEditorParam::class.java)
    }

    override fun set(param: UniversalEditorParam) {
        val result = gson.toJson(param)
        putString(KEY_CACHE, result)
        applyEditor()
    }

    companion object {
        private const val PREF_NAME = "universal_media_editor"
        private const val KEY_CACHE = "key_param"
    }
}
