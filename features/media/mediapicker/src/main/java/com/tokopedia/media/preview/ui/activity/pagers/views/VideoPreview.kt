package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.view.View
import com.tokopedia.media.R
import com.tokopedia.media.common.uimodel.MediaUiModel

class VideoPreview(
    private val context: Context
) : BasePagerPreview {

    override val layout: Int
        get() = R.layout.view_item_preview_video

    override fun setupView(media: MediaUiModel): View {
        return rootLayoutView(context).also {

        }
    }

}