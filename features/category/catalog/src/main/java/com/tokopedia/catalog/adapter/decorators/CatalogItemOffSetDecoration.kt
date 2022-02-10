package com.tokopedia.catalog.adapter.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toPx


/** for Staggered
 * space in px
 **/
class CatalogItemOffSetDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)
        (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).leftMargin = 0
        (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).rightMargin = 0
        (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).topMargin = 0
        (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).bottomMargin = 0
        val position = parent.getChildAdapterPosition(view)
        if(position < 0)
            return
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        if (spanIndex.isZero()) {
            //settings for left column
            var top = 0
            if (position < 2) top = parent.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toPx().toInt()
            val left = parent.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toPx().toInt()
            (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).leftMargin = left
            (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).topMargin = top
        } else {
            //settings for right column
            var top = 0
            if (position < 2) top = parent.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toPx().toInt()
            val right = parent.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toPx().toInt()
            (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).rightMargin = right
            (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).topMargin = top
        }
    }
}