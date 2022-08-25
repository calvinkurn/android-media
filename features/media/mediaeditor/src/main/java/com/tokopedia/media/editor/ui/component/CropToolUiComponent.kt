package com.tokopedia.media.editor.ui.component

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class CropToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_crop) {

    fun setupView(editorParam: EditorParam?) {
        (container() as LinearLayout).apply {
            editorParam?.autoCropRatio?.let {
                addView(
                    generateCropButton(
                        it.getRatioX(),
                        it.getRatioY()
                    )
                )
            } ?: kotlin.run {
                editorParam?.ratioList?.forEach { ratio ->
                    addView(
                        generateCropButton(
                            ratio.getRatioX(),
                            ratio.getRatioY()
                        )
                    )
                }
            }
        }

        container().show()
    }

    private fun generateCropButton(widthRatio: Int, heightRatio: Int): View {
        return View.inflate(container().context, R.layout.ui_component_crop_action_layout, null)
            .apply {
                findViewById<View>(R.id.box_crop).apply {
                    val viewDefaultSize = DEFAULT_SIZE.toPx()
                    val limitSize = SIZE_LIMIT.toPx()

                    val ls = layoutParams

                    var width = viewDefaultSize * widthRatio
                    var height = viewDefaultSize * heightRatio

                    if (width > limitSize || height > limitSize) {
                        width /= 2
                        height /= 2
                    }

                    ls.width = width
                    ls.height = height
                }
                findViewById<Typography>(R.id.text_crop).text = "$widthRatio:$heightRatio"

                setOnClickListener {
                    listener.onCropRatioClicked(widthRatio / heightRatio.toFloat())
                }
            }
    }

    interface Listener {
        fun onCropRatioClicked(ratio: Float)
    }

    companion object {
        private const val DEFAULT_SIZE = 32
        private const val SIZE_LIMIT = DEFAULT_SIZE * 2
    }
}