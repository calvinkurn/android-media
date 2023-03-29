package com.tokopedia.picker.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.databinding.WidgetMediaThumbnailDebugBinding
import com.tokopedia.picker.common.mapper.humanize
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_3
import com.tokopedia.unifyprinciples.Typography.Companion.SMALL

class DebugMediaThumbnailWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DebugSquareFrameLayout(context, attributeSet, defStyleAttr) {

    private val binding: WidgetMediaThumbnailDebugBinding =
        WidgetMediaThumbnailDebugBinding.inflate(
            LayoutInflater.from(context)
        ).also {
            addView(it.root)
        }

    fun regularThumbnail(element: MediaUiModel?) {
        binding.txtDuration.setType(BODY_3)
        renderView(element)
    }

    fun smallThumbnail(element: MediaUiModel?) {
        binding.txtDuration.setType(SMALL)
        renderView(element)
    }

    private fun renderView(element: MediaUiModel?) {
        if (element == null) return
        val file = element.file?: return

        binding.container.show()

        binding.imgPreview.loadImage(file.path) {
            isAnimate(true)
            setPlaceHolder(-1)
            centerCrop()
        }

        if (file.isVideo()) {
            binding.bgVideoShadow.show()

            binding.txtDuration.show()
            binding.txtDuration.text = element.videoLength.humanize()
        }
    }

    fun removeWidget() {
        binding.container.hide()
    }

}
