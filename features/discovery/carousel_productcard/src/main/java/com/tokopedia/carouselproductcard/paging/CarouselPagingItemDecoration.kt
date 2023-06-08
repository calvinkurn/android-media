package com.tokopedia.carouselproductcard.paging

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

internal class CarouselPagingItemDecoration(
    private val context: Context,
): DividerItemDecoration(context, VERTICAL) {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        (0 until parent.childCount).forEach { index ->
            val view = parent.getChildAt(index)

            drawable.apply {
                val left = view.left + 8.toPx()
                val top = view.bottom + 8.toPx()
                val right = view.right - 8.toPx()
                val bottom = view.bottom + 9.toPx()
                bounds = Rect(left, top, right, bottom)
                draw(canvas)
            }
        }
    }

    override fun getDrawable(): Drawable {
        val colorRes = com.tokopedia.unifycomponents.R.color.Unify_NN50
        val color = ContextCompat.getColor(context, colorRes)
        return ColorDrawable(color)
    }
}
