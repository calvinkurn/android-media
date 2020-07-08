package com.tokopedia.statistic.presentation.view.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.statistic.R

/**
 * Created By @ilhamsuaib on 08/07/20
 */

class StatisticItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val dp12 = view.context.resources.getDimension(R.dimen.shc_dimen_12dp)

        if (position == state.itemCount.minus(1)) {
            outRect.bottom = dp12.toInt()
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }
}