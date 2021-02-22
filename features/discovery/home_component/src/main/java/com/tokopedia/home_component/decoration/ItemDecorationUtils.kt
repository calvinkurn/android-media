package com.tokopedia.home_component.decoration

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by yfsx on 2/5/21.
 */

fun RecyclerView.clearDecorations() {
    if (itemDecorationCount > 0) {
        for (i in itemDecorationCount - 1 downTo 0) {
            removeItemDecorationAt(i)
        }
    }
}