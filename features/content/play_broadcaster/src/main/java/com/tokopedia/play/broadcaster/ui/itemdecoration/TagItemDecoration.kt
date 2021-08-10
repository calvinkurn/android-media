package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created by jegul on 18/02/21
 */
class TagItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset8 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)

        if (position < parent.adapter?.itemCount.orZero()) outRect.right = offset8
        else super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom = offset8
    }
}