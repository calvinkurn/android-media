package com.tokopedia.settingbank.view.decoration

import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.settingbank.view.viewHolder.BankTNCViewHolder
import com.tokopedia.unifycomponents.toPx

class DividerItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private const val BORDER_HEIGHT_DP = 1
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            // Skip drawing the border for the first item if it's the specific view holder
            if (i == Int.ZERO &&
                parent.getChildViewHolder(child) is BankTNCViewHolder ||
                i == childCount - Int.ONE
            ) {
                continue
            }

            val left = child.left - params.leftMargin
            val right = child.right + params.rightMargin
            val top = child.bottom + params.bottomMargin
            val bottom = top + BORDER_HEIGHT_DP.toPx()

            val paint = Paint()
            paint.color = ContextCompat.getColor(
                parent.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN100,
            )
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }
}
