package com.tokopedia.play.widget.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by jegul on 10/11/20
 */
class PlayWidgetCardMediumItemDecoration(
    context: Context,
    private val hasOverlay: () -> Boolean,
) : RecyclerView.ItemDecoration() {

    private val rightMargin = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl2)

    private val playWidgetMediumLeftOverlaySpace = context.resources.getDimensionPixelSize(
        R.dimen.play_widget_card_medium_left_overlay_space
    )

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val viewHolder = parent.getChildViewHolder(view)
        if (parent.getChildAdapterPosition(view) == 0 && hasOverlay()) {
            outRect.left = playWidgetMediumLeftOverlaySpace
        }
//        if (viewHolder is PlayWidgetCardMediumChannelViewHolder || viewHolder is PlayWidgetCardMediumTranscodeViewHolder) outRect.right = rightMargin
//        else super.getItemOffsets(outRect, view, parent, state)
    }
}