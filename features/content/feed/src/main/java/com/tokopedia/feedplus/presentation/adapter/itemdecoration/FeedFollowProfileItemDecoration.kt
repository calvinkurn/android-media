package com.tokopedia.feedplus.presentation.adapter.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.ui.itemdecoration.FocusedCarouselItemDecoration
import com.tokopedia.kotlin.extensions.view.getScreenWidth

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileItemDecoration(
    context: Context,
) : FocusedCarouselItemDecoration(context) {

    override val maxShrink: Float
        get() = 0.85f

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val last = parent.adapter?.itemCount ?: 0

        when (position) {
            0 -> outRect.left = calculateFirstAndLastItemOffset(view)
            last - 1 -> outRect.right = calculateFirstAndLastItemOffset(view)
            else -> {
                outRect.left = offset6
                outRect.right = offset6
            }
        }
    }

    private fun calculateFirstAndLastItemOffset(view: View): Int {
        return getScreenWidth().div(2) - view.layoutParams.width.div(2)
    }
}
