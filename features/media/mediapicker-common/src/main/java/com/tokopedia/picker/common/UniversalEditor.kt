package com.tokopedia.picker.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_UNIVERSAL_MEDIA_EDITOR

object UniversalEditor {

    fun intent(context: Context, param: UniversalEditorParam.() -> Unit = {}): Intent {
        val editorParam = UniversalEditorParam().apply(param)
        return RouteManager.getIntent(context, INTERNAL_UNIVERSAL_MEDIA_EDITOR).apply {
            putExtra(EXTRA_UNIVERSAL_EDITOR_PARAM, editorParam)
        }
    }
}
