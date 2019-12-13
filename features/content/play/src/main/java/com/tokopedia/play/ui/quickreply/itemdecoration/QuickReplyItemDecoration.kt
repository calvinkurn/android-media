package com.tokopedia.play.ui.quickreply.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 13/12/19
 */
class QuickReplyItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerWidth = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = dividerWidth
    }
}