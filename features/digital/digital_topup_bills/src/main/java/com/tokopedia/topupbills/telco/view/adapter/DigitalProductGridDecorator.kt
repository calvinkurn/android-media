package com.tokopedia.topupbills.telco.view.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalProductGridDecorator(val space: Int, val resources: Resources) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect?.run {
            parent?.let {
                val childPosition = it.getChildAdapterPosition(view)
                val offset = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        space.toFloat(),
                        resources.displayMetrics
                ).toInt()
                if (childPosition == 0 || childPosition == 1) top = offset // Top row cells, add top offset
                if (childPosition % 2 == 0) {
                    right = offset  // Even index cell, add right offset
                    left = offset
                }
                bottom = offset
                right = offset
            }
        }

    }

}