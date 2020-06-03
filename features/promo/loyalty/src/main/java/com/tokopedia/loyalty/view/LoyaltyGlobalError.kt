package com.tokopedia.loyalty.view

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx

fun GlobalError.addButton(text:String): View {
    val unifyButton = UnifyButton(context)
    val width = 220.toPx()
    val height = LinearLayout.LayoutParams.WRAP_CONTENT
    val lp = LinearLayout.LayoutParams(width, height)
    val margin = 16.toPx()
    val topMargin = 8.toPx()
    lp.setMargins(margin, topMargin, margin, margin)
    unifyButton.text = text
    unifyButton.buttonSize = UnifyButton.Size.LARGE
    unifyButton.buttonVariant = UnifyButton.Variant.GHOST
    unifyButton.layoutParams = lp
    if (errorAction.parent is LinearLayout) {
        (errorAction.parent as LinearLayout).addView(unifyButton)
    }
    return unifyButton
}
