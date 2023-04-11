package com.tokopedia.tokomember_seller_dashboard.view.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R

class TmMemberItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        val drawable = context.resources.getDrawable(R.drawable.basic_divider)
        val left = parent.left + parent.paddingLeft
        val right = parent.left + parent.width - parent.paddingRight
        val childCount = parent.childCount
        val startIndex = 0
        for(index in startIndex until childCount){
            val child = parent.getChildAt(index)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + drawable.intrinsicHeight
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
    }
}
