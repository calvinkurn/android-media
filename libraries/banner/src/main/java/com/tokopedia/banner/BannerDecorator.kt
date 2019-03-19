package com.tokopedia.banner

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by meta on 28/02/19.
 * Credit Devara Fikry
 */

class BannerDecorator(val leftFirst: Int,
                      val left: Int,
                      val rightLast: Int,
                      val right: Int) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager.getPosition(view) == 0) {
            outRect.left = leftFirst
        }
    }
}