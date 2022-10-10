package com.tokopedia.media.picker.ui.widget.thumbnail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.databinding.WidgetMediaThumbnailBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.media.picker.ui.widget.layout.SquareFrameLayout
import com.tokopedia.media.picker.utils.loadPickerImage
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.media.R as mediaResources
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

    fun regularThumbnail(element: MediaUiModel?, onLoaded: () -> Unit = {}) {
        binding.txtDuration.setType(BODY_3)
        renderView(element, onLoaded)
    }

    fun smallThumbnail(element: MediaUiModel?, onLoaded: () -> Unit = {}) {
        binding.txtDuration.setType(SMALL)
        renderView(element, onLoaded)
    }

    private fun renderView(element: MediaUiModel?, onLoaded: () -> Unit) {
        if (element == null) return
        val file = element.file?: return

        binding.container.show()
        binding.imgPreview.loadPickerImage(file.path, onLoaded)

        onVideoShow(file)
    }

    private fun onVideoShow(file: PickerFile) {
        binding.bgVideoShadow.showWithCondition(file.isVideo())
        binding.txtDuration.shouldShowWithAction(file.isVideo()) {
            binding.txtDuration.text = file.readableVideoDuration(context)
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