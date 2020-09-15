package com.tokopedia.topchat.chatsetting.view.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ChatSettingDividerItemDecoration(context: Context?) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable? = context?.let { ContextCompat.getDrawable(it, com.tokopedia.abstraction.R.drawable.bg_line_separator_thin) }

    interface Listener {
        fun shouldDrawDivider(): Boolean
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawVertical(c, parent)
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (!shouldDrawDivider(i, parent)) {
                continue
            }
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + parent.context.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_half)
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }

    private fun shouldDrawDivider(position: Int, parent: RecyclerView): Boolean {
        val vh = parent.findViewHolderForAdapterPosition(position)
        if (vh is Listener) {
            return vh.shouldDrawDivider()
        }
        return false
    }
}