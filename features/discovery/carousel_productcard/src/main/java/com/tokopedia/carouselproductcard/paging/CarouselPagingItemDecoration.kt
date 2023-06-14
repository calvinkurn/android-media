package com.tokopedia.carouselproductcard.paging

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

internal class CarouselPagingItemDecoration(
    context: Context,
    private val paddingHorizontalPx: Int,
    private val itemPerPage: Int,
): DividerItemDecoration(context, VERTICAL) {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val absolutePosition = parent.getChildAdapterPosition(view)

        outRect.left = paddingHorizontalPx / (if (absolutePosition < itemPerPage) 1 else 2)
        outRect.right = paddingHorizontalPx / 2
        outRect.bottom = 1.toPx()
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val drawable = drawable ?: return
        canvas.save()

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
        canvas.restore()
    }

    companion object {
        fun createDrawable(context: Context): Drawable {
            return GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE

                val colorRes = com.tokopedia.unifycomponents.R.color.Unify_NN50
                setColor(ContextCompat.getColor(context, colorRes))
                setSize(1.toPx(), 1.toPx())
            }
        }
    }
}
