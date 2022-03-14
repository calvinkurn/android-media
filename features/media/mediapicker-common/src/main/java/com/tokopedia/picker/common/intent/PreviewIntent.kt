package com.tokopedia.picker.common.intent

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER_PREVIEW
import com.tokopedia.picker.common.uimodel.MediaUiModel

object PreviewIntent {

    private const val KEY_PREVIEW_ITEMS = "picker-preview-items"

    fun intent(context: Context, elements: ArrayList<MediaUiModel>): Intent {
        return RouteManager.getIntent(context, INTERNAL_MEDIA_PICKER_PREVIEW).apply {
            putExtra(KEY_PREVIEW_ITEMS, elements)
        }
    }

    fun result(intent: Intent?): ArrayList<MediaUiModel> {
        return intent?.getParcelableArrayListExtra(KEY_PREVIEW_ITEMS)?: arrayListOf()
    }

}