package com.tokopedia.productbundlewidget.adapter.viewholder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.unifycomponents.toPx
import kotlin.math.roundToInt


class MultipleBundleItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private var divider: Drawable?

    init {
        divider = getIconUnifyDrawable(context,
            IconUnify.ADD,
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount: Int = parent.childCount
        val mBounds = Rect()
        for (i in 0 until childCount.dec()) {
            val child = parent.getChildAt(i)
            parent.layoutManager?.getDecoratedBoundsWithMargins(child, mBounds)
            val right = mBounds.right + PLUS_ICON_SIZE.toPx() * HORIZONTAL_PERCENT_ADJUSTMENT
            val left = mBounds.right - PLUS_ICON_SIZE.toPx() * HORIZONTAL_PERCENT_ADJUSTMENT

            divider?.setBounds(left.roundToInt(), PLUS_ICON_TOP_POS.toPx(),
                right.roundToInt(), PLUS_ICON_TOP_POS.toPx() + PLUS_ICON_SIZE.toPx())
            divider?.draw(c)
        }
    }

    companion object {
        private const val PLUS_ICON_SIZE = 18
        private const val PLUS_ICON_TOP_POS = 30
        private const val HORIZONTAL_PERCENT_ADJUSTMENT = 0.5f
    }
}
