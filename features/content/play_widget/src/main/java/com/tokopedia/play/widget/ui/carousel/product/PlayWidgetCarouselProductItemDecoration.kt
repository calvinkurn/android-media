package com.tokopedia.play.widget.ui.carousel.product

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by kenny.hadisaputra on 16/05/23
 */
class PlayWidgetCarouselProductItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset12 = context.resources.getDimensionPixelOffset(
        R.dimen.play_widget_dp_12
    )

    private val offset8 = context.resources.getDimensionPixelOffset(
        unifyR.dimen.unify_space_8
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.left = offset12
        } else {
            outRect.left = offset8
        }

        val itemCount = parent.adapter?.itemCount ?: return
        if (position == itemCount - 1) {
            outRect.right = offset12
        } else {
            outRect.right = 0
        }
    }
}
