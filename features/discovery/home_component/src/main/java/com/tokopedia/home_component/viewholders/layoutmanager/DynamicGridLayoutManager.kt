package com.tokopedia.home_component.viewholders.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class DynamicGridLayoutManager(context: Context) : GridLayoutManager(context, SPAN_FULL_SIZE) {

    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemCount = itemCount

                if (itemCount == 1) return SPAN_FULL_SIZE
                return if (itemCount == 2) {
                    SPAN_FULL_SIZE
                } else {
                    if (position == 0) SPAN_FULL_SIZE
                    else SPAN_GRID_SIZE
                }
            }
        }
    }

    companion object {
        private const val SPAN_GRID_SIZE = 1
        private const val SPAN_FULL_SIZE = 2
    }
}
