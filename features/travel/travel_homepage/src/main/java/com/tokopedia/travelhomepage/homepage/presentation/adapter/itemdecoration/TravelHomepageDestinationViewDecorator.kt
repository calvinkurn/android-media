package com.tokopedia.travelhomepage.homepage.presentation.adapter.itemdecoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * @author by jessica on 2019-08-13
 */

class TravelHomepageDestinationViewDecorator(var spanSize: Int = 0) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.layoutManager!!.getPosition(view)
        if (spanSize == 0) {
            if (position % 2 == 0  && position != 0)
                outRect.left = 16
            else if (position % 2 == 1)
                outRect.right = 16
        } else if (spanSize == 2) {
            if (position % 2 == 0)
                outRect.right = 16
            else outRect.left = 16
        }
    }
}
