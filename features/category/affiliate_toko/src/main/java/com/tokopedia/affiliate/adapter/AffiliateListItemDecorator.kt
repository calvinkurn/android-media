package com.tokopedia.affiliate.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toPx


class AffiliateListItemDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)
        with(outRect){
            val lastItem = parent.adapter?.itemCount?.minus(1) ?: 0
            val adapterPosition = parent.getChildAdapterPosition(view)
            if( lastItem == adapterPosition){
                bottom = parent.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_24).toPx()
                        .toInt()
            }
        }
    }
}