package com.tokopedia.flight.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.R

/**
 * Created by Furqan on 06/10/2021.
 */
class FullDividerItemDecoration constructor(context: Context)
    : ItemDecoration() {

    private val mDivider: Drawable = MethodChecker.getDrawable(context, R.drawable.flight_bg_line_seperator)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}