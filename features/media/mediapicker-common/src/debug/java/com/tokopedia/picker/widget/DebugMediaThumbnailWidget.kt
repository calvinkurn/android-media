package com.tokopedia.picker.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.databinding.WidgetMediaThumbnailDebugBinding
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.extractVideoDuration
import com.tokopedia.picker.common.utils.videoFormat
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
        binding.container.show()

        binding.imgPreview.loadImage(element.path) {
            isAnimate(true)
            setPlaceHolder(-1)
            centerCrop()
        }

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
        binding.txtDuration.text = duration.videoFormat()
    }

}