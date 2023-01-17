package com.tokopedia.content.test.util

import android.view.View
import android.view.ViewGroup

/**
 * Created by kenny.hadisaputra on 21/03/22
 */
fun View.isSiblingWith(condition: (View) -> Boolean): Boolean {
    val parent = (this.parent as? ViewGroup) ?: return false
    return (0 until parent.childCount).any { index ->
        val child = parent.getChildAt(index)
        if (child == this) false
        else condition(child)
    }
}