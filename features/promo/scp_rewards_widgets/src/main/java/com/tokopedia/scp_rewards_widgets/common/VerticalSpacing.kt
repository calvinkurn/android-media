package com.tokopedia.scp_rewards_widgets.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class VerticalSpacing(
    private val space:Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect){
            val itemPos = parent.getChildAdapterPosition(view)
            if(itemPos!=0){
                top = space.toPx()
            }
        }
    }
}
