package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.view.View
import com.tokopedia.media.common.uimodel.MediaUiModel

interface BasePagerPreview {
    val layout: Int
    fun setupView(media: MediaUiModel): View

    fun rootLayoutView(context: Context): View
        = View.inflate(context, layout, null)
}