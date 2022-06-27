package com.tokopedia.picker.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_EDITOR

object MediaEditor {

    fun intent(context: Context, param: EditorParam.() -> Unit = {}): Intent {
        val editorParam = EditorParam().apply(param)

        return RouteManager.getIntent(context, INTERNAL_MEDIA_EDITOR).apply {
            putExtra(EXTRA_EDITOR_PARAM, editorParam)
        }
    }

}