package com.tokopedia.product.manage.feature.list.view.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R

class ProductFilterItemDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)

        if(itemPosition > 0) {
            outRect.left = view.resources
                .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
        }
    }
}