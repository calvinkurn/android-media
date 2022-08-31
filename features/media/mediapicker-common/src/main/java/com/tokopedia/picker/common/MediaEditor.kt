package com.tokopedia.picker.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_EDITOR

object MediaEditor {

    fun intent(context: Context, param: EditorParam.() -> Unit = {}, imageList: List<String>): Intent {
        val editorParam = EditorParam().apply(param)
        return generateIntent(context, editorParam, imageList)
    }

    fun intent(context: Context, param: EditorParam, imageList: List<String>): Intent {
        return generateIntent(context, param, imageList)
    }

    private fun generateIntent(context: Context, editorParam: EditorParam, imageList: List<String>): Intent{
        val editorImageSource = EditorImageSource(imageList)

        return RouteManager.getIntent(context, INTERNAL_MEDIA_EDITOR).apply {
            putExtra(EXTRA_EDITOR_PARAM, editorParam)
        }.apply {
            putExtra(EXTRA_INTENT_EDITOR, editorImageSource)
        }
    }
}