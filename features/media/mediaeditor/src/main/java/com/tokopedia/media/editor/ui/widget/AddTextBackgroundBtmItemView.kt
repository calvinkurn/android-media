package com.tokopedia.media.editor.ui.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.databinding.AddTextBackgroundBtmItemLayoutBinding
import com.tokopedia.media.editor.data.entity.AddTextBackgroundTemplate
import com.tokopedia.media.editor.data.AddTextColorProvider
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
            setStroke(BORDER_SIZE.toPx(), ContextCompat.getColor(context, principleR.color.Unify_GN500))
        }

        viewBinding.btmItemChecklist.show()
    }

    fun setInactive() {
        (viewBinding.btmItemConstraint.background as GradientDrawable).apply {
            mutate()
            setStroke(BORDER_SIZE.toPx(), ContextCompat.getColor(context, principleR.color.Unify_NN300))
        }

        viewBinding.btmItemChecklist.hide()
    }

    fun setBackgroundModel(
        backgroundTemplate: Int,
        @ColorInt backgroundColor: Int,
        colorProvider: AddTextColorProvider
    ) {
        when (backgroundTemplate) {
            AddTextBackgroundTemplate.FULL.value -> editorR.drawable.add_text_background_full
            AddTextBackgroundTemplate.FLOATING.value -> {
                val margin = TEXT_BACKGROUND_FLOATING_MARGIN.toPx()
                viewBinding.btmItemBackground.setMargin(margin, 0, margin, margin)
                editorR.drawable.add_text_background_floating
            }

            AddTextBackgroundTemplate.SIDE_CUT.value -> editorR.drawable.add_text_background_cut
            else -> 0
        }.let {
            if (it == 0) return

            ContextCompat.getDrawable(context, it)?.let { backgroundDrawable ->
                colorProvider.implementDrawableColor(backgroundDrawable, backgroundColor)
                viewBinding.btmItemBackground.background = backgroundDrawable
            }
        }
    }

    companion object {
        private const val TEXT_BACKGROUND_FLOATING_MARGIN = 4
        private const val BORDER_SIZE = 2
    }
}
