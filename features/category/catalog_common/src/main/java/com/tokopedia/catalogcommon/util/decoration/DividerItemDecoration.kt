package com.tokopedia.catalogcommon.util.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

class DividerItemDecoration(
    context: Context,
    dividerDrawableResId: Int,
    private val verticalMargin: Int
    ) : RecyclerView.ItemDecoration() {

    private val dividerDrawable: Drawable? =
        ContextCompat.getDrawable(context, dividerDrawableResId)


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + dividerDrawable?.intrinsicWidth.orZero()

            val dividerTop = top + verticalMargin
            val dividerBottom = bottom - verticalMargin

            dividerDrawable?.alpha = (20 / 100.0 * 255).toInt()
            dividerDrawable?.setBounds(left, dividerTop, right, dividerBottom)
            dividerDrawable?.draw(c)
        }
    }
}
