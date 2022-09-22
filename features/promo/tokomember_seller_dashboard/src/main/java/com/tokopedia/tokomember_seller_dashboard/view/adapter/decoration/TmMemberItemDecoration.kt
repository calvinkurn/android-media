package com.tokopedia.tokomember_seller_dashboard.view.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R

class TmMemberItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val drawable = context.resources.getDrawable(R.drawable.basic_divider)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val count = parent.childCount
        for(idx in 0 until count){
            val child = parent.getChildAt(idx)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + drawable.intrinsicHeight
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(c)
        }
    }
}