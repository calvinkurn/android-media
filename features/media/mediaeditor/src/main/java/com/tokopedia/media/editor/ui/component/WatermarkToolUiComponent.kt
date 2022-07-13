package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.unifycomponents.ImageUnify

class WatermarkToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_watermark) {

    private val buttonType1 = findViewById<ImageUnify>(R.id.watermark_type1)
    private val buttonType2 = findViewById<ImageUnify>(R.id.watermark_type2)

    fun setupView() {
        container().show()


        buttonType1.setOnClickListener {
            listener.onWatermarkChanged(WATERMARK_TOKOPEDIA)
        }

        buttonType2.setOnClickListener {
            listener.onWatermarkChanged(WATERMARK_SHOP)
        }
    }

    fun getButtonRef(): Pair<ImageView, ImageView> {
        return Pair(buttonType1, buttonType2)
    }

    interface Listener {
        fun onWatermarkChanged(value: Int)
    }

    companion object {
        const val WATERMARK_TOKOPEDIA = 0
        const val WATERMARK_SHOP = 1
    }
}