package com.tokopedia.notifcenter.presentation.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifcenter.R
import androidx.core.content.ContextCompat
import com.tokopedia.notifcenter.presentation.adapter.NotificationAdapter

class NotificationItemDecoration(context: Context?) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable? = context?.let {
        ContextCompat.getDrawable(it, R.drawable.bg_notifcenter_line_separator_thin)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawVertical(c, parent)
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent
                .resources
                .getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8)
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            if (!shouldDrawDivider(parent, child)) {
                continue
            }
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + parent
                    .context
                    .resources
                    .getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_half)
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }

    private fun shouldDrawDivider(parent: RecyclerView, childView: View): Boolean {
        val dataPosition = parent.getChildAdapterPosition(childView)
        val adapter = parent.adapter as? NotificationAdapter ?: return true
        return adapter.shouldDrawDivider(dataPosition)
    }
}