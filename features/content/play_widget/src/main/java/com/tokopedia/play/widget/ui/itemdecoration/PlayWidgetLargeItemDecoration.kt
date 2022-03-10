package com.tokopedia.play.widget.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R.dimen as unifyRDimen

/**
 * @author by astidhiyaa on 10/03/22
 */
class PlayWidgetLargeItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val defaultOffset = context.resources.getDimensionPixelOffset(unifyRDimen.spacing_lvl2)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        outRect.top =  if (position == 0 || position == 1) 0 else defaultOffset
        outRect.bottom = 0
    }
}