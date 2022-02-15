package com.tokopedia.media.common.intent

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER_PREVIEW
import com.tokopedia.media.common.uimodel.MediaUiModel

object PreviewIntent : BaseIntent<MediaUiModel> {

    override val appLink: String
        get() = INTERNAL_MEDIA_PICKER_PREVIEW

    override val keyName: String
        get() = "picker-preview"

    fun intent(context: Context, elements: ArrayList<MediaUiModel>): Intent {
        return RouteManager.getIntent(context, appLink).apply {
            putExtra(keyName, elements)
        }
    }

    fun intentWith(context: Context, elements: ArrayList<MediaUiModel>) {
        if (elements.isEmpty()) return
        val intent = intent(context, elements)
        context.startActivity(intent)
    }

}