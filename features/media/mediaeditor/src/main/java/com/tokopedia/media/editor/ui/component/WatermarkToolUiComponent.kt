package com.tokopedia.media.editor.ui.component

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.media.editor.R.dimen as dimenR

class WatermarkToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_watermark) {

    private val buttonType1 = findViewById<ImageView>(R.id.watermark_type1)
    private val buttonType2 = findViewById<ImageView>(R.id.watermark_type2)

    fun setupView() {
        container().show()

        buttonType1.setOnClickListener {
            listener.onWatermarkChanged(WATERMARK_TOKOPEDIA)
            setButtonSelected(it)
            releaseButtonSelected(buttonType2)
        }

        buttonType2.setOnClickListener {
            listener.onWatermarkChanged(WATERMARK_SHOP)
            setButtonSelected(it)
            releaseButtonSelected(buttonType1)
        }
    }

    fun getButtonRef(): Pair<ImageView, ImageView> {
        return Pair(buttonType1, buttonType2)
    }

    private fun setButtonSelected(view: View) {
        view.background = ContextCompat.getDrawable(context, R.drawable.editor_rect_green_selected_thumbnail)
    }

    private fun releaseButtonSelected(view: View) {
        view.background = null
    }

    interface Listener {
        fun onWatermarkChanged(value: Int)
    }

    companion object {
        const val WATERMARK_TOKOPEDIA = 0
        const val WATERMARK_SHOP = 1
    }
}