package com.tokopedia.media.editor.utils

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.picker.common.PickerParam
import javax.inject.Inject

class ParamCacheManager @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) : LocalCacheHandler(context, PREF_NAME_CACHE_PICKER) {

    fun getPickerParam(): PickerParam {
        val param = getString(KEY_PICKER_PARAM) ?: return PickerParam()
        return gson.fromJson(param, PickerParam::class.java)
    }

    fun setPickerParam(param: PickerParam) {
        val toJson = gson.toJson(param)
        putString(KEY_PICKER_PARAM, toJson)
        applyEditor()
    }

    fun getEditorParam(): EditorParam {
        val param = getString(KEY_EDITOR_PARAM) ?: return EditorParam()
        return gson.fromJson(param, EditorParam::class.java)
    }

    fun setEditorParam(param: EditorParam) {
        val toJson = gson.toJson(param)
        putString(KEY_EDITOR_PARAM, toJson)
        applyEditor()
    }

    companion object {
        private const val PREF_NAME_CACHE_PICKER = "cache_media_editor"
        private const val KEY_PICKER_PARAM = "key_picker_param"
        private const val KEY_EDITOR_PARAM = "key_editor_param"
    }

}