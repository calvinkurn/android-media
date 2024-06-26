package com.tokopedia.tokopedianow.home.presentation.decoration

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeSharingWidgetViewHolder
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class HomeSpacingDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private const val FIRST_ITEM_POSITION = 1
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if(position == RecyclerView.NO_POSITION) return

        val dimen0dp = parent.context.resources.getDimensionPixelSize(
            unifyprinciplesR.dimen.unify_space_0)
        val dimen4dp = parent.context.resources.getDimensionPixelSize(
            unifyprinciplesR.dimen.unify_space_4)
        val dimen8dp = parent.context.resources.getDimensionPixelSize(
            unifyprinciplesR.dimen.unify_space_8)
        val dimen16dp = parent.context.resources.getDimensionPixelSize(
            unifyprinciplesR.dimen.unify_space_16)

        when (val viewHolder = parent.findViewHolderForLayoutPosition(position)) {
            is BannerComponentViewHolder -> {
                viewHolder.itemView.setLayoutParams {
                    if(position == FIRST_ITEM_POSITION) {
                        it.topMargin = -dimen16dp
                    } else {
                        it.topMargin = -dimen4dp
                        it.bottomMargin = dimen4dp
                    }
                }
            }
            is HomeSharingWidgetViewHolder -> {
                viewHolder.itemView.setLayoutParams { it.bottomMargin = -dimen8dp }
            }
            else -> {
                if(position == FIRST_ITEM_POSITION) {
                    viewHolder?.itemView.setLayoutParams { it.topMargin = dimen0dp }
                } else {
                    viewHolder?.itemView.setLayoutParams { it.bottomMargin = dimen0dp }
                }
            }
        }
    }

    private fun View?.setLayoutParams(block: (MarginLayoutParams) -> Unit) {
        this?.let {
            val layoutParams = it.layoutParams
            val rvLayoutParams = layoutParams as? MarginLayoutParams

            it.layoutParams = rvLayoutParams?.apply {
                block.invoke(this)
            }
        }
    }
}
