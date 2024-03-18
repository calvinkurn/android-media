package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.thankyou_native.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class BulletsView(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {
    init {
        orientation = VERTICAL
    }

    fun setText(textArr: List<String>) {
        removeAllViews()
        textArr.forEach {
            val parent = LinearLayout(context)
            parent.orientation = HORIZONTAL
            val bulletView = ImageView(context)
            val lp = LayoutParams(6.toPx(), 6.toPx())
            lp.setMargins(0, 6.toPx(), 0, 0)
            bulletView.layoutParams = lp
            bulletView.setImageResource(R.drawable.bullet)
            if (textArr.size > 1) {
                parent.addView(bulletView)
            }
            val textView = Typography(context)
            textView.setType(Typography.PARAGRAPH_3)
            textView.text = HtmlLinkHelper(context, it).spannedString
            textView.setPadding(if (textArr.size > 1) 8.toPx() else 0, 0, 0, 4.toPx())
            parent.addView(textView)
            addView(parent)
        }
    }

}
