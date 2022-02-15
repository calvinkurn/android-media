package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.tokopedia.media.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.preview.ui.uimodel.PreviewUiModel

class ImagePreview(
    private val context: Context
) : BasePagerPreview {

    override val layout: Int
        get() = R.layout.view_item_preview_image

    override fun setupView(media: PreviewUiModel): View {
        return rootLayoutView(context).also {
            val imagePreview = it.findViewById<ImageView>(R.id.img_preview)

            imagePreview.loadImage(media.data.path)
        }
    }

}