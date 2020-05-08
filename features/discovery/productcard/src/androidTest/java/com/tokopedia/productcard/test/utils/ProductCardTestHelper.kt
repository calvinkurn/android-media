package com.tokopedia.productcard.test.utils

import android.view.View
import android.view.ViewGroup

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
    if (child is ViewGroup) {
        this.addAll(child.getChildren())
    }
    else if (child != null) {
        this.add(child)
    }
}