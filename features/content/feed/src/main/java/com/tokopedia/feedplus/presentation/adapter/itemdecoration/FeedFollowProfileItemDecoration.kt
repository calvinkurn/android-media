package com.tokopedia.feedplus.presentation.adapter.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.ui.itemdecoration.FocusedCarouselItemDecoration
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.content.common.R as contentCommonR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileItemDecoration(
    context: Context,
) : FocusedCarouselItemDecoration(context) {

    override val maxShrink: Float = 0.85f

    override val roundedOffset: Int = context.resources.getDimensionPixelOffset(contentCommonR.dimen.content_common_space_6)

    override val overlayColor: Int = MethodChecker.getColor(context, unifyR.color.Unify_Static_Black)

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
