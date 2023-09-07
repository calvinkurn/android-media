package com.tokopedia.editor.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.tokopedia.editor.util.FontDetail
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as principleR
import com.tokopedia.editor.R as editorR

class InputTextStyleItemView(context: Context, attributeSet: AttributeSet?): CardUnify2(context, attributeSet) {

    private var textContent: Typography? = null
    private var isActive: Boolean = false

    init {
        val lp = LinearLayout.LayoutParams(
            40.toPx(),
            40.toPx()
        )
        lp.rightMargin = RIGHT_MARGIN.toPx()
        layoutParams = lp
        radius = 40f.toPx()

        textContent = Typography(context).apply {
            text = resources.getString(editorR.string.universal_editor_input_text_style_text)
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
        isActive = true
    }

    fun setInactive() {
        textContent?.let {
            it.setBackgroundColor(
                resources.getColor(principleR.color.Unify_NN900)
            )
            it.setTextColorCompat(principleR.color.Unify_NN0)
        }
        isActive = false
    }

    fun setFont(font: FontDetail) {
        val typeFace = Typeface.createFromAsset(context.assets, font.fontName)
        textContent?.setTypeface(typeFace, font.fontStyle)
    }

    fun isActive(): Boolean {
        return isActive
    }

    companion object {
        private const val RIGHT_MARGIN = 16
    }
}
