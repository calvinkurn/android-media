package com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.R
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by kenny.hadisaputra on 17/10/23
 */
class HorizontalStoriesItemDecoration(
    resources: Resources
) : RecyclerView.ItemDecoration() {

    private val offset10 = resources.getDimensionPixelOffset(
        R.dimen.feed_space_10
    )

    private val offset16 = resources.getDimensionPixelOffset(
        unifyprinciplesR.dimen.spacing_lvl4
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)

        if (position == 0) {
            outRect.left = offset16
        } else {
            outRect.left = offset10
        }

        if (position == state.itemCount - 1) {
            outRect.right = offset16
        }
    }
}
