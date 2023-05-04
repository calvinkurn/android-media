package com.tokopedia.media.editor.ui.widget

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_FULL
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_FLOATING
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_SIDE_CUT
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_BLACK
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_WHITE
import com.tokopedia.media.editor.utils.toWhite
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.media.editor.R as editorR

class AddTextLatarBtmItem(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var mImageRef: AppCompatImageView? = null
    private var mItemConstraint: ConstraintLayout? = null
    private var mItemChecklist: View? = null
    private var mItemLatar: View? = null

    init {
        View.inflate(context, editorR.layout.add_text_latar_btm_item, this)?.apply {
            mImageRef = findViewById(editorR.id.btm_item_img)
            mItemConstraint = findViewById(editorR.id.btm_item_constraint)
            mItemChecklist = findViewById(editorR.id.btm_item_checklist)
            mItemLatar = findViewById(editorR.id.btm_item_latar)
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
                setStroke(2.toPx(), ContextCompat.getColor(context, editorR.color.Unify_GN500))
            }
        }

        mItemChecklist?.show()
    }

    fun setInactive() {
        mItemConstraint?.let {
            (it.background as GradientDrawable).apply {
                mutate()
                setStroke(2.toPx(), ContextCompat.getColor(context, editorR.color.Unify_NN300))
            }
        }

        mItemChecklist?.hide()
    }

    fun setLatarModel(latarTemplate: Int, latarColor: Int = TEXT_LATAR_TEMPLATE_BLACK) {
        when(latarTemplate) {
            TEXT_LATAR_TEMPLATE_FULL -> editorR.drawable.add_text_latar_full
            TEXT_LATAR_TEMPLATE_FLOATING -> {
                val margin = 4.toPx()
                mItemLatar?.setMargin(margin, 0,margin,margin)
                editorR.drawable.add_text_latar_floating
            }
            TEXT_LATAR_TEMPLATE_SIDE_CUT -> editorR.drawable.add_text_latar_cut
            else -> 0
        }.let {
            if (it == 0) return

            // define is latar color for item preview
            if (latarColor == TEXT_LATAR_TEMPLATE_WHITE) {
                ContextCompat.getDrawable(context, it)?.let { latarBg ->
                    latarBg.toWhite()
                    mItemLatar?.background = latarBg
                }
            } else {
                mItemLatar?.setBackgroundResource(it)
            }
        }
    }
}
