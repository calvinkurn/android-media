package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */
class ProductTagItemDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val startEndOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)
    private val inBetweenOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl3)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()

        outRect.left = if (position <= 0) startEndOffset
        else inBetweenOffset

        if (position == itemCount - 1) outRect.right = startEndOffset
    }
}