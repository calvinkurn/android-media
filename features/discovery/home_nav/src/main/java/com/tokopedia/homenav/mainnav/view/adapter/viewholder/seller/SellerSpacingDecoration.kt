package com.tokopedia.homenav.mainnav.view.adapter.viewholder.seller

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery.common.utils.toDpInt

/**
 * Created by dhaba
 */
class SellerSpacingDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 12f.toDpInt()
        }
        else if (parent.getChildAdapterPosition(view) == state.itemCount-1) {
            outRect.right = 12f.toDpInt()
        }
    }
}