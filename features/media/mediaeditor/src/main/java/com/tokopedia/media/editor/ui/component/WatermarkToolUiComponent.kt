package com.tokopedia.media.editor.ui.component

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.data.repository.WatermarkType
import com.tokopedia.picker.common.basecomponent.UiComponent

class WatermarkToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_watermark) {

    private val btnWatermarkDiagonal = findViewById<ImageView>(R.id.btn_watermark_diagonal)
    private val btnWatermarkCenter = findViewById<ImageView>(R.id.btn_watermark_center)

    fun setupView() {
        container().show()

        btnWatermarkDiagonal.setOnClickListener {
            listener.onWatermarkChanged(WatermarkType.Diagonal)
            setButtonSelected(it)
            releaseButtonSelected(btnWatermarkCenter)
        }

        btnWatermarkCenter.setOnClickListener {
            listener.onWatermarkChanged(WatermarkType.Center)
            setButtonSelected(it)
            releaseButtonSelected(btnWatermarkDiagonal)
        }
    }

    fun setWatermarkTypeSelected(watermarkType: WatermarkType?) {
        when (watermarkType) {
            WatermarkType.Diagonal -> setButtonSelected(btnWatermarkDiagonal)
            WatermarkType.Center -> setButtonSelected(btnWatermarkCenter)
            null -> {
                listener.onWatermarkChanged(WatermarkType.Diagonal)
                setButtonSelected(btnWatermarkDiagonal)
                releaseButtonSelected(btnWatermarkCenter)
            }
        }
    }

    fun getButtonRef(): Pair<ImageView, ImageView> {
        return Pair(btnWatermarkDiagonal, btnWatermarkCenter)
    }

    private fun setButtonSelected(view: View) {
        view.background =
            ContextCompat.getDrawable(context, R.drawable.editor_rect_green_selected_thumbnail)
    }

    private fun releaseButtonSelected(view: View) {
        view.background = null
    }

    interface Listener {
        fun onWatermarkChanged(type: WatermarkType)
    }
}