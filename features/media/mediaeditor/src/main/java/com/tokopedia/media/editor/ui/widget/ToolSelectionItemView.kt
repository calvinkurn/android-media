package com.tokopedia.media.editor.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.databinding.ToolSelectionItemLayoutBinding
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography

@SuppressLint("ResourcePackage")
class ToolSelectionItemView : ConstraintLayout {
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context) : super(context)

    private var viewBinding = ToolSelectionItemLayoutBinding.inflate(LayoutInflater.from(context))

    init {
        addView(viewBinding.root)
    }

    fun setTextTitle(textRef: Int) {
        viewBinding.toolSelectionItemText.setText(textRef)
    }

    fun setIcon(iconId: Int) {
        viewBinding.toolSelectionItemIcon.apply {
            visible()
            setImage(iconId)
        }
    }

    fun setImage(imageRef: Int, isFull: Boolean = false) {
        viewBinding.toolSelectionItemImage.apply {
            visible()
            if (isFull) {
                val lp = layoutParams
                lp.width = LinearLayout.LayoutParams.MATCH_PARENT
                lp.height = LinearLayout.LayoutParams.MATCH_PARENT
                layoutParams = lp
            }
            setImageResource(imageRef)
        }
    }

    fun setListener(listener: () -> Unit) {
        viewBinding.toolSelectionItemBorder.setOnClickListener {
            listener()
        }

        viewBinding.toolSelectionItemText.setOnClickListener {
            listener()
        }
    }

    fun setActive() {
        viewBinding.toolSelectionItemBorder.changeTypeWithTransition(CardUnify2.TYPE_BORDER_ACTIVE)
    }

    fun setInactive() {
        viewBinding.toolSelectionItemBorder.changeTypeWithTransition(CardUnify2.TYPE_BORDER)
    }
}
