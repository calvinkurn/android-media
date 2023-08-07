package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
class BeautificationOptionItemDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val offset8 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()

        outRect.apply {
            when(position) {
                INITIAL_POSITION_INDEX -> left = offset16
                itemCount - 1 -> {
                    left = offset8
                    right = offset16
                }
                else -> left = offset8
            }
        }
    }

    companion object {
        private const val INITIAL_POSITION_INDEX = 0
    }
}
