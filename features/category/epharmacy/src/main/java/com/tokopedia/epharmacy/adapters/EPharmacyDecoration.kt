package com.tokopedia.epharmacy.adapters

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class EPharmacyImagesDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return

        outRect.right = view.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    }
}

internal class EPharmacyDividerItemDecorator(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: android.graphics.Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 1 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + mDivider.intrinsicHeight
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(c)
        }
    }
}