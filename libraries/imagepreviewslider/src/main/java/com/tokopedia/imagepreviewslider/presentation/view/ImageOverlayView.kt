package com.tokopedia.imagepreviewslider.presentation.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.imagepreviewslider.R
import kotlinx.android.synthetic.main.view_image_overlay.view.*

/**
 * @author by jessica on 2019-12-16
 */

class ImageOverlayView(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_image_overlay, this)
        context?.let {
            tv_title_overlay.setShadowLayer(1.6f, 1.5f, 1.3f, androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_96))
        }
    }

    fun updateImageIndexPosition(position: Int, imageList: List<String>?) {
        val imageIndex = "${position + 1} / ${imageList?.size}"
        image_index.text = imageIndex
    }
}