package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.component.slider.MediaEditorSlider
import com.tokopedia.picker.common.basecomponent.UiComponent

class CropToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Int
) : UiComponent(viewGroup, R.id.uc_tool_crop) {

    fun setupView(){
        container().show()
    }

}