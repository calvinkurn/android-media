package com.tokopedia.media.preview.ui.widget.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.media.R
import com.tokopedia.media.databinding.WidgetButtonRetakePreviewBinding

class RetakeActionButtonWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    private val binding: WidgetButtonRetakePreviewBinding =
        WidgetButtonRetakePreviewBinding.inflate(
            LayoutInflater.from(context)
        ).also {
            addView(it.root)
        }

    fun commonMode() {
        binding.imgAction.setImageResource(com.tokopedia.iconunify.R.drawable.iconunify_image)
        binding.txtTitle.text = context.getString(R.string.picker_button_retake_common)
    }

    fun videoMode() {
        binding.imgAction.setImageResource(com.tokopedia.iconunify.R.drawable.iconunify_video)
        binding.txtTitle.text = context.getString(R.string.picker_button_retake_video)
    }

    fun photoMode() {
        binding.imgAction.setImageResource(com.tokopedia.iconunify.R.drawable.iconunify_camera)
        binding.txtTitle.text = context.getString(R.string.picker_button_retake_image)
    }

}