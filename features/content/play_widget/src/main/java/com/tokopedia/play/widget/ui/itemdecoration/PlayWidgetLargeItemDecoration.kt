package com.tokopedia.play.widget.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifyprinciples.R.dimen as unifyRDimen

/**
 * @author by astidhiyaa on 10/03/22
 */
class PlayWidgetLargeItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val defaultOffset = context.resources.getDimensionPixelOffset(unifyRDimen.spacing_lvl3)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()

        outRect.top =  if (position == 0) 0 else defaultOffset
        if(position == itemCount - 1) outRect.bottom = 0
    }
}