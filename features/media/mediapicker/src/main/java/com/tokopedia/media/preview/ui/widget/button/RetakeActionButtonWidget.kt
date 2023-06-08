package com.tokopedia.media.preview.ui.widget.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.R
import com.tokopedia.media.databinding.WidgetButtonRetakePreviewBinding
import com.tokopedia.picker.common.uimodel.MediaUiModel

class RetakeActionButtonWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    private var binding: WidgetButtonRetakePreviewBinding

    init {
        binding = WidgetButtonRetakePreviewBinding.inflate(
            LayoutInflater.from(context)
        )

        addView(binding.root)
    }

    fun setModeUi(media: MediaUiModel) {
        val isVideoFromCamera = media.file?.isVideo() == true && media.isCacheFile
        val isImageFromCamera = media.file?.isImage() == true && media.isCacheFile

        when {
            isVideoFromCamera -> videoMode()
            isImageFromCamera -> photoMode()
            else -> commonMode()
        }
    }

    private fun commonMode() {
        binding.imgAction.setImage(IconUnify.IMAGE)
        binding.txtTitle.text = context.getString(R.string.picker_button_retake_common)
    }

    private fun videoMode() {
        binding.imgAction.setImage(IconUnify.VIDEO)
        binding.txtTitle.text = context.getString(R.string.picker_button_retake_video)
    }

    private fun photoMode() {
        binding.imgAction.setImage(IconUnify.CAMERA)
        binding.txtTitle.text = context.getString(R.string.picker_button_retake_image)
    }
}
