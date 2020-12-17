package com.tokopedia.shop.search.widget

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.R
import com.tokopedia.shop.search.view.adapter.viewholder.ShopSearchProductFixResultViewHolder

class ShopSearchProductDividerItemDecoration(
        private val dividerDrawable: Drawable
) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.adapter?.apply {
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val left = if (getItemViewType(i) == ShopSearchProductFixResultViewHolder.LAYOUT) {
                    parent.context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
                } else {
                    parent.context.resources.getDimensionPixelOffset(R.dimen.dp_72)
                }
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + parent.context.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_half)
                dividerDrawable.setBounds(left, top, right, bottom)
                dividerDrawable.draw(c)
            }
        }
    }

}