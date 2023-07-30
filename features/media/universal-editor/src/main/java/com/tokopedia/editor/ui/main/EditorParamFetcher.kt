package com.tokopedia.editor.ui.main

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.picker.common.UniversalEditorParam
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val NAME = "universal_media_editor"
private val Context.dataStore by preferencesDataStore(NAME)

interface EditorParamFetcher {
    suspend operator fun invoke(): UniversalEditorParam?
    suspend fun set(param: UniversalEditorParam.() -> Unit)
    suspend fun set(param: UniversalEditorParam)
}

class EditorParamFetcherImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : EditorParamFetcher {

    override suspend fun invoke(): UniversalEditorParam? {
        return context.dataStore.data
            .map {
                val content = it[stringPreferencesKey(KEY)] ?: ""
                Gson().fromJson(content, UniversalEditorParam::class.java)
            }.firstOrNull()
    }

    override suspend fun set(param: UniversalEditorParam.() -> Unit) {
        context.dataStore.edit {
            val element = invoke()?.apply(param) ?: UniversalEditorParam()
            it[stringPreferencesKey(KEY)] = Gson().toJson(element)
        }
    }

    override suspend fun set(param: UniversalEditorParam) {
        context.dataStore.edit {
            it[stringPreferencesKey(KEY)] = Gson().toJson(param)
        }
    }

    companion object {
        private const val KEY = "universal_param"
    }
}
