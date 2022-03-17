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
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.media.picker.ui.widget.layout.SquareFrameLayout
import com.tokopedia.picker.common.utils.extractVideoDuration
import com.tokopedia.media.picker.utils.pickerLoadImage
import com.tokopedia.picker.common.utils.toVideoDurationFormat
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
        binding.bgVideoShadow.showWithCondition(element.isVideo())
        binding.txtDuration.shouldShowWithAction(element.isVideo()) {
            videoDuration(element.path)
        }
    }

    fun removeWidget() {
        binding.container.hide()
    }

    private fun videoDuration(filePath: String) {
        val duration = extractVideoDuration(context, filePath)
        binding.txtDuration.text = duration.toVideoDurationFormat()
    }

    fun setThumbnailSelected(isSelected: Boolean){
        if(isSelected){
            val paddingSize = resources.getDimension(mediaResources.dimen.picker_thumbnail_selected_padding).toInt()
            val backgroundAsset = MethodChecker.getDrawable(context, mediaResources.drawable.picker_rect_green_selected_thumbnail)

            binding.imgPreview.setPadding(paddingSize, paddingSize, paddingSize, paddingSize)
            binding.imgPreview.background = backgroundAsset
        } else {
            binding.imgPreview.setPadding(0, 0, 0, 0)
            binding.imgPreview.background = null
        }
    }
}