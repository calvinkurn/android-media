package com.tokopedia.home_component.viewholders.layoutmanager

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class DynamicGridLayoutManager(context: Context) : GridLayoutManager(context, SPAN_FULL_SIZE) {

    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemCount = itemCount

                if (itemCount == 1) return SPAN_FULL_SIZE
                return if (itemCount == 2) {
                    SPAN_GRID_SIZE
                } else {
                    if (position == 0) SPAN_FULL_SIZE
                    else SPAN_GRID_SIZE
                }
            }
        }
    }

    /**
     * A dynamic padding decorator for widget.
     *
     * Due to the coupon-widget has a dynamic span rendering with follwing this criteria:
     * 1. If we only have a single data, then will be render a full width.
     * 2. If we have 2 data, then both of them will be rendered grid with [SPAN_GRID_SIZE] accordingly.
     * 3. If we have 2 data (max), then the first item will be render full-width, and the rest of the data,
     *    will be rendered grid with 2-cols-liked.
     *
     * Apart from that, the [AutomateCouponGridView] has also built-in padding, thus he have to
     * adjust it based on our needs. Hence, we need to this custom Decorator to apply the adjustment.
     */
    class Decorator : RecyclerView.ItemDecoration() {

        private var padding = 0

        init {
            padding = PADDING_DECORATOR.toPx()
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val itemCount = parent.adapter?.itemCount ?: 0
            val adapterPosition = parent.getChildAdapterPosition(view)

            // apply padding for full-width item type
            if (itemCount == 1) outRect.setOuterPadding()

            if (itemCount == 2) {
                // if has 2 items (grid-like item type), adjust the left and right padding accordingly
                outRect.setOuterTopAndBottomPadding()
                outRect.setDynamicLeftAndRightPadding { adapterPosition == 0 }
            }

            if (itemCount == 3) {
                // for the first item, set full-width padding item type
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.setOuterPadding()
                } else {
                    // the second and third items, apply grid-like padding adjustment accordingly
                    outRect.setOuterTopAndBottomPadding()
                    outRect.setDynamicLeftAndRightPadding { adapterPosition == 1 }
                }
            }
        }

        private fun Rect.setOuterPadding() {
            top = padding
            bottom = padding
            left = padding
            right = padding
        }

        private fun Rect.setOuterTopAndBottomPadding() {
            top = padding
            bottom = padding
        }

        private fun Rect.setDynamicLeftAndRightPadding(condition: () -> Boolean) {
            if (condition()) left = padding else right = padding
        }
    }

    companion object {
        // Layout Manager
        private const val SPAN_GRID_SIZE = 1
        private const val SPAN_FULL_SIZE = 2

        // Decorator
        private const val PADDING_DECORATOR = 4
    }
}
