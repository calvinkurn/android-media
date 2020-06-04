package com.tokopedia.loyalty.view

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.unifycomponents.UnifyButton

fun GlobalError.addButton(text: String): View {
    val unifyButton = UnifyButton(context)
    val width = resources.getDimension(com.tokopedia.design.R.dimen.dp_220).toInt()
    val height = LinearLayout.LayoutParams.WRAP_CONTENT
    val lp = LinearLayout.LayoutParams(width, height)
    val margin = resources.getDimension(com.tokopedia.design.R.dimen.dp_16).toInt()
    val topMargin = resources.getDimension(com.tokopedia.design.R.dimen.dp_8).toInt()
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
