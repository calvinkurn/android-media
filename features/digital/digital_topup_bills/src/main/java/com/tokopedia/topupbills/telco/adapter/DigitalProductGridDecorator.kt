package com.tokopedia.topupbills.telco.adapter

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalProductGridDecorator(val space: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.run {
            this.left = space
            this.right = space
            this.bottom = space

            parent?.run {
                if (this.getChildLayoutPosition(view) == 0) {
                    outRect.top = space
                } else {
                    outRect.top = 0
                }
            }
        }

    }

}