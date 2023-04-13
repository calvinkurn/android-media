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
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.editor.R
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography

@SuppressLint("ResourcePackage")
class ToolSelectionItem: ConstraintLayout {
    private var mTextRef: Typography? = null
    private var mImgRef: AppCompatImageView? = null
    private var mIconRef: IconUnify? = null
    private var mCardRef: CardUnify2? = null

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context) : super(context)

    init {
        View.inflate(context, R.layout.tool_selecction_item_layout, this)
        mTextRef = findViewById(R.id.tool_selection_item_text)
        mImgRef = findViewById(R.id.tool_selection_item_image)
        mIconRef = findViewById(R.id.tool_selection_item_icon)
        mCardRef = findViewById(R.id.tool_selection_item_border)
    }

    fun setTextTitle(textRef: Int) {
        mTextRef?.setText(textRef)
    }

    fun setIcon(iconId: Int) {
        mIconRef?.apply {
            visible()
            setImage(iconId)
        }
    }

    fun setImage(imageRef: Int, isFull: Boolean = false) {
        mImgRef?.apply {
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
        mCardRef?.setOnClickListener {
            listener()
        }

        mTextRef?.setOnClickListener {
            listener()
        }
    }

    fun setActive() {
        mCardRef?.changeTypeWithTransition(CardUnify2.TYPE_BORDER_ACTIVE)
    }

    fun setInactive() {
        mCardRef?.changeTypeWithTransition(CardUnify2.TYPE_BORDER)
    }
}
