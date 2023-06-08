package com.tokopedia.media.editor.ui.fragment.bottomsheet.addtextbackground

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_FULL
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_FLOATING
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_SIDE_CUT
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_BLACK
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_BACKGROUND_TEMPLATE_WHITE
import com.tokopedia.media.editor.utils.toWhite
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.unifyprinciples.R as principleR

class AddTextBackgroundBtmItem(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var mImageRef: AppCompatImageView? = null
    private var mItemConstraint: ConstraintLayout? = null
    private var mItemChecklist: View? = null
    private var mItemBackground: View? = null

    init {
        View.inflate(context, editorR.layout.add_text_background_btm_item_layout, this)?.apply {
            mImageRef = findViewById(editorR.id.btm_item_img)
            mItemConstraint = findViewById(editorR.id.btm_item_constraint)
            mItemChecklist = findViewById(editorR.id.btm_item_checklist)
            mItemBackground = findViewById(editorR.id.btm_item_background)
        }
    }

    fun setImage(url: String) {
        mImageRef?.loadImage(url)
    }

    fun setClickListener(listener: () -> Unit) {
        mItemConstraint?.setOnClickListener {
            listener()
        }
    }

    fun setActive() {
        mItemConstraint?.let {
            (it.background as GradientDrawable).apply {
                mutate()
                setStroke(2.toPx(), ContextCompat.getColor(context, principleR.color.Unify_GN500))
            }
        }

        mItemChecklist?.show()
    }

    fun setInactive() {
        mItemConstraint?.let {
            (it.background as GradientDrawable).apply {
                mutate()
                setStroke(2.toPx(), ContextCompat.getColor(context, principleR.color.Unify_NN300))
            }
        }

        mItemChecklist?.hide()
    }

    fun setBackgroundModel(backgroundTemplate: Int, backgroundColor: Int = TEXT_BACKGROUND_TEMPLATE_BLACK) {
        when(backgroundTemplate) {
            TEXT_BACKGROUND_TEMPLATE_FULL -> editorR.drawable.add_text_background_full
            TEXT_BACKGROUND_TEMPLATE_FLOATING -> {
                val margin = 4.toPx()
                mItemBackground?.setMargin(margin, 0,margin,margin)
                editorR.drawable.add_text_background_floating
            }
            TEXT_BACKGROUND_TEMPLATE_SIDE_CUT -> editorR.drawable.add_text_background_cut
            else -> 0
        }.let {
            if (it == 0) return

            // define is background color for item preview
            if (backgroundColor == TEXT_BACKGROUND_TEMPLATE_WHITE) {
                ContextCompat.getDrawable(context, it)?.let { backgroundDrawable ->
                    backgroundDrawable.toWhite()
                    mItemBackground?.background = backgroundDrawable
                }
            } else {
                mItemBackground?.setBackgroundResource(it)
            }
        }
    }
}
