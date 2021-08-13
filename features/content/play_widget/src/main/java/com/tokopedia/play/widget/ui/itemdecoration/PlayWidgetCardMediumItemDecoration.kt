package com.tokopedia.play.widget.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumTranscodeViewHolder

/**
 * Created by jegul on 10/11/20
 */
class PlayWidgetCardMediumItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val rightMargin = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val viewHolder = parent.getChildViewHolder(view)
        if (viewHolder is PlayWidgetCardMediumChannelViewHolder || viewHolder is PlayWidgetCardMediumTranscodeViewHolder) outRect.right = rightMargin
        else super.getItemOffsets(outRect, view, parent, state)
    }
}