package com.tokopedia.sellerorder.detail.presentation.adapter

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.sellerorder.R

class SomDetailItemDecoration : RecyclerView.ItemDecoration() {
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        if (childCount > 0) {
            val drawable = MethodChecker.getDrawable(parent.context, R.drawable.som_detail_divider)
            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight
            val child = parent.getChildAt(childCount - 1)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + drawable.intrinsicHeight

            drawable.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            drawable.draw(canvas)
        }
    }
}