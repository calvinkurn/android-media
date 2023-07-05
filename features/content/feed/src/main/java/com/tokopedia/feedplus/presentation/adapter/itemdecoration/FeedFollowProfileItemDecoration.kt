package com.tokopedia.feedplus.presentation.adapter.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselItemDecoration

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileItemDecoration(
    context: Context,
) : PlayWidgetCarouselItemDecoration(context) {

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

        val offsetFirstAndLastItem = getScreenWidth().div(2) - view.layoutParams.width.div(2)

        when (position) {
            0 -> outRect.left = offsetFirstAndLastItem
            last - 1 -> outRect.right = offsetFirstAndLastItem
            else -> {
                outRect.left = offset6
                outRect.right = offset6
            }
        }
    }
}
