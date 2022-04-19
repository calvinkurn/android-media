package com.tokopedia.home_recom.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_recom.view.viewholder.ProductInfoViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationErrorViewHolder

/**
 * Created by Lukas on 08/10/20.
 */

class StaggerGridSpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val halfSpace = spacing / 2
        val viewHolder = parent.getChildViewHolder(view)
        if(viewHolder is RecommendationErrorViewHolder){
            outRect.top = 150
        } else if(viewHolder !is ProductInfoViewHolder) {
            if (parent.paddingLeft != halfSpace) {
                parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace)
                parent.clipToPadding = false
            }
            outRect.top = halfSpace
            outRect.bottom = halfSpace
            outRect.left = halfSpace
            outRect.right = halfSpace
        }
    }
}