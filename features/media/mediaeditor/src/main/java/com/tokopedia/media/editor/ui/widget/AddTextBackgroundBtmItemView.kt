package com.tokopedia.media.editor.ui.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.databinding.AddTextBackgroundBtmItemLayoutBinding
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_FULL
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_FLOATING
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_SIDE_CUT
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_BLACK
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_WHITE
import com.tokopedia.media.editor.utils.AddTextColorProvider
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.unifyprinciples.R as principleR

class AddTextBackgroundBtmItemView constructor(
    context: Context,
    attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {

    private var viewBinding: AddTextBackgroundBtmItemLayoutBinding

    init {
        viewBinding = AddTextBackgroundBtmItemLayoutBinding.inflate(LayoutInflater.from(context))
        addView(viewBinding.root)
    }

    fun setImage(url: String) {
        viewBinding.btmItemImg.loadImage(url)
    }

    fun setActive() {
        (viewBinding.btmItemConstraint.background as GradientDrawable).apply {
            mutate()
            setStroke(2.toPx(), ContextCompat.getColor(context, principleR.color.Unify_GN500))
        }

        viewBinding.btmItemChecklist.show()
    }

    fun setInactive() {
        (viewBinding.btmItemConstraint.background as GradientDrawable).apply {
            mutate()
            setStroke(2.toPx(), ContextCompat.getColor(context, principleR.color.Unify_NN300))
        }

        viewBinding.btmItemChecklist.hide()
    }

    fun setBackgroundModel(
        backgroundTemplate: Int,
        backgroundColor: Int = TEXT_BACKGROUND_TEMPLATE_BLACK,
        colorProvider: AddTextColorProvider
    ) {
        when (backgroundTemplate) {
            TEXT_BACKGROUND_TEMPLATE_FULL -> editorR.drawable.add_text_background_full
            TEXT_BACKGROUND_TEMPLATE_FLOATING -> {
                val margin = 4.toPx()
                viewBinding.btmItemBackground.setMargin(margin, 0, margin, margin)
                editorR.drawable.add_text_background_floating
            }

            TEXT_BACKGROUND_TEMPLATE_SIDE_CUT -> editorR.drawable.add_text_background_cut
            else -> 0
        }.let {
            if (it == 0) return

            // define is background color for item preview
            if (backgroundColor == TEXT_BACKGROUND_TEMPLATE_WHITE) {
                ContextCompat.getDrawable(context, it)?.let { backgroundDrawable ->
                    colorProvider.drawableToWhite(backgroundDrawable)
                    viewBinding.btmItemBackground.background = backgroundDrawable
                }
            } else {
                viewBinding.btmItemBackground.setBackgroundResource(it)
            }
        }
    }
}
