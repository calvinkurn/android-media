package com.tokopedia.media.editor.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.editor.util.FontDetail
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as principleR

class AddTextStyleItemViewClone(context: Context, attributeSet: AttributeSet?): CardUnify2(context, attributeSet) {

    private var textContent: Typography? = null

    init {
        val lp = LinearLayout.LayoutParams(
            40.toPx(),
            40.toPx()
        )
        lp.rightMargin = RIGHT_MARGIN.toPx()
        layoutParams = lp
        radius = 40f.toPx()

        textContent = Typography(context).apply {
            text = "Aa"
            gravity = Gravity.CENTER
        }

        setInactive()
        addView(textContent)
    }

    fun setActive() {
        textContent?.let {
            it.setBackgroundColor(
                resources.getColor(principleR.color.Unify_NN0)
            )
            it.setTextColorCompat(principleR.color.Unify_NN950)
        }
    }

    fun setInactive() {
        textContent?.let {
            it.setBackgroundColor(
                resources.getColor(principleR.color.Unify_NN900)
            )
            it.setTextColorCompat(principleR.color.Unify_NN0)
        }
    }

    fun setFont(font: FontDetail) {
        textContent?.setTypeface(null, font.fontStyle)
    }

    companion object {
        private const val RIGHT_MARGIN = 16
    }
}
