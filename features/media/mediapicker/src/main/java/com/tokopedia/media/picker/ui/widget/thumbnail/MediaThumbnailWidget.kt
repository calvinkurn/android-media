package com.tokopedia.media.picker.ui.widget.thumbnail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.databinding.WidgetMediaThumbnailBinding
import com.tokopedia.media.picker.ui.widget.layout.SquareFrameLayout
import com.tokopedia.media.picker.utils.loadPickerImage
import com.tokopedia.picker.common.mapper.humanize
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_3
import com.tokopedia.unifyprinciples.Typography.Companion.SMALL
import com.tokopedia.media.R as mediaResources

class MediaThumbnailWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SquareFrameLayout(context, attributeSet, defStyleAttr) {

    private var binding: WidgetMediaThumbnailBinding

    init {
        binding = WidgetMediaThumbnailBinding.inflate(
            LayoutInflater.from(context)
        )

        addView(binding.root)
    }

    fun regularThumbnail(element: MediaUiModel?, onLoaded: () -> Unit = {}) {
        binding.txtDuration.setType(DISPLAY_3)
        renderView(element, onLoaded)
    }

    fun smallThumbnail(element: MediaUiModel?, onLoaded: () -> Unit = {}) {
        binding.txtDuration.setType(SMALL)
        renderView(element, onLoaded)
    }

    private fun renderView(element: MediaUiModel?, onLoaded: () -> Unit) {
        if (element == null) return
        val file = element.file ?: return

        binding.container.show()
        binding.imgPreview.loadPickerImage(file.path, onLoaded)
        binding.bgVideoShadow.showWithCondition(file.isVideo())
        binding.txtDuration.showIfWithBlock(file.isVideo()) {
            binding.txtDuration.text = element.duration.humanize()
        }
    }

    fun removeWidget() {
        binding.container.hide()
    }

    fun setThumbnailSelected(isSelected: Boolean) {
        if (isSelected) {
            val paddingSize =
                resources.getDimension(mediaResources.dimen.picker_thumbnail_selected_padding)
                    .toInt()
            val backgroundAsset = MethodChecker.getDrawable(
                context,
                mediaResources.drawable.picker_rect_green_selected_thumbnail
            )

            binding.imgPreview.setPadding(paddingSize, paddingSize, paddingSize, paddingSize)
            binding.imgPreview.background = backgroundAsset
        } else {
            binding.imgPreview.setPadding(0, 0, 0, 0)
            binding.imgPreview.background = null
        }
    }
}
