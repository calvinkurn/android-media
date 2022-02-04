package com.tokopedia.picker.ui.widget.thumbnail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.picker.databinding.WidgetMediaThumbnailBinding
import com.tokopedia.picker.ui.uimodel.MediaUiModel
import com.tokopedia.picker.ui.widget.layout.SquareFrameLayout
import com.tokopedia.picker.utils.extractVideoDuration
import com.tokopedia.picker.utils.pickerLoadImage
import com.tokopedia.picker.utils.toVideoDurationFormat
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_3
import com.tokopedia.unifyprinciples.Typography.Companion.SMALL

class MediaThumbnailWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SquareFrameLayout(context, attributeSet, defStyleAttr) {

    private val binding: WidgetMediaThumbnailBinding =
        WidgetMediaThumbnailBinding.inflate(
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
        binding.container.show()
        binding.imgPreview.pickerLoadImage(element.path)

        if (element.isVideo()) {
            videoDuration(element.path)

            binding.bgVideoShadow.show()
            binding.txtDuration.show()
        }
    }

    fun removeWidget() {
        binding.container.hide()
    }

    private fun videoDuration(filePath: String) {
        val duration = extractVideoDuration(context, filePath)
        binding.txtDuration.text = duration.toVideoDurationFormat()
    }

}