package com.tokopedia.media.editor.ui.component

import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class CropToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, editorR.id.uc_tool_crop) {

    private val viewRefList = mutableListOf<Pair<View, Typography>>()

    private val activeColor = ContextCompat.getColor(this.context, unifyR.color.Unify_GN500)
    private val inactiveColor = ContextCompat.getColor(this.context, unifyR.color.Unify_NN950)

    fun setupView(editorParam: EditorParam?, detailUiModel: EditorDetailUiModel) {
        (container() as LinearLayout).apply {
            editorParam?.autoCropRatio?.let {
                addView(
                    generateCropButton(
                        it.getRatioX(),
                        it.getRatioY(),
                        true
                    )
                )
            } ?: kotlin.run {
                editorParam?.ratioList?.forEachIndexed { index, ratio ->
                    val isSelected = if(detailUiModel.cropRotateValue.getRatio() != null){
                        detailUiModel.cropRotateValue.getRatio()?.toInt() == ratio.getRatio().toInt()
                    } else (index == 0 && detailUiModel.isToolCrop())

                    addView(
                        generateCropButton(
                            ratio.getRatioX(),
                            ratio.getRatioY(),
                            isSelected
                        )
                    )
                }
            }
        }

        container().show()
    }

    private fun generateCropButton(widthRatio: Int, heightRatio: Int, isSelected: Boolean = false): View {
        return View.inflate(container().context, editorR.layout.ui_component_crop_action_layout, null)
            .apply {
                val boxCrop = findViewById<View>(editorR.id.box_crop).apply {
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

                val boxCropText = findViewById<Typography>(editorR.id.text_crop)
                boxCropText.text = "$widthRatio:$heightRatio"

                viewRefList.add(Pair(boxCrop, boxCropText))

                setOnClickListener {
                    setCropInactive()

                    listener.onCropRatioClicked(widthRatio / heightRatio.toFloat())

                    boxCrop.background.setColorFilter(activeColor, PorterDuff.Mode.SRC_ATOP)
                    boxCropText.setTextColor(activeColor)
                }

                if(isSelected) performClick()
            }
    }

    private fun setCropInactive(){
        viewRefList.forEach {
            it.first.background.setColorFilter(inactiveColor, PorterDuff.Mode.SRC_ATOP)
            it.second.setTextColor(inactiveColor)
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