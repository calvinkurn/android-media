package com.tokopedia.media.editor.ui.component

import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.component.slider.MediaEditorSlider
import com.tokopedia.picker.common.basecomponent.UiComponent

class CropToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_crop) {

    private val btn11Ref = findViewById<View>(R.id.box_crop_1_1)
    private val btn34Ref = findViewById<View>(R.id.box_crop_3_4)
    private val btn21Ref = findViewById<View>(R.id.box_crop_2_1)

    fun setupView(){
        container().show()

        btn11Ref.setOnClickListener {
            listener.onCropRatioClicked(RATIO_1_1)
        }

        btn34Ref.setOnClickListener {
            listener.onCropRatioClicked(RATIO_3_4)
        }

        btn21Ref.setOnClickListener {
            listener.onCropRatioClicked(RATIO_2_1)
        }
    }

    interface Listener{
        fun onCropRatioClicked(ratio: Float)
    }

    companion object{
        private const val RATIO_1_1 = 1f
        private const val RATIO_3_4 = 3/4f
        private const val RATIO_2_1 = 2/1f
    }
}