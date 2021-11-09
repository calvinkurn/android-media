package com.tokopedia.productcard.test.utils

import android.view.View
import android.view.ViewGroup
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.QuantityEditorUnify

internal fun ViewGroup.getChildren(): List<View> {
    val children = mutableListOf<View>()

    forAllChild {
        children.addChildView(it)
    }

    return children
}

private fun ViewGroup.forAllChild(action: (View?) -> Unit) {
    for(i in 0..childCount) {
        val child: View? = getChildAt(i)
        action(child)
    }
}

private fun MutableList<View>.addChildView(child: View?) {
    child ?: return

    this.add(child)

    if (child is ViewGroup
            && child !is ProgressBarUnify
            && child !is QuantityEditorUnify)
        this.addAll(child.getChildren())
}