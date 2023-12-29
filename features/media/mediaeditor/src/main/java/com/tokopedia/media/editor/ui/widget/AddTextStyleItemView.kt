package com.tokopedia.media.editor.ui.widget

import android.graphics.Color
import android.graphics.Typeface
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as principleR

class AddTextStyleItemView(
    val card: CardUnify2,
    val typography: Typography
) {
    init {
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.rightMargin = RIGHT_MARGIN.toPx()
        card.layoutParams = lp
    }

    fun setActive() {
        card.cardType = CardUnify2.TYPE_BORDER_ACTIVE
        typography.setBackgroundColor(Color.TRANSPARENT)
        typography.setTextColorCompat(principleR.color.Unify_GN400)
    }

    fun setInactive() {
        card.cardType = CardUnify2.TYPE_BORDER
        typography.setBackgroundColor(
            ContextCompat.getColor(card.context, principleR.color.Unify_NN0)
        )
        typography.setTextColorCompat(principleR.color.Unify_NN600)
    }

    fun setStyleBold() {
        typography.setTypeface(null, Typeface.BOLD)
    }

    fun setStyleItalic() {
        typography.setTypeface(null, Typeface.ITALIC)
    }

    companion object {
        private const val RIGHT_MARGIN = 16
    }
}
