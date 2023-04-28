package com.tokopedia.media.editor.ui.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.media.editor.R as editorR

class AddTextLatarBtmItem(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var mImageRef: AppCompatImageView? = null
    private var mItemConstraint: ConstraintLayout? = null
    private var mItemChecklist: View? = null

    init {
        View.inflate(context, editorR.layout.add_text_latar_btm_item, this)?.apply {
            mImageRef = findViewById(editorR.id.btm_item_img)
            mItemConstraint = findViewById(editorR.id.btm_item_constraint)
            mItemChecklist = findViewById(editorR.id.btm_item_checklist)
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
}
