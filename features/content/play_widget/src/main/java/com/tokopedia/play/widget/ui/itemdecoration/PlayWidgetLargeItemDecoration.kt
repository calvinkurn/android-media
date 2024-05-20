package com.tokopedia.play.widget.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by astidhiyaa on 4/17/24
 */
class PlayWidgetLargeItemDecoration(ctx: Context) : RecyclerView.ItemDecoration() {

    private val padding =
        ctx.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.unify_space_8)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = padding
        outRect.bottom = padding
        outRect.right = padding
    }
}
