package com.tokopedia.picker.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_EDITOR

object MediaEditor {

    fun intent(
        context: Context,
        imageList: List<String>,
        param: EditorParam.() -> Unit = {}
    ): Intent {
        val editorParam = EditorParam().apply(param)
        return generateIntent(context, imageList, editorParam)
    }

    fun intent(context: Context, imageList: List<String>, param: EditorParam): Intent {
        return generateIntent(context, imageList, param)
    }

    private fun generateIntent(
        context: Context,
        imageList: List<String>,
        editorParam: EditorParam
    ): Intent {
        val editorImageSource = EditorImageSource(imageList)

        return RouteManager.getIntent(context, INTERNAL_MEDIA_EDITOR).apply {
            putExtra(EXTRA_EDITOR_PARAM, editorParam)
        }.apply {
            putExtra(EXTRA_INTENT_EDITOR, editorImageSource)
        }
    }
}